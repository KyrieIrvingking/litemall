#!/bin/bash
script=/home/xiaolvquan/xiaolvquan/cron-job/src
log=/home/xiaolvquan/xiaolvquan/cron-job/logs
last_month=`date +%Y-%m -d "1 month ago"`
current_month=`date +%Y-%m`
if [ ! -d "$log" ]; then
    mkdir -p "$log"
fi
cd $script
echo $last_month
echo $current_month
/usr/bin/python userBillMonth.py "${last_month}"
/usr/bin/python userBillMonth.py "${current_month}"
