@echo off

REM ��������UTF-8����
chcp 65001

echo.
echo MySQL���ݿⱸ�ݽű�
echo.
echo *****************************
echo.
echo �������ڣ�%date%
echo ����ʱ�䣺%time%
echo.
echo *****************************
echo.
echo ��ʼ����...
echo.
::set nowtime=%date:~3,4%��%date:~8,2%��%date:~11,2%��%time:~0,2%ʱ%time:~3,2%��
set "nowtime=%date:~3,4%%date:~8,2%%date:~11,2% %time:~0,2%%time:~3,2%"
set "nowtime=%nowtime: =0%"
::���ݿ�����
set dbname=elevator_maintain
::���ݿ���˺�
set user=zxkj
::���ݿ�����
set password=zxkj#123
::���ݿ��ַ
set host=192.168.1.23
::���ݿ�˿�
set port_num=3306

:: mysqldump -h����IP -u���ݿ��û��� -p���ݿ����� ���ݿ����� [Ҫ�����ı�] | gzip > /����Ŀ¼/test.sql.gz

::ѹ�����ݣ�ѹ��������Ҫ����gzip.exe
::mysqldump --default-character-set=utf8mb4 -P%port_num% -h%host% -u%user% -p%password% %dbname% | gzip > %dbname%_%nowtime%.sql.gz 

::mysqldump --default-character-set=utf8mb4 -P%port_num% -h%host% -u%user% -p%password% %dbname% | gzip > %dbname%_%nowtime%.sql.gz 

::mysqldump --default-character-set=utf8 -P%port_num% -h%host% -u%user% -p%password% %dbname% | gzip > %dbname%_%nowtime%.sql.gz 

:: mysqldump --default-character-set=gbk -h127.0.0.1 -uroot -p168168 test | gzip > test.sql.gz 

::��ѹ������
:: mysqldump -h127.0.0.1 -uroot -p168168 test > test.sql
:: mysqldump -h127.0.0.1 -uroot -p168168 test  | gzip > test.sql.gz
 mysqldump --default-character-set=utf8mb4 -P%port_num% -h%host% -u%user% -p%password% %dbname%  > %dbname%_%nowtime%.sql

echo.
echo MySQL���ݿⱸ�����
echo.

@echo on
@pause