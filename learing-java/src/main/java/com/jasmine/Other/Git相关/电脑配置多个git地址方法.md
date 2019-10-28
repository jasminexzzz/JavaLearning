- 原文地址 : https://blog.csdn.net/EllieYaYa/article/details/86488695

#### 1. 生成github.com对应的私钥公钥
（本文中文件地址C:\Users\popfisher目录）
执行命令 ssh-keygen -t rsa -C email 创建github对应的sshkey，命名为id_rsa_github，密码 jas*******
```
ssh-keygen -t rsa -C 49154XXXX@qq.com
```

#### 2. 同样的方式生产git.oschina.net的私钥公钥
（邮箱地址可以相同可以不同，本文相同）
执行命令ssh-keygen -t rsa -C email创建github对应的sshkey，命名为id_rsa_oschina，密码 123456
```linux
ssh-keygen -t rsa -C 774232122@qq.com
```


#### 3. 文件说明和使用
转载者注：俺自己配置的是github和公司内网的gitlab，方法是一样一样的哈

把上面得到的文件拷贝到git默认访问的.ssh目录(win10在用户目录下，本文C:\Users\popfisher.ssh)
除了秘钥文件之外，config文件是后面的步骤中手动生产的，known_hosts文件是后续自动生产的


把github对应的公钥和oschina对应的公钥上传到服务器
GitHub添加SSH key的方式如下图所示：


git.oschina.net添加SSH key的方式如下图所示：


#### 4. 修改配置文件
在.ssh目录创建config文本文件并完成相关配置(最核心的地方)
每个账号单独配置一个Host，每个Host要取一个别名，每个Host主要配置HostName和IdentityFile两个属性即可

Host的名字可以取为自己喜欢的名字，不过这个会影响git相关命令，例如：
Host mygithub 这样定义的话，命令如下，即git@后面紧跟的名字改为mygithub
git clone git@mygithub:PopFisher/AndroidRotateAnim.git

|字段|解释|
|-----|-----|
|HostName |这个是真实的域名地址|
|IdentityFile 　　　　　　　 这里是id_rsa的地址|
|PreferredAuthentications| 配置登录时用什么权限认证–可设为publickey,password publickey,keyboard-interactive等|
|User 　　　　　　　　　　　|配置使用用户名|

config文件配置如下：

```
# 配置公司gitlab.com
Host 192.168.1.25       
HostName 192.168.1.25
IdentityFile C:\\Users\hz\\.ssh\\id_rsa_gitlab_from_company
PreferredAuthentications publickey
User wangyf

# 配置自己码云
Host gitee.com
HostName gitee.com
IdentityFile C:\\Users\hz\\.ssh\\id_rsa_gitee_from_company
PreferredAuthentications publickey
User jasminexz
```

#### 5、测试
打开Git Bash客户端（管理员身份运行）执行测试命令测试是否配置成功（会自动在.ssh目录生成known_hosts文件把私钥配置进去）


测试成功之后就可以在电脑上同时使用git多多账号同时操作，互不影响了
clone github上的项目AndroidRotateAnim
　　打开github上AndroidRotateAnim项目，复制其对应的clone命令如下图所示
在这里插入图片描述


建库的操作部分我就不粘了哈，自行搜索。


再次感谢原作者详细的截图。

