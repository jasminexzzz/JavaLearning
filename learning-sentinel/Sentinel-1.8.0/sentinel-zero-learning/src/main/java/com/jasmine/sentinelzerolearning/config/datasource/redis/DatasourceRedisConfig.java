package com.jasmine.sentinelzerolearning.config.datasource.redis;

import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.redis.RedisDataSource;
import com.alibaba.csp.sentinel.datasource.redis.config.RedisConnectionConfig;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author : jasmineXz
 */
@Component
public class DatasourceRedisConfig {

    private static final String FLOW_RULE_KEY = "sentinel:rule:flow";
    private static final String FLOW_RULE_CHANNEL = "sentinel_channel_flow";

    @PostConstruct
    public void init() {
        RedisConnectionConfig redisConnectionConfig = buildRedisConnectionConfig();

        ReadableDataSource<String, List<FlowRule>> redisDataSource = new RedisDataSource<List<FlowRule>>(
                redisConnectionConfig,
                FLOW_RULE_KEY,
                FLOW_RULE_CHANNEL,
                new ConverterTest());

        FlowRuleManager.register2Property(redisDataSource.getProperty());
    }

    private static RedisConnectionConfig buildRedisConnectionConfig () {
        return RedisConnectionConfig.builder()
            .withHost("localhost")
            .withPort(6379)
            .build();
    }

    public static class ConverterTest implements Converter {

        @Override
        public Object convert(Object source) {
            return source;
        }
    }
}
