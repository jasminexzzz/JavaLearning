package com.xzzz.C2_middleware.Nginx;

/**
 * @author : jasmineXz
 */
public class 避坑 {
    /**
     websocket 连接nginx报错404

     检查nginx是否开启了websocket穿透，如下面代码展示：
     server{
         location / {
             proxy_pass http://entertain.sbc.com/xpe-products-sbc-entertain/;
             proxy_cookie_path  /xpe-products-sbc-entertain/ /;
             proxy_http_version 1.1;
             proxy_set_header Host $proxy_host;
             proxy_set_header Real-Host $host; #保留真实域名
             proxy_set_header Upgrade $http_upgrade;
             proxy_set_header Connection "upgrade";
             proxy_set_header X-Real-IP $remote_addr;
             proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
         }
         设置下面两行
         proxy_set_header Upgrade $http_upgrade;
         proxy_set_header Connection "upgrade";
     }


     重启服务

     */
}
