package com.xzzz.C3_servers.docker;

/**
 * @author : jasmineXz
 */
public class 打包报错处理 {
    /**
     * =============================================================================================================================
     *
     * 无法连接到远程API
     * Failed to execute goal com.spotify:docker-maven-plugin:1.0.0:build (default-cli) on project common-registry: Exception caught:
     * java.util.concurrent.ExecutionException: com.spotify.docker.client.shaded.javax.ws.rs.ProcessingException:
     * org.apache.http.conn.HttpHostConnectException: Connect to localhost:2375 [localhost/127.0.0.1, localhost/0:0:0:0:0:0:0:1]
     * failed: Connection refused: connect -> [Help 1]
     *
     * 开启Expose deamon on tcp://localhost:2375 without TLS
     *
     * =============================================================================================================================
     *
     *
     * pull access denied for frolvlad/alpine-oraclejdk8, repository does not exist or may require 'docker login'
     */
}
