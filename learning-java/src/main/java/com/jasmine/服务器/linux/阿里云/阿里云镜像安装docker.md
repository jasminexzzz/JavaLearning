#### step 1: 安装必要的一些系统工具
```linux
sudo apt-get update
sudo apt-get -y install apt-transport-https ca-certificates curl software-properties-common
```
#### step 2: 安装GPG证书
```linux
curl -fsSL http://mirrors.aliyun.com/docker-ce/linux/ubuntu/gpg | sudo apt-key add -
```
#### Step 3: 写入软件源信息
```linux
sudo add-apt-repository "deb [arch=amd64] http://mirrors.aliyun.com/docker-ce/linux/ubuntu $(lsb_release -cs) stable"
````
#### Step 4: 更新并安装 Docker-CE
```linux
sudo apt-get -y update
sudo apt-get -y install docker-ce
````
#### Step 5: 安装校验
```linux
root@jasmine:$ sudo docker version
```

###### 参考官方教程: https://help.aliyun.com/document_detail/60742.html?spm=a2c4g.11186623.2.13.205d1613JKjI9q
