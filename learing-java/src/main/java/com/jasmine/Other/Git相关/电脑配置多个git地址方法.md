- 原文地址:(https://mp.csdn.net/mdeditor/102783210#)
### 1. 生成不同环境对应秘钥 
#### 1. 生成gitlab对应的私钥公钥
执行命令创建公司git环境对应的sshkey
```linux
ssh-keygen -t rsa -C 49154XXXX@qq.com
```
#### 2. 生成gitee对应的私钥公钥
执行命令创建gitee对应的sshkey
```linux
ssh-keygen -t rsa -C 49154XXXX@qq.com
```

- 执行命令后会显示如下信息
```linux
Generating public/private rsa key pair.
# 输入生成的秘钥文件名
Enter file in which to save the key (/c/Users/hz/.ssh/id_rsa): id_rsa_gitlab_from_company
# 输入使用此秘钥的密码,本文使用:abc123
Enter passphrase (empty for no passphrase):
# 再次输入使用此秘钥的密码
Enter same passphrase again:
Your identification has been saved in id_rsa_gitlab_from_company.
Your public key has been saved in id_rsa_gitlab_from_company.pub.
The key fingerprint is:
# 这里的最好不不要泄露给其他人
SHA256:XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX 49154XXXX@qq.com
The key's randomart image is:
+---[RSA 2048]----+
|..o+..=X=+.      |
|..o.oo+B*o.o     |
|.. oo ++*.oo     |
|.  + . . .o      |
|  = E . S        |
| B * . .         |
|* + + .          |
|o. . .           |
|                 |
+----[SHA256]-----+
```
![在这里插入图片描述](https://img-blog.csdnimg.cn/20191028160125102.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9qYXNtaW5leHouYmxvZy5jc2RuLm5ldA==,size_16,color_FFFFFF,t_70)
- git bash执行命令时路径是哪里,生成的文件就在哪里
- git并不知道你生成的秘钥对应哪个网站,后面需要自己配置

### 2. 文件如何使用
把得到的文件拷贝到git默认访问的.ssh目录
- 默认在C盘用户文件下的.ssh文件中

![在这里插入图片描述](https://img-blog.csdnimg.cn/2019102815482832.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9qYXNtaW5leHouYmxvZy5jc2RuLm5ldA==,size_16,color_FFFFFF,t_70)
- 除了秘钥文件之外,config文件是后面的步骤中手动生产的,known_hosts文件是后续自动生产的

###  3.在码云或gitlab上添加自己的秘钥
- 码云

![在这里插入图片描述](https://img-blog.csdnimg.cn/20191028155634497.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9qYXNtaW5leHouYmxvZy5jc2RuLm5ldA==,size_16,color_FFFFFF,t_70)
- gitlab

![在这里插入图片描述](https://img-blog.csdnimg.cn/20191028155703975.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9qYXNtaW5leHouYmxvZy5jc2RuLm5ldA==,size_16,color_FFFFFF,t_70)
### 4.修改配置文件
在.ssh目录创建config文件
直接右键创建创建文本文件,然后删除文件后缀即可

![在这里插入图片描述](https://img-blog.csdnimg.cn/20191028155752808.png)
- config文件配置如下：
```
# 配置公司gitlab.com
Host 192.168.1.25       
HostName 192.168.1.25
IdentityFile C:\\Users\hz\\.ssh\\id_rsa_gitlab_from_company
PreferredAuthentications publickey
User wangxx

# 配置自己码云
Host gitee.com
HostName gitee.com
IdentityFile C:\\Users\hz\\.ssh\\id_rsa_gitee_from_company
PreferredAuthentications publickey
User jasxxxxxx
```

|字段|解释|
|:--------|:----|
|Host|这个是请求名称|
|HostName|这个是真实的域名地址,github就是github的地址,内网就是内网的ip|
|IdentityFile|这里是id_rsa的路径|
|PreferredAuthentications|配置登录时用什么权限认证–可设为publickey,password publickey,keyboard-interactive等|
|User|配置使用用户名|

==host会影响你的使用,比如平时使用SSH方式clone代码==

 ```linux
 git clone git@github.com:spring-projects/spring-boot.git
 ```
 但如果你将host修改为abcd,那么访问时则需要
 ```linux
 git clone git@abcd:spring-projects/spring-boot
 ```
<kbd>==**所以最好host和hostname相同**==</kbd>
 
### 5.测试
执行命令
```linux
# gitee.com为你的config文件中的host字段名
ssh -T git@gitee.com
```
然后会让你输入密码:
```linux
# 输入前面配置的密码即可,本文使用:abc123
Enter passphrase for key 'C:\\Users\hz\\.ssh\\id_rsa_gitee_from_company':
```
访问成功如下:
```linux
Hi [你的账户名称]! You've successfully authenticated, but GITEE.COM does not provide shell access.
```
完整如下:

![在这里插入图片描述](https://img-blog.csdnimg.cn/20191028161505712.png)
到此就配置成功,git会根据你访问的地址来使用不同的秘钥进行操作.无需你手动切换用户等.