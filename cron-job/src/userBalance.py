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

#获取数据库配置 
mdb = MySqlConnection.MySqlConnection()
#md5 转换
currentDay = time.strftime('%Y-%m-%d',time.localtime(time.time()))
log = Logger.Logger(logPath + "/userBalance_" + currentDay + ".log",level='debug')


def updateUserBalance(userId,fee):
    sql = "update xiaolvquan_user set banlance=(banlance+%s),update_time=now() where id=%s" % (fee,userId)
    log.logger.info("updateUserBalance---->" + sql)
    return mdb.update(sql)

def updateUserBillMonth(userId):
    #修改月账单表状态为1，已计入余额，第二次执行，不更新余额 
    sql = "update xiaolvquan_user_bill_month set add_banlance='1',updateTime=now() where user_id=%s" % (userId)
    log.logger.info("updateUserBalance---->" + sql)
    return mdb.update(sql)

def getNewMonthFee(month,userId):
    fee= 0
    sql = """select (promotion_actual_fee + recommend_actual_fee) as fee from xiaolvquan_user_bill_month where add_banlance = '2' and user_id = '%s' and month = '%s'
          """ % (userId,month)
    log.logger.info("getNewMonthFee---->" + sql)
    try:
        result = mdb.select(sql)
        for row in result:
            fee = int(row[0])
    except:
        log.logger.error("Error:getNewMonthFee---->'%s'" % sql)
    return fee

def getAllUserIds():
    userIds = []
    sql = """select id as userId from xiaolvquan_user
          """
    log.logger.info("getAllUserIds---->" + sql)
    try:
        result = mdb.select(sql)
        for row in result:
            userId = int(row[0])
            userIds.append(userId)
    except:
        log.logger.error("Error:getAllUserIds---->'%s'" % sql)
    return userIds


def main(argv):
    if len(argv) == 1:
        month = time.strftime('%Y-%m',time.localtime(time.time()))
    else:
        month = sys.argv[1]
    userIds = getAllUserIds()
    for i, userId in enumerate(userIds):
        fee = getNewMonthFee(month,userId)
        updateUserBalance(userId,fee)
        updateUserBillMonth(userId)        
if __name__ == "__main__":
    main(sys.argv)
