package com.jasmine.C2_middleware.Nginx;

/**
 * @author : jasmineXz
 */
public class 命令 {
    /**
     启动 : /usr/local/ngnix/sbin/nginx
     关闭 : /usr/local/ngnix/sbin/nginx -s stop
     重启 : /usr/local/ngnix/sbin/nginx -s reload
     读取配置 :

     查看进程 : ps -ef | grep nginx
        如果能看到两个相邻ID的进程，说明启动成功
        root      10309      1  0 17:25 ?        00:00:00 nginx: master process ./nginx
        nobody    10310  10309  0 17:25 ?        00:00:00 nginx: worker process

     查看配置文件路径 :
        1. 找ps -ef|grep nginx到nginx运行位置

        2. 查看Nginx配置文件地址
        ./nginx -t
     */
}
