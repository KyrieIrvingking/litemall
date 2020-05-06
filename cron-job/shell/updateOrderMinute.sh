#!/bin/bash
script=/home/xiaolvquan/xiaolvquan/cron-job/src
log=/home/xiaolvquan/xiaolvquan/cron-job/logs
if [ ! -n "$1" ] ;then
    hour=`date '+%Y%m%d%H'`
else   
    hour=$1
fi
echo $date
if [ ! -d "$log" ]; then
    mkdir -p "$log"
fi
cd $script
last_hour=`date '+%Y-%m-%d %H'`
last_hour=`date -d "1 hours ago ${last_hout}" +"%Y%m%d%H"`
echo $last_hour
/usr/bin/python fetchOrders.py "${hour}"
/usr/bin/python fetchOrders.py "${last_hour}"
