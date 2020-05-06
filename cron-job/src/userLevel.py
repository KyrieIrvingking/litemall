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


def updateUserLevel(userId,userLevel):
    sql = "update xiaolvquan_user set user_level=%s,update_time=now() where id=%s" % (userLevel,userId)
    log.logger.info("updateUserFensiNum---->" + sql)
    return mdb.update(sql)

def getXiashuNum(userId):
    num = 0
    sql = """select count(*) as num from xiaolvquan_user where parent_user_id = '%s'
          """ % userId
    log.logger.info("getXiashuNum---->" + sql)
    try:
        result = mdb.select(sql)
        for row in result:
            num = int(row[0])
    except:
        log.logger.error("Error:getXiashuNum---->'%s'" % sql)
    return num

def getAllUserIds():
    userIds = []
    sql = """select id as userId from xiaolvquan_user xu where id not in (select user_id from xiaolvquan_user_whitelist)
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

def getCommissionConfig():
    commissionConfigDict = {}
    commissionConfigList = []
    sql = """select user_level,min_invite_users,max_invite_users  from xiaolvquan_commission_config
          """
    log.logger.info("getCommissionConfig---->" + sql)
    try:
        result = mdb.select(sql)
        for row in result:
            user_level = row[0]
            min_invite_users = row[1]
            max_invite_users = row[2]
            commissionConfigDict={'user_level': user_level,'min_invite_users': min_invite_users,'max_invite_users': max_invite_users}
            commissionConfigList.append(commissionConfigDict)
    except:
        log.logger.error("Error:getCommissionConfig---->'%s'" % sql)
    return commissionConfigList 

def main(argv):
    userIds = getAllUserIds()
    commissionConfigList = getCommissionConfig()
    for i, userId in enumerate(userIds):
        xiashuNum = getXiashuNum(userId)
        for i, commissionConfigDict in enumerate(commissionConfigList):
            minInviteUsers = commissionConfigDict['min_invite_users']
            maxInviteUsers = commissionConfigDict['max_invite_users']
            newUserLevel = commissionConfigDict['user_level']
            if xiashuNum >= minInviteUsers and xiashuNum < maxInviteUsers:
                updateUserLevel(userId,newUserLevel)
if __name__ == "__main__":
    main(sys.argv)
