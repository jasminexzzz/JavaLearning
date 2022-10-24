package com.jasmine.A1_java.high.数据库.MySql.数据库问题.链接问题;

/**
 * @author : jasmineXz
 */
public class 错误解决方式汇总 {
    /**
     1. Lost connection to MySQL server at ‘reading initial communication packet', system error: 0 mysql远程连接问题
        1).
            编辑配置文件 vim /etc/my.cnf
            增加配置     skip-name-resolve
            重启服务     service mysqld restart


     2. cant_connectto_mysql_server_10060
         第一个检查：确认mysql服务器启动成功
             点击"此电脑"右键打开管理进入注册表环境中
             选择左边栏目中的服务与应用程序的子目录"服务"
             进入"服务"后,我们可以看到所有服务的运行情况.在键盘上输一个"m",找到mysql的位置.
             打开mysql,查看服务器是否在运行中.如果处于停止状态,可以手动打开.因为手动打开,操作起来比较麻烦,可以选择启动方式为自动,下一次在UI界面连
             接数据库时就可以不用手动启动服务器.

         第二个检查：确认mysql是否已经开启远程连接
             在cmd中登录本机mysql服务,使用mysql数据库.点击"win+R",打开运行,在运行输入框里面输入"cmd".如果第一次在cmd中打开mysql,会出现下面的情况.
             Ps:解决办法
             选中"此电脑",点击右键选择属性->"高级系统设置"
             接着点击"高级"下的"环境变量"
             在环境变量系统变量里面找到"path",点击编辑,如下图所示：
             按"win+D"切换到桌面,找到mysql的安装目录,复制其地址到path中,用英文分号分割path先前的内容,然后一直点击确定.
             这样就配置好了环境变量,重新打开cmd登录本机mysql服务,使用mysql数据库
             输入select host,user from user; 显示如下,表示已经可以远程连接.然后flush privileges;进行刷新（平时更改配置的时候基本是需要刷新的,
             不管是windows还是linux）
             若没有如图显示,则首先对mysql授权远程连接.如下：
             登录本机mysql,使用mysql数据库,因为用户表都是存在mysql数据库里面的.
             输入命令：GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'root' WITH GRANT OPTION;
             'root'@'%' 中,root表示授权的用户,@表示授权的IP,@表示授权所有IP.在这里,我们采用的是授权所有IP的所有功能,当然,我们还可以对数据库的某
             些功能进行限定.
             1.    检查mysql的配置文件(一般在mysql安装目录下的mysql.ini),用记事本打开.检查是否有绑定IP,即bind-address,若有,即用#注释或者删除即
             可,同时查看是否允许TCP/IP.
         4. 防火墙设置
         可以检查一下windows防火墙是否关闭,我们先关闭防火墙,然后再尝试连接.
         http://img.blog.csdn.net/20160603210237617?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQv/font/5a6L5L2T/fontsize/400/fill/I0JBQk
         FCMA==/dissolve/70/gravity/Center
         经过上面常见的检查以后,mqsql应该可以成功连接了.如果还有问题,我们可以检查我们的网络是否通了,有没有对应好局域网与公网的防火墙之类的.




     二. 有时候重启服务就可解决
     */
}
