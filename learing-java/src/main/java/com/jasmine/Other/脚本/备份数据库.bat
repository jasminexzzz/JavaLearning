@echo off

REM 声明采用UTF-8编码
chcp 65001

echo.
echo MySQL数据库备份脚本
echo.
echo *****************************
echo.
echo 备份日期：%date%
echo 备份时间：%time%
echo.
echo *****************************
echo.
echo 开始备份...
echo.
::set nowtime=%date:~3,4%年%date:~8,2%月%date:~11,2%日%time:~0,2%时%time:~3,2%分
set "nowtime=%date:~3,4%%date:~8,2%%date:~11,2% %time:~0,2%%time:~3,2%"
set "nowtime=%nowtime: =0%"
::数据库名称
set dbname=elevator_maintain
::数据库的账号
set user=zxkj
::数据库密码
set password=zxkj#123
::数据库地址
set host=192.168.1.23
::数据库端口
set port_num=3306

:: mysqldump -h主机IP -u数据库用户名 -p数据库密码 数据库名称 [要导出的表] | gzip > /导出目录/test.sql.gz

::压缩备份，压缩备份需要下载gzip.exe
::mysqldump --default-character-set=utf8mb4 -P%port_num% -h%host% -u%user% -p%password% %dbname% | gzip > %dbname%_%nowtime%.sql.gz 

::mysqldump --default-character-set=utf8mb4 -P%port_num% -h%host% -u%user% -p%password% %dbname% | gzip > %dbname%_%nowtime%.sql.gz 

::mysqldump --default-character-set=utf8 -P%port_num% -h%host% -u%user% -p%password% %dbname% | gzip > %dbname%_%nowtime%.sql.gz 

:: mysqldump --default-character-set=gbk -h127.0.0.1 -uroot -p168168 test | gzip > test.sql.gz 

::不压缩备份
:: mysqldump -h127.0.0.1 -uroot -p168168 test > test.sql
:: mysqldump -h127.0.0.1 -uroot -p168168 test  | gzip > test.sql.gz
 mysqldump --default-character-set=utf8mb4 -P%port_num% -h%host% -u%user% -p%password% %dbname%  > %dbname%_%nowtime%.sql

echo.
echo MySQL数据库备份完成
echo.

@echo on
@pause