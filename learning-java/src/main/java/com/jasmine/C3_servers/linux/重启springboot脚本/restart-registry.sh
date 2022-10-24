#!/bin/bash
# 重启注册中心脚本
# 需要注意脚本名不要与进程名相同,否则 kill -9 命令会把脚本进程一起杀死
pid=`ps aux | grep common-registry.jar | grep -v grep | awk '{print $2}'`
echo "进程ID : " $pid
kill -9 $pid
echo "进程" $pid "已被杀死"
echo "开始重启注册中心(common-registry)"
nohup java -Xms300m -Xmx300m -jar /usr/local/jasmine/bjscloud/common-registry/common-registry.jar &

# 启动不起来需要在vi中执行命令
# :set ff=unix