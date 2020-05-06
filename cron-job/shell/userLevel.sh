#!/bin/bash
script=/home/xiaolvquan/xiaolvquan/cron-job/src
log=/home/xiaolvquan/xiaolvquan/cron-job/logs
if [ ! -d "$log" ]; then
    mkdir -p "$log"
fi
cd $script
/usr/bin/python userLevel.py
