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
log = Logger.Logger(logPath + "/userBillMonth_" + currentDay + ".log",level='debug')


def insertUserBillMonth(month):
    sql = """
          INSERT INTO xiaolvquan_user_bill_month ( user_id, actual_fee, promotion_actual_fee, recommend_actual_fee,promotion_estimate_fee,recommend_estimate_fee,rebate_estimate_fee,rebate_actual_fee, STATUS, add_banlance, MONTH, updateTime ) 
          SELECT
              user_id,
              sum( actual_fee ) AS actual_fee,
              sum( promotion_actual_fee ) AS promotion_actual_fee,
              sum( recommend_actual_fee ) AS recommend_actual_fee,
              sum( promotion_estimate_fee ) AS promotion_estimate_fee,
              sum( recommend_estimate_fee ) AS recommend_estimate_fee,
              sum( rebate_estimate_fee ) AS rebate_estimate_fee,
              sum( rebate_actual_fee ) AS recommend_actual_fee,
              '1' AS STATUS,
              '2' AS add_balance,
              '%s' AS MONTH,
              now( ) AS updateTime 
              FROM
              	xiaolvquan_user_effect_day 
              WHERE
              	DAY like '%s%%'
              GROUP BY
              	user_id
          """ % (month,month) 
    log.logger.info("insertUserBillMonth---->" + sql)
    return mdb.insert(sql)

def deleteUserBillMonth(month):
    sql = "delete from xiaolvquan_user_bill_month where month='%s' and status<>'2'" % month
    log.logger.info("deleteUserBillMonth---->" + sql)
    return mdb.delete(sql)


def main(argv):
    if len(argv) == 1:
        month = time.strftime('%Y-%m',time.localtime(time.time()))
        print month
    else:
        month = sys.argv[1]
    log.logger.info("====================开始更新账单！======================")
    deleteUserBillMonth(month)
    insertUserBillMonth(month)
    log.logger.info("====================更新账单完成！======================")
if __name__ == "__main__":
    main(sys.argv)
