package com.jasmine.sentinelzerolearning.config.datasource.redis;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.redis.RedisDataSource;
import com.alibaba.csp.sentinel.datasource.redis.config.RedisConnectionConfig;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import java.util.List;

/**
 * @author : jasmineXz
 */
public class DatasourceRedisConfig {

    public static void init () {
        RedisConnectionConfig redisConnectionConfig = buildRedisConnectionConfig();
        ReadableDataSource<String, List<FlowRule>> redisDataSource = new RedisDataSource<List<FlowRule>>(redisConnectionConfig, ruleKey, channel, flowConfigParser);
        FlowRuleManager.register2Property(redisDataSource.getProperty());
    }

    private static RedisConnectionConfig buildRedisConnectionConfig () {
        RedisConnectionConfig config = RedisConnectionConfig.builder()
            .withHost("localhost")
            .withPort(6379)
            .build();
        return config;
    }
}
