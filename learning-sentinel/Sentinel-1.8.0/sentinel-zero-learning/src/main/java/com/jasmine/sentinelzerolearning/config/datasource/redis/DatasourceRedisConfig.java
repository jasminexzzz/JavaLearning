package com.jasmine.sentinelzerolearning.config.datasource.redis;

import com.alibaba.csp.sentinel.config.SentinelConfig;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.redis.RedisDataSource;
import com.alibaba.csp.sentinel.datasource.redis.config.RedisConnectionConfig;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.jasmine.sentinelzerolearning.util.JsonUtil;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author : jasmineXz
 */
@Component
public class DatasourceRedisConfig {

    private static final String FLOW_RULE_KEY = "sentinel:flow_rule_key";
    private static final String FLOW_CHANNEL = "sentinel_flow_channel";

    @PostConstruct
    public void init() {
        RedisConnectionConfig redisConnectionConfig = buildRedisConnectionConfig();
        ReadableDataSource<String, List<FlowRule>> redisDataSource = new RedisDataSource<List<FlowRule>>(
                redisConnectionConfig,
                FLOW_RULE_KEY + SentinelConfig.getAppName(),
                FLOW_CHANNEL + SentinelConfig.getAppName(),
                new FlowConverter());

        FlowRuleManager.register2Property(redisDataSource.getProperty());
    }

    private static RedisConnectionConfig buildRedisConnectionConfig () {
        return RedisConnectionConfig.builder()
            .withHost("localhost")
            .withPort(6379)
            .build();
    }

    /**
     * 持久化数据的转换器, 如持久化的是Json字符串，这里可以将字符串转换为规则集合
     */
    public static class FlowConverter implements Converter<String,List<FlowRule>> {
        @Override
        public List<FlowRule> convert(String source) {
            System.out.println("source: " + source);
            return JsonUtil.toObject(source, new TypeReference<List<FlowRule>>(){});
        }
    }

}
