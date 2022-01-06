package com.jasmine.es.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author wangyf
 * @since 1.2.2
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "estool")
public class EsProperties implements EnvironmentAware {

    /**
     * 环境变量
     */
    private Environment env;

    /**
     * single  : 单节点
     * cluster : 集群
     */
    private String discoveryType = "single";

    /**
     * 连接方式: http
     */
    private String scheme = "http";

    /**
     * 请求获取数据的超时时间(即响应时间)，单位毫秒。即在与目标url建立了连接之后，等待返回数据的时间的最大时间，
     * 如果超出时间没有响应，就会抛出socketTimeoutException异常。
     */
    private Integer socketTimeout = 30000;

    /**
     * 设置连接超时时间，单位毫秒。指的是连接目标url的连接超时时间，即客服端发送请求到与目标url建立起连接的最大时间。
     * 如果在该时间范围内还没有建立起连接，会抛出connectionTimeOut异常。
     */
    private Integer connectTimeout = 3000;

    /**
     * 设置从connect Manager(连接池)获取Connection 超时时间，单位毫秒。HttpClient中的要用连接时尝试从连接池中获取，
     * 若是在等待了一定的时间后还没有获取到可用连接（比如连接池中没有空闲连接了）则会抛出获取连接超时异常。
     */
    private Integer connectRequestTimeout = 3000;

    private Single single;

    private Cluster cluster;

    private String esToolDesc;


    /** 单节点模式 */
    @Data
    public static class Single {
        private String node;
    }


    /** 集群模式 */
    @Data
    public static class Cluster {
        private String[] nodes;
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }
}
