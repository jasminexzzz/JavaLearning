package com.jasmine.C3_servers.tomcat;

/**
 * @author : jasmineXz
 */
public class tomcat启动日志乱码 {
    /**
     打开cd到tomcat/conf/目录下
     修改logging.properties
     找到
     java.util.logging.ConsoleHandler.encoding = utf-8这行
     更改为
     java.util.logging.ConsoleHandler.encoding = GBK
     */
}
