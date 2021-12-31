package com.alibaba.csp.sentinel.dashboard.rule.redis;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 发布熔断规则
 *
 * @author wangyf
 * @since 2.0.1
 */
@Component("flowRuleRedisPublisher")
public class FlowRuleRedisPublisher implements DynamicRulePublisher<List<FlowRuleEntity>> {

    private static final String FLOW_RULE_KEY = "sentinel:flow_rule_key";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void publish(String app, List<FlowRuleEntity> rules) throws Exception {
        stringRedisTemplate.opsForValue().set(FLOW_RULE_KEY, JsonUtil.toJson(rules));
    }
}
