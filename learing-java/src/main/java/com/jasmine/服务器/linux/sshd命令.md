systemctl              ##服务控制命令
systemctl	start sshd   ##开启服务
systemctl	stop sshd    ##关闭服务
systemctl restart	sshd	##重启服务
systemctl reload	sshd 	##重新加载服务配置
systemctl enable	sshd 	##设定服务开机启动
systemctl	disable	sshd	##设定服务开机不启动
systemctl	list-units  	##列出已经开启服务当前状态
systemctl list-dependencies	##列出服务的倚赖
