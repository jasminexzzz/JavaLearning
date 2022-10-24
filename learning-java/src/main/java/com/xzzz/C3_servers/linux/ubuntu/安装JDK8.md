# Ubuntu 安装 JDK

## 手动下载压缩包安装oracle Java JDK

#### 1、前往oracle Java官网下载JDK
（http://www.oracle.com/technetwork/java/javase/downloads/index.html）

#### 2、解压缩到指定目录（以jdk-8u191-linux-x64.tar.gz为例）

创建目录:
```
sudo mkdir /usr/local/jdk
```
解压缩到该目录:
```
sudo tar -zxvf jdk-7u60-linux-x64.gz -C /usr/local/jdk
```
#### 3.修改环境变量:　　
```
sudo vi ~/.bashrc
```
在文件末尾追加下面内容：
```
#set oracle jdk environment
export JAVA_HOME=/usr/local/jdk/jdk1.8.0_241  ## 这里要注意目录要换成自己解压的jdk 目录
export JRE_HOME=${JAVA_HOME}/jre  
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib
export PATH=${JAVA_HOME}/bin:$PATH  
```

使环境变量马上生效：
```
source ~/.bashrc
```
#### 4、系统注册此jdk
```
sudo update-alternatives --install /usr/bin/java java /usr/local/jasmine/jdk/jdk1.8.0_241/bin/java 300
```
#### 5、查看java版本，看看是否安装成功：
```
java -version
```