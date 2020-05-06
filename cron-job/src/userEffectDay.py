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
commissionRate = 0.5

#获取数据库配置 
mdb = MySqlConnection.MySqlConnection()
#md5 转换
currentDay = time.strftime('%Y-%m-%d',time.localtime(time.time()))
log = Logger.Logger(logPath + "/userEffectDay_" + currentDay + ".log",level='debug')

def handleUserEffectDay(day): 
    """计算每天推客推广和推荐收入
       @param day
    """
    log.logger.info("=============计算%s推客的推广和推荐收入开始==============" % day)
    deleteUserEffectDay(day)
    insertPromotionFee(day)
    insertRecommendFee(day)
    insertRebateFee(day)
    log.logger.info("=============计算%s推客的推广和推荐收入完成==============" % day)

def deleteUserEffectDay(day):
    sql = "delete from xiaolvquan_user_effect_day where day='%s'" % day
    log.logger.info("deleteUserEffectDay---->" + sql)
    return mdb.delete(sql)

def insertPromotionFee(day):
    sql = """
             insert into xiaolvquan_user_effect_day(day,user_id,estimate_fee,actual_fee,estimate_cos_price,actual_cos_price,order_num,order_price,promotion_estimate_fee,promotion_actual_fee,recommend_estimate_fee,recommend_actual_fee,rebate_estimate_fee,rebate_actual_fee,updateTime )
             SELECT
                '%s' as day,
             	og.userId as user_id,
             	sum( og.estimateFee ) AS estimate_fee,
             	sum( og.actualFee ) AS actual_fee,
             	sum( og.estimateCosPrice ) AS estimate_cos_price,
             	sum( og.actualCosPrice ) AS actual_cos_price,
             	count( DISTINCT o.orderId ) AS order_num,
             	sum( og.price * skuNum ) AS order_price,
                round(if(og.isRebate = 1,sum( og.estimateFee * cc.nocoupon_rebate_promotion_rate / 100 * cc.nocoupon_promotion_rate / 100 ),sum( og.estimateFee * cc.promotion_rate / 100 )),2) AS promotion_estimate_fee,
                round(if(og.isRebate = 1,sum( og.actualFee * cc.nocoupon_rebate_promotion_rate / 100 * cc.nocoupon_promotion_rate / 100 ),sum( og.actualFee * cc.promotion_rate / 100 )),2) AS promotion_actual_fee,
             	0 AS recommend_estimate_fee,
             	0 AS recommend_actual_fee,
                0 AS rebate_estimate_fee,
                0 AS rebate_actual_fee, 
                now() as updateTime 
             FROM
             	xiaolvquan_order o,
             	xiaolvquan_order_goods og,
             	xiaolvquan_user u,
             	xiaolvquan_commission_config cc 
             WHERE
             	o.orderId = og.orderId 
             	AND u.id = og.userId 
             	AND og.orderUserLevel = cc.user_level 
             	AND og.validCode in (16,17) 
                AND from_unixtime(o.orderTime/1000,'%%Y-%%m-%%d')='%s'
             GROUP BY
             	og.userId
          """ % (day,day) 
    log.logger.info("insertPromotionFee---->" + sql)
    return mdb.insert(sql)

def insertRecommendFee(day):
    sql = """
              insert into xiaolvquan_user_effect_day(day,user_id,estimate_fee,actual_fee,estimate_cos_price,actual_cos_price,order_num,order_price,promotion_estimate_fee,promotion_actual_fee,recommend_estimate_fee,recommend_actual_fee,rebate_estimate_fee,rebate_actual_fee,updateTime )
              SELECT
              	'%s' AS DAY,
              	t.user_id AS user_id,
              	0 AS estimate_fee,
              	0 AS actual_fee,
              	0 AS estimate_cos_price,
              	0 AS actual_cos_price,
              	0 AS order_num,
              	0 AS order_price,
              	0 AS promotion_estimate_fee,
              	0 AS promotion_actual_fee,
              	ROUND(sum( promotion_estimate_fee * cc.recommend_rate / 100 ),2) AS recommend_estimate_fee,
              	ROUND(sum( promotion_actual_fee * cc.recommend_rate / 100 ),2) AS recommend_actual_fee ,
              	0 AS rebate_estimate_fee,
              	0 AS rebate_actual_fee,
                now() as updateTime 
              FROM
              	(
              SELECT
              	u.parent_user_id AS user_id,
                if(og.isRebate = 1,sum( og.estimateFee * xcc.nocoupon_rebate_promotion_rate / 100 * xcc.nocoupon_promotion_rate / 100 ),sum( og.estimateFee * xcc.promotion_rate / 100 )) AS promotion_estimate_fee,
                if(og.isRebate = 1,sum( og.actualFee * xcc.nocoupon_rebate_promotion_rate / 100 * xcc.nocoupon_promotion_rate / 100 ),sum( og.actualFee * xcc.promotion_rate / 100 )) AS promotion_actual_fee
              FROM
              	xiaolvquan_order o,
              	xiaolvquan_order_goods og,
              	xiaolvquan_user u,
                xiaolvquan_commission_config xcc 
              WHERE
              	o.orderId = og.orderId 
              	AND u.id = og.userId 
              	AND og.validCode in (16,17)
                AND xcc.user_level = og.orderUserLevel
                AND from_unixtime(o.orderTime/1000,'%%Y-%%m-%%d')='%s'
              GROUP BY
              	u.parent_user_id 
              	) t
              	JOIN xiaolvquan_user u ON t.user_id = u.id
              	JOIN xiaolvquan_commission_config cc ON u.user_level = cc.user_level 
              GROUP BY
              	t.user_id 
          """ % (day,day)
    log.logger.info("insertRecommendFee---->" + sql)
    return mdb.insert(sql)

def insertRebateFee(day):
    pass
    sql = """
              insert into xiaolvquan_user_effect_day(day,user_id,estimate_fee,actual_fee,estimate_cos_price,actual_cos_price,order_num,order_price,promotion_estimate_fee,promotion_actual_fee,recommend_estimate_fee,recommend_actual_fee,rebate_estimate_fee,rebate_actual_fee,updateTime )
              SELECT
                 '%s' as day,
                 og.buyUserId as user_id,
               	 0 AS estimate_fee,
               	 0 AS actual_fee,
               	 0 AS estimate_cos_price,
               	 0 AS actual_cos_price,
               	 0 AS order_num,
               	 0 AS order_price,
               	 0 AS promotion_estimate_fee,
               	 0 AS promotion_actual_fee,
               	 0 AS recommend_estimate_fee,
               	 0 AS recommend_actual_fee,
                 round(if(og.isRebate = 1,sum( og.estimateFee * cc.nocoupon_rebate_promotion_rate / 100 * cc.nocoupon_rebate_rate / 100 ),0),2)  AS rebate_estimate_fee,
                 round(if(og.isRebate = 1,sum( og.actualFee * cc.nocoupon_rebate_promotion_rate / 100 * cc.nocoupon_rebate_rate / 100 ),0),2) AS rebate_actual_fee, 
                 now() as updateTime 
             FROM
             	xiaolvquan_order o,
             	xiaolvquan_order_goods og,
             	xiaolvquan_user u,
             	xiaolvquan_commission_config cc 
             WHERE
             	o.orderId = og.orderId 
             	AND u.id = og.userId 
             	AND og.orderUserLevel = cc.user_level 
             	AND og.validCode in (16,17) 
                AND og.buyUserId <> 0
                AND from_unixtime(o.orderTime/1000,'%%Y-%%m-%%d')='%s'
             GROUP BY
             	og.buyUserId
          """ % (day,day)
    log.logger.info("insertRebateFee---->" + sql)
    return mdb.insert(sql)

def main(argv):
    if len(argv) == 1:
        day = str(time.strftime('%Y-%m-%d',time.localtime(time.time())))
    else:
        day = sys.argv[1]
    print day
    handleUserEffectDay(day)
if __name__ == "__main__":
    main(sys.argv)
