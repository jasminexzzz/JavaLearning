package com.jasmine.es.client.config;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;


/**
 * ElasticSearch 配置类
 * @author wangyf
 * @since 0.0.1
 */
@Slf4j
@Configuration
public class EsConfig {

    @Autowired
    private EsProperties esProperties;

    @Bean
    public RestClientBuilder restClientBuilder() {
        RestClientBuilder restClientBuilder;
        if (DiscoverEnum.SINGLE.getType().equals(esProperties.getDiscoveryType()) && esProperties.getSingle() != null) {
            restClientBuilder = RestClient.builder(buildHttpHost(esProperties.getSingle().getNode()));
        } else if (DiscoverEnum.CLUSTER.getType().equals(esProperties.getDiscoveryType()) && esProperties.getCluster() != null) {
            restClientBuilder = RestClient.builder(buildHttpHost(esProperties.getCluster().getNodes()));
        } else {
            throw new IllegalArgumentException("错误的配置类型:" + esProperties.getDiscoveryType());
        }

        setFailureListener(restClientBuilder);
        setRequestConfigCallback(restClientBuilder);
        return restClientBuilder;
    }

    @Bean
    public RestHighLevelClient restHighLevelClient(@Autowired RestClientBuilder restClientBuilder){
        return new RestHighLevelClient(restClientBuilder);
    }



    /*
     * 设置一个监听器，每次节点出现故障时都会收到通知，以防需要采取措施，
     */
    private void setFailureListener (RestClientBuilder restClientBuilder) {
        restClientBuilder.setFailureListener(new RestClient.FailureListener() {
            @Override
            public void onFailure(Node node) {
                log.error("[ES] 连接ES节点失败: {}",node.toString());
            }
        });
    }


    /*
     * 设置允许修改默认请求配置的回调
     */
    private void setRequestConfigCallback (RestClientBuilder restClientBuilder) {
        restClientBuilder.setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder
                .setConnectionRequestTimeout(esProperties.getConnectRequestTimeout())
                .setSocketTimeout(esProperties.getSocketTimeout())
                .setConnectTimeout(esProperties.getConnectTimeout())
        );
    }


    /**
     * 创建 HttpHost
     * @param str IP端口字符串
     */
    private HttpHost buildHttpHost(String str) {
        String[] ipAndPort;
        if (StrUtil.isBlank(str) || (ipAndPort = str.split(":")).length != 2) {
            log.error("[ES] ES地址配置错误: {}", str);
            throw new IllegalArgumentException("ES地址配置错误: " + str);
        }
        return new HttpHost(ipAndPort[0], Integer.parseInt(ipAndPort[1]), esProperties.getScheme());
    }

    /**
     * HttpHost 数组
     * @param strs IP端口字符串数组
     */
    private HttpHost[] buildHttpHost (String[] strs) {
        if (strs.length == 0) {
            log.error("[ES] ES地址配置错误: {}", Arrays.toString(strs));
            throw new IllegalArgumentException("ES地址配置错误: " + Arrays.toString(strs));
        }

        HttpHost[] hosts = new HttpHost[strs.length];
        for (int i = 0; i < strs.length; i++) {
            hosts[i] = buildHttpHost(strs[i]);
        }
        return hosts;
    }

}