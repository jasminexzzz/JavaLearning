在腾讯云上拿到的Ubuntu主机分配给的用户是ubuntu用户，并不是root用户，而阿里云上拿到的Ubuntu主机分配给的用户就是root用户。如果没有root用户权限做事情会变得麻烦，每次做什么都要sudo一下，下面介绍怎么设置root用户的ssh登录：

###1、设置root密码，
可以修改成和ubuntu用户一样，方便记忆。先使用ubuntu用户ssh登录腾讯云，然后执行命令
```linux
sudo passwd root
```
接着输入root密码，屏幕不会像Windows那样出现星号，输完密码敲回车键就可以了，要输入两次密码。
![avatar](https://best-hzjl.oss-cn-hangzhou.aliyuncs.com/oss/img/elevator/fad6d4b7d63049b89e10d7b49fbc1cdd.jpg)


### 2、修改ssh登录的配置
即/etc/ssh/sshd_config文件，修改为允许root登录，可以执行命令
```linux
sudo vim /etc/ssh/sshd_config
```
注意：这里的sudo前缀不可少，否则接下来的修改无法保存。进入vim编辑，用方向键向下滚动找到PermitRootLogin这项，


![avatar](https://best-hzjl.oss-cn-hangzhou.aliyuncs.com/oss/img/elevator/a4d311fb7d324521abb5fc963f78a059.jpg)

按下insert键进入插入模式，将PermitRootLogin后面的prohibit-password改为yes，再按下Esc键，然后依次按下:键(英文冒号键)、w键和q键，最后按下回车键，保存修改成功。

### 3、重启ssh服务使刚才的ssh配置的修改生效，执行命令
```
sudo service ssh restart
```
4、使用root用户测试登录，成功。


root用户登录

作者：纯洁的纯洁
链接：https://www.jianshu.com/p/69d662f804c1
来源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。