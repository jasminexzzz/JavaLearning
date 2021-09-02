
# grok 模板

### 样例1

**样例数据**

```
2021-08-27 16:57:27.426 DEBUG[0.203.12:20881-thread-198] [T:005C6DEE68E3441F9D9380F2B4C98090] sercenter.biz.auth.AuthUserDetailService 346 : [授权查询] 当前登录用户的平台信息如下 登录平台 [PLATFORM], 登录方式 [PASSWORD]
```

**Grok模式**
```grok
%{DATESTAMP:datetime}\s*%{LOGLEVEL:level}\s*\[(?<thread>([\S+]*\s*))\]\s*\[(?<tid>([\S+]*\s*))\]\s*(?<class>([\S+]*\s*\d*))\s*\:(?<msg>([\S+\s*]*))
```