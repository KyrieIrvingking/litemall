# -*- coding: utf-8 -*-
import os,sys
import hashlib, datetime, time
sys.path.append(r'../conf/')
sys.path.append(r'../util/')
import requests,json
import ParseConf,logging
import MySQLdb
import MySqlConnection 
import Logger
hasMore = False
#定义配置文件 
basePath = "/home/xiaolvquan/xiaolvquan/cron-job"
logPath = "/home/xiaolvquan/xiaolvquan/cron-job/logs"
globalConfFile = "%s/conf/global.conf" % basePath
globalConf = ParseConf.ParseConf(globalConfFile)
#获取 api 参数
appKey = globalConf.readGet('jdUnionApi', 'appKey') 
appSecret = globalConf.readGet('jdUnionApi', 'appSecret') 
hostUrl = globalConf.readGet('jdUnionApi', 'hostUrl') 
#获取数据库配置 
mdb = MySqlConnection.MySqlConnection()
#md5 转换
md5 = lambda pwd: hashlib.md5(pwd).hexdigest()
currentDay = time.strftime('%Y-%m-%d',time.localtime(time.time()))
log = Logger.Logger(logPath + "/fetchOrders_" + currentDay + ".log",level='debug')
unionTagPinGou = ['00000100','00000111']
def getSign(parameters):
    """ 签名
    @param parameters dict: uri请求参数(包含除signature外的公共参数)
    """
    if "signature" in parameters:
        parameters.pop("signature")
    # NO.1 参数排序
    _my_sorted = sorted(parameters.items(), key=lambda parameters: parameters[0])
    # NO.2 排序后拼接字符串
    canonicalizedQueryString = ''
    for (k, v) in _my_sorted:
        canonicalizedQueryString += '{}{}'.format(k,v)
    canonicalizedQueryString = appSecret+canonicalizedQueryString+appSecret
    # NO.3 加密返回签名: signature
    return md5(canonicalizedQueryString).upper()

def make_url(params={}):
    """生成请求参数
    @param params dict: uri请求参数(不包含公共参数)
    """
    if not isinstance(params, dict):
        raise TypeError("params is not a dict")
    # 设置公共参数
    currentTime = time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time()))
    publicParams = dict(timestamp=currentTime ,v='1.0',sign_method='md5',format='json',method='jd.union.open.order.query',app_key=appKey)
    # 添加加公共参数
    allParams ={}
    paramsJson = json.dumps(params).replace(' ', '')
    allParams['param_json'] = paramsJson
    #allParams['param_json']='{"orderReq":{"time":"2019060611","pageNo":1,"pageSize":20,"type":1}}'
    for k,v in publicParams.iteritems():
        allParams[k] = v
    uri = ''
    for k,v in allParams.iteritems():
        uri += '{}={}&'.format(k,v)
    uri += 'sign=' + getSign(allParams)
    return uri

def getRequest(queryTime,no):
    """GET方式请求京东联盟api
       @param queryTime:请求时间，2019060611
    """
    params = {}
    #time 时间,pageNo 开始页,pageSize 每页条数,type=1 按照下单时间
    orderReq = dict(time = queryTime,pageNo = no,pageSize = 500,type = 1)
    params['orderReq'] = orderReq 
    url = 'https://router.jd.com/api?' + make_url(params)
    r = requests.get(url,'utf-8')
    res = r.text
    return res

def handleOrder(order): 
    """新增或更新order、order_goods表
       @param order
    """
    orderId = order['orderId']
    skuList = order['skuList']
    skuListNew = []
    skuIdDel = []
    num = getOrderNumByOrderId(orderId)
    if num == 0:
        insertOrder(order)
    else:
        updateOrder(order)
    for i, sku in enumerate(skuList):
       skuId = sku['skuId']
       unionTag = sku['unionTag']
       orderGoods = getOrderGoodsListByOrderId(orderId,skuId)
       if unionTag in unionTagPinGou:
           skuIdDel.append(skuId)
           skuListNew.append(sku)
           deleteOrderGoods(orderId,skuId)
       else:    
           #新增订单 
           if len(orderGoods) == 0:
               insertOrderGoods(sku,orderId)
           #更新订单-非拼购
           elif len(orderGoods) == 1:
               updateOrderGoods(sku,orderId)
    #更新订单-拼购，先删除，再新增
    delIds = list(set(skuIdDel))
    for i, delId in enumerate(delIds):
        deleteOrderGoods(orderId,delId)
    for i, sku in enumerate(skuListNew):
        insertOrderGoods(sku,orderId)

def getOrderGoodsNumByOrderId(orderId,skuId):
    num = 0
    sql = "select count(1) as number from xiaolvquan_order_goods where orderId='%s' and skuId='%s'" % (orderId,skuId)
    try:
        result = mdb.select(sql)
        for row in result:
            num = int(row[0])
    except:
        log.logger.error("Error:getOrderGoodsNumByOrderId,sql:'%s'" % sql)
    return num

def getOrderGoodsListByOrderId(orderId,skuId):
    goodsList = []
    sql = "select orderId,skuId,unionTag  from xiaolvquan_order_goods where orderId='%s' and skuId='%s'" % (orderId,skuId)
    try:
        result = mdb.select(sql)
        for row in result:
            dicts = {}
            orderId = row[0]
            skuId = row[1]
            unionTag = row[2]
            dicts = {'orderId':orderId,'skuId':skuId,'unionTag':unionTag}
            goodsList.append(dicts)
    except:
        log.logger.error("Error:getOrderGoodsListByOrderId,sql:'%s'" % sql)
    return goodsList

def insertOrderGoods(sku,orderId):
    updateTime = time.strftime('%Y-%m-%d %H:%M:%S', time.localtime())
    status = '0'
    userId = '0'#推广人
    buyUserId = '0'#购买人
    isRebate = '0'#是否有返利收入
    orderUserLevel = '1' #下单时用户等级,默认普通用户
    subUnionId = sku['subUnionId'] 
    subUnionIdArr = subUnionId.split("_")
    if len(subUnionIdArr) >= 1:
        userId= subUnionIdArr[0]
    if len(subUnionIdArr) >=2:
        buyUserId= subUnionIdArr[1]
    if len(subUnionIdArr) >=3:
        isRebate= subUnionIdArr[2]
    if len(subUnionIdArr) >=4:
        orderUserLevel= subUnionIdArr[3]
    commissionRateXlq = ''
    sql = "insert into xiaolvquan_order_goods (orderId,skuId,skuName,cid1,cid2,cid3,skuNum,skuReturnNum,frozenSkuNum,price,actualFee,actualCosPrice,\
                                               estimateCosPrice,estimateFee,commissionRate,finalRate,ext1,payMonth,pid,popId,positionId,siteId,subSideRate,\
                                               subUnionId,subsidyRate,traceType,unionAlias,unionTag,unionTrafficGroup,validCode,buyUserId,isRebate,userId,orderUserLevel,commissionRateXlq,\
                                               status,updateTime) values( \
                                               \"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\
                                               \"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\
                                               \"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\")""" % \
                                               (orderId,sku['skuId'],sku['skuName'],sku['cid1'],sku['cid2'],sku['cid3'],sku['skuNum'],\
                                               sku['skuReturnNum'],sku['frozenSkuNum'],sku['price'],sku['actualFee'],sku['actualCosPrice'],\
                                               sku['estimateCosPrice'],sku['estimateFee'],sku['commissionRate'],sku['finalRate'],sku['ext1'],\
                                               sku['payMonth'],sku['pid'],sku['popId'],sku['positionId'],sku['siteId'],sku['subSideRate'],\
                                               sku['subUnionId'],sku['subsidyRate'],sku['traceType'],sku['unionAlias'],sku['unionTag'],\
                                               sku['unionTrafficGroup'],sku['validCode'],buyUserId,isRebate,userId,orderUserLevel,commissionRateXlq,status,updateTime)
    log.logger.info("insertOrderGoods---->" + sql)
    return mdb.insert(sql)

def deleteOrderGoods(orderId,skuId):
    updateTime = time.strftime('%Y-%m-%d %H:%M:%S', time.localtime())
    sql = """delete from xiaolvquan_order_goods where orderId='%s' and skuId='%s'""" % (orderId,skuId)
    log.logger.info("---->deleteOrderGoods" + sql)
    return mdb.delete(sql)

def updateOrderGoods(sku,orderId):
    updateTime = time.strftime('%Y-%m-%d %H:%M:%S', time.localtime())
    sql = "update xiaolvquan_order_goods set \
           price=\"%s\",actualFee=\"%s\",actualCosPrice=\"%s\",estimateCosPrice=\"%s\",estimateFee=\"%s\",\
           commissionRate=\"%s\",finalRate=\"%s\",validCode=\"%s\",payMonth=\"%s\",ext1=\"%s\",validCode=\"%s\",\
           updateTime=\"%s\" where orderId=\"%s\" and skuId=\"%s\"" % \
           (sku['price'],sku['actualFee'],sku['actualCosPrice'],sku['estimateCosPrice'],sku['estimateFee'],\
           sku['commissionRate'],sku['finalRate'],sku['validCode'],sku['payMonth'],sku['ext1'],\
           sku['validCode'],updateTime,orderId,sku['skuId'])
    log.logger.info("updateOrderGoods---->" + sql)
    return mdb.update(sql)

def getOrderNumByOrderId(orderId):
    num = 0
    sql = "select count(1) as number from xiaolvquan_order where orderId='%s'" % orderId
    log.logger.info("getOrderNumByOrderId---->" + sql)
    try:
        result = mdb.select(sql)
        for row in result:
            num = int(row[0])
    except:
        log.logger.error("Error:queryOrdersNumByOrderId,sql:'%s'" % sql)
    return num

def insertOrder(order):
    updateTime = time.strftime('%Y-%m-%d %H:%M:%S', time.localtime())
    status = '0'
    sql = "insert into xiaolvquan_order(orderId,parentId,orderEmt,payMonth,plus,popId,unionId,ext1,validCode,orderTime,finishTime,status,updateTime) values \
    (\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\")" % \
    (order['orderId'],order['parentId'],order['orderEmt'],order['payMonth'],order['plus'],order['popId'],order['unionId'],\
    order['ext1'],order['validCode'],order['orderTime'],order['finishTime'],status,updateTime)
    log.logger.info("insertOrder---->" + sql)
    return mdb.insert(sql)

def updateOrder(order):
    updateTime = time.strftime('%Y-%m-%d %H:%M:%S', time.localtime())
    sql = "update xiaolvquan_order set parentId=\"%s\",payMonth=\"%s\",ext1=\"%s\",validCode=\"%s\",finishTime=\"%s\",updateTime=\"%s\" where orderId=\"%s\""  % (order['parentId'],order['payMonth'],order['ext1'],order['validCode'],order['finishTime'],updateTime,order['orderId'])
    pass
    log.logger.info("updateOrder---->" + sql)
    return mdb.update(sql)

def run(queryTime,pageNo):
    res = getRequest(queryTime,pageNo)
    result = json.loads(res)['jd_union_open_order_query_response']['result']
    resultDicts= json.loads(result)
    code = resultDicts['code']
    hasMore = resultDicts['hasMore']
    orders = {}
    if code == 200:
        if resultDicts.has_key('data'):
            orders = resultDicts['data']
        else:
            log.logger.info("no orders in %s" % queryTime)
    else:
        log.logger.info("request error,code = %s" % code)
    log.logger.info("====================获取到 %s 个订单，开始更新数据！======================" % str(len(orders)))
    for i, order in enumerate(orders):
        log.logger.info("====================订单%s更新开始！======================" % order['orderId']) 
        handleOrder(order)
        log.logger.info("====================订单%s更新完成！======================" % order['orderId'])
    log.logger.info("====================数据更新完成,共%s个订单！======================" % str(len(orders))) 

def main(argv):
    if len(argv) == 1:
        queryTime = time.strftime('%Y%m%d%H',time.localtime(time.time()))
    else:
        queryTime = sys.argv[1]
    pageNo = 1
    run(queryTime,pageNo)
    while hasMore == True:
        pageNo = pageNo +1
        run(queryTime,pageNo)
if __name__ == "__main__":
    main(sys.argv)
