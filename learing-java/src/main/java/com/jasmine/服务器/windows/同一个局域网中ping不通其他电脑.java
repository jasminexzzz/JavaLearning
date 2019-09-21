package com.jasmine.服务器.windows;

/**
 * @author : jasmineXz
 */
public class 同一个局域网中ping不通其他电脑 {
    /**
     * 因为windows防火墙默认设置的是不让别人ping通的，所以方法就是，修改防火墙相关设置。
     * 步骤：
     * 控制面板 →
     * 系统和安全 →
     * Windows防火墙 →
     * 高级设置 →
     * 入站规则 →
     * 文件和打印机共享（回显请求 - ICMPv4-In）设置为启用
     */

}
