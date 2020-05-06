#!/bin/bash
script=/home/xiaolvquan/xiaolvquan/cron-job/src
log=/home/xiaolvquan/xiaolvquan/cron-job/logs

cd ${script}
nowdate=`date +%Y%m01`
start_time=`date -d"$nowdate last month" +"%Y-%m-%d"`
end_time=`date '+%Y-%m-%d'`
echo "${end_time} > time >= ${start_time}"
while [ "${start_time}" != "${end_time}" ]
do
  day=`date -d "${start_time}" +"%Y-%m-%d"`
  echo ${day}
  /usr/bin/python userEffectDay.py "${day}"
  start_time=`date -d "1 days ${start_time}" +"%Y-%m-%d"`

done
