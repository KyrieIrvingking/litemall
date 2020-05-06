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
log = Logger.Logger(logPath + "/userFensi_" + currentDay + ".log",level='debug')

def updateUserFensiNum(userId,fensiNum):
    sql = "update xiaolvquan_user set fensi_num=%s,update_time=now() where id=%s" % (fensiNum,userId)
    log.logger.info("updateUserFensiNum---->" + sql)
    return mdb.delete(sql)

def getFensiNumById(userId):
    num = 0
    sql = """
             SELECT
             	count(id) as fensi
             FROM
             	( SELECT * FROM xiaolvquan_user WHERE parent_user_id > 0 ORDER BY parent_user_id, id DESC ) realname_sorted,
             	( SELECT @pv := %s ) initialisation 
             WHERE
             	( FIND_IN_SET( parent_user_id, @pv ) > 0 AND @pv := concat( @pv, ',', id ) )
          """ % userId
    log.logger.info("insertPromotionFee---->" + sql)
    try:
        result = mdb.select(sql)
        for row in result:
            num = int(row[0])
    except:
        log.logger.error("Error:getFensiNumById---->'%s'" % sql)
    return num

def getAllUserIds():
    userIds = []
    sql = """
          select id as userId from xiaolvquan_user
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
    userIds = getAllUserIds()
    print userIds
    for i, userId in enumerate(userIds):
        fensiNum = getFensiNumById(userId)
        updateUserFensiNum(userId,fensiNum)
if __name__ == "__main__":
    main(sys.argv)
