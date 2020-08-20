修改网卡配置文件 vi /etc/sysconfig/network-scripts/ifcfg-ens32    (最后一个为网卡名称)

##动态获取IP地址
需要修改两处地方即可

（1）bootproto=dhcp

（2）onboot=yes

````
[root@mini ~]# systemctl restart network

[root@mini ~]# 
````

###配置静态IP地址
设置静态IP地址与动态iIP差不多，也是要修改网卡配置文件 vi /etc/sysconfig/network-scripts/ifcfg-ens32    (最后一个为网卡名称)

（1）bootproto=static

（2）onboot=yes

（3）在最后加上几行，IP地址、子网掩码、网关、dns服务器

````
IPADDR=192.168.1.180
NETMASK=255.255.255.0
GATEWAY=192.168.1.1
DNS1=119.29.29.29
DNS2=8.8.8.8
````
重启服务器即可