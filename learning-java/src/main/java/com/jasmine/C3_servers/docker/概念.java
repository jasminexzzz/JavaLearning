package com.jasmine.C3_servers.docker;

/**
 * @author : jasmineXz
 */
public class 概念 {
    /**
     1. 容器

     2. 镜像

     3. 仓库

     4. 宿主机
        安装docker的机器

     4. 挂载
        docker可以支持把一个宿主机上的目录挂载到镜像里.
        通过 -v 参数,冒号前为宿主机目录,必须为绝对路径,冒号后为镜像内挂载的路径.
        docker run -it -v /home/dock/Downloads:/usr/Downloads:ro ubuntu64 /bin/bash

     */
}
