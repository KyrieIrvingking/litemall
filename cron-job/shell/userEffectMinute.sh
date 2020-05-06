#!/bin/bash
script=/home/xiaolvquan/xiaolvquan/cron-job/src
log=/home/xiaolvquan/xiaolvquan/cron-job/logs
if [ ! -n "$1" ] ;then
    day=`date '+%Y-%m-%d'`
else   
    day=$1
fi
if [ ! -d "$log" ]; then
    mkdir -p "$log"
fi
cd $script
echo $day
/usr/bin/python userEffectDay.py "${day}"
