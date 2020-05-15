:: 批量将某种文件重命名
:: 注意修改下方需要修改的文件后缀 *.png ,和命名后的文件名后缀 !n!.png
@echo off
setlocal enabledelayedexpansion


::
for /f "delims=" %%i in ('dir /s/b *.*') do (
	set "foo=%%~nxi"    
	set foo=!foo: =!    
	set foo=!foo: =!    
	ren "%%~fi" "!foo!"
)


:: 无参：遍历当前路径的文件夹下的文件，但也可在(匹配符)中指定路径
:: /d：遍历当前路径的文件夹下的文件夹，但也可在(匹配符)中指定路径
:: /r [路径]：深度遍历指定路径下的所有文件，子目录中的文件也会被遍历到，如果没指定路径，默认当前路径
:: /l ：当使用参数 /l 时，需结合(匹配符)一起使用，此时 () 括号内部的用法规则为：(start, step, end)，此时的 for 命令作用等同于 java 语言中的 for 语句
:: /f ：用于解析文件中的内容，本节不做介绍

set n=1
for /f %%i in ('dir /b *.png') do (
	ren "%%i" bjscloud-logo-vertical-!n!.png
	set /a n+=1
)
echo 批量重命名完成!
pause