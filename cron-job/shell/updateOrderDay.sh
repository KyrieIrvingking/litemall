#!/bin/bash
script=/home/xiaolvquan/xiaolvquan/cron-job/src
log=/home/xiaolvquan/xiaolvquan/cron-job/logs
if [ ! -d "$log" ]; then
    mkdir -p "$log"
fi
cd $script
nowdate=`date +%Y%m01`
start_time=`date -d"$nowdate last month" +"%Y-%m-%d 00"`
end_time=`date '+%Y-%m-%d %H'`
echo "${end_time} > time >= ${start_time}"
while [ "${start_time}" != "${end_time}" ]
do
  hour=`date -d "${start_time}" +"%Y%m%d%H"`
  echo ${hour}
  /usr/bin/python fetchOrders.py "${hour}"
  start_time=`date -d "1 hours ${start_time}" +"%Y-%m-%d %H"`
  sleep 1
  
done
