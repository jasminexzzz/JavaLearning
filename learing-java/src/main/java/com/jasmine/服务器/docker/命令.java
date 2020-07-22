package com.jasmine.服务器.docker;

/**
 * @author : jasmineXz
 */
public class 命令 {
    /**
     1.　查看镜像路径
     2.　<查看镜像　　　　　　　　　 ：docker images
     3.　<查看正在运行的容器　　　　 ：docker ps
     4.　<查看所有容器　　　　　　　 ：docker ps -a (可以查看<container id>)
     5.　终止一个运行中的容器　　　　：docker stop $CONTAINER_ID
     6.　启动一个容器　　　　　　　　：docker start $CONTAINER_ID
     7.　重启一个容器　　　　　　　　：docker restart $CONTAINER_ID
     8.　查看容器的映射的端口　　　　：docker port <container name or id>
     9.　查看容器的IP地址　　　　　　：docker inspect <container name or id>| grep IPAddress    
    10.　查看容器运行日志　　　　　　：docker logs <container name or id>
    11.　实时查看容器日志　　　　　　：docker logs -f -t --tail 行数 <container name or id>
    12.　删除容器　　　　　　　　　　：docker rmi <container name or id>
            注意点：
             1. 删除前需要保证容器是停止的  stop
             2. 需要注意删除镜像和容器的命令不一样. docker rmi <container name or id>  ,其中 容器(rm)  和 镜像(rmi)
             3. 顺序需要先删除容器
    13.　如果想要把镜像存出到本地文件,可以使用docker save命令#docker save –o /data/testimage.tar testimage:latest (以testimage镜像名称为例)
    14.　从文件载入镜像可以使用Docker load命令# docker load < testimage.tar
    15.　<进入容器　　　　　　　　   ：docker exec -it <container name or id>  /bin/bash



     1.　搜索镜像　：docker search
     　　　　例如　：docker search elasticsearch:5.6.8
     2.　拉取镜像　：docker pull mysql
     　　　　例如　：docker pull mysql:8.0.18
     3.　运行镜像　：docker run
     　　　　例如　：docker run -p 3306:3306 --name mysql -e MYSQL_ROOT_PASSWORD=admin -d mysql
                     docker run          : 运行镜像
                     -p	3306:3306        : 代表端口映射,格式为 宿主机映射端口:容器运行端口
                     -e	                 : 代表添加环境变量
                     -m 512M             : 代码最大内存512
                     MYSQL_ROOT_PASSWORD : 是设置root用户的登陆密码
                     -d                  : 作为一个守护进程在后台运行
                     mysql               : 容器名称
                     -v                  : E:\WorkStation\MiddleWare\kafka:/opt/kafka/config/server.properties


     1. docker 下安装vim编辑器
        apt-get update
        apt-get install vim


     1. docker cp [容器名/容器ID]:[配置文件的路径] [本地操作系统的路径,如:E:\]
        如 : docker cp kafka:/opt/kafka/config/server.properties E:\
     2. docker ps [本地操作系统的路径,如:E:\] [容器名/容器ID]:[配置文件的路径]
        如 : docker cp E:\server.properties kafka:/opt/kafka/config/server.properties
     */
}
