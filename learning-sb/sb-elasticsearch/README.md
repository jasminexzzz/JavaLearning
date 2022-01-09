# 启动本地ES

```shell
./bin/elasticsearch
```

# docker 安装

## 拉取镜像

```shell script
docker pull elasticsearch:7.10.1
```

## 修改配置文件

## 启动

启动命令

```shell script
docker run --name elasticsearch -p 9200:9200 -p 9300:9300 \
-e "discovery.type=single-node" \
-e ES_JAVA_OPTS="-Xms64m -Xmx128m" \
-v /Users/jasmine/IdeaProjects/middleware/docker/es/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
-v /Users/jasmine/IdeaProjects/middleware/docker/es/data:/usr/share/elasticsearch/data \
-v /Users/jasmine/IdeaProjects/middleware/docker/es/plugins:/usr/share/elasticsearch/plugins \
-d 558380375f1a
```

注意映射目录和JVM调整

# docker 安装 kibana

## 拉取镜像

```shell script
docker pull kibana:7.10.1
```

## 查看镜像中的es地址

### 查看镜像信息

```shell script
docker inspect elasticsearch
```

### 查看ip

> 查看最底部的 IPAddress 字段

```shell script
NetworkSettings:{
  ...
  Networks: {
    "bridge": {
      "IPAMConfig": null,
      "Links": null,
      "Aliases": null,
      "NetworkID": "dde7d25ec1df50f2f81475e71fbb595800214548b03e34d8c3a4bedf68e8c177",
      "EndpointID": "0a381a08148221f18d8a3592ccda186361647517c4339e1e5da7b2e07db64732",
      "Gateway": "172.17.0.1",
      "IPAddress": "172.17.0.4",
      "IPPrefixLen": 16,
      "IPv6Gateway": "",
      "GlobalIPv6Address": "",
      "GlobalIPv6PrefixLen": 0,
      "MacAddress": "02:42:ac:11:00:04",
      "DriverOpts": null
      }
  }
}
```

## 修改配置文件

将映射的配置文件的 es 地址修改为上述IP

```yaml
# 端口
server.port: 5601
# 暴露IP
server.host: "0.0.0.0"
# ES 地址
elasticsearch.hosts: ["http://172.17.0.4:9200"]
# 语言
i18n.locale: "zh-CN"
```

## 启动

```shell script
docker run --name kibana -p 5601:5601 \
-v /Users/jasmine/IdeaProjects/middleware/docker/kibana/config/kibana.yml:/usr/share/kibana/config/kibana.yml \
-d 3e014820ee3f
```

```shell script
docker run --name kibana -p 9200:9200 -p 9300:9300 \
-e ES_JAVA_OPTS="-Xms64m -Xmx128m" \
-v /Users/jasmine/IdeaProjects/middleware/docker/kibana/config/kibana.yml:/usr/share/kibana/config/kibana.yml \
-d 558380375f1a
```

# Grok 格式化脚本

## Qiaoqiao 日志 GROK 格式化

```java
# 日志原文
2022-01-07 19:00:01.216 WARN  [Thread-133] [T:D0E7B7A9BE3A4472BD48BE30EBA55BFF] com.qiaoqiao.usercenter.biz.wx.common.WxTask.refreshTppAuthorizerTokens 86   : [MP > :8899] 该小程序不存在 refresh_token, 无法刷新或获取小程序的 token 信息

# Grok 格式化脚本
%{TIMESTAMP_ISO8601:datetime}\s*%{LOGLEVEL:level}\s*\[(?<thread>([\S+]*\s*))\]\s*\[(?<tid>([\S+]*\s*))\]\s*(?<class>([\S+]*\s*\d*))\s*\:(?<msg>([\S+\s*]*))
# 另一个更易读的脚本
%{TIMESTAMP_ISO8601:datetime}\s%{LOGLEVEL:level}\s*\[%{DATA:thread}\]\s*\[%{DATA:traceid}\]\s*%{DATA:class}\:(?<msg>([\S+\s*]*))
```

## Sentinel mertic 日志格式化

```java
# 日志原文
1641655527000|2022-01-08 23:25:27|resource_fastfail|16|4|16|0|0|0|0|0
1641553972000|2022-01-07 19:12:52|com.qiaoqiao.usercenter.api.user.service.UserDubboService:getUserByIds(java.util.List,java.lang.String)|2|0|2|0|3|0|0|2

# Grok 格式化脚本
%{TIMESTAMP_ISO8601:datetime}\|%{DATA:resource}\|%{INT:pass}\|%{INT:block}\|%{INT:success}\|%{INT:exception}\|%{INT:rt}\|%{INT:occupied}\|%{INT:concurrency}\|%{INT:cf}
```
