package com.jasmine.服务器.linux.centos7;

/**
 * @author : jasmineXz
 */
@SuppressWarnings("all")
public class 命令 {
    /**
     * ─────────────────────────────────────────────────────────────────────────────────────────────────────
     * 启动防火墙 : systemctl start firewalld.service
     *
     * 关闭防火墙 : systemctl stop firewalld.service
     *              部署项目后，端口可能会被关闭，导致无法访问，可以关闭防火墙来放开端口
     *
     * 重启防火墙 : systemctl restart firewalld.service
     * ─────────────────────────────────────────────────────────────────────────────────────────────────────
     * 新增开放一个端口号 : firewall-cmd --zone=public --add-port=80/tcp --permanent
     * # 说明:
     * # –zone #作用域
     * # –add-port=80/tcp #添加端口，格式为：端口/通讯协议
     * # –permanent 永久生效，没有此参数重启后失效
     * # 注意:新增/删除操作需要重启防火墙服务.
     * # 其他PC telnet开放的端口必须保证本地 telnet 127.0.0.1 端口号 能通。本地不通不一定是防火墙的问题。
     *
     * 多个端口 : firewall-cmd --zone=public --add-port=80-90/tcp --permanen
     *
     * 查看本机已经启用的监听端口: ss -ant
     * # centos7以下使用netstat -ant,7使用ss
     *
     * 删除 : firewall-cmd --zone=public --remove-port=80/tcp --permanent
     * ─────────────────────────────────────────────────────────────────────────────────────────────────────
     * 网络相关
     *
     * 查看ip : ip addr
     *
     */
}
