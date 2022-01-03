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

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void publish(String app, List<FlowRuleEntity> rules) throws Exception {
        String ruleStr = JsonUtil.toJson(rules);
        // 持久化到redis
        stringRedisTemplate.opsForValue().set(RedisRuleConstant.FLOW_RULE_KEY + app, ruleStr);
        // 发布内容
        stringRedisTemplate.convertAndSend(RedisRuleConstant.FLOW_CHANNEL + app, ruleStr);
    }
}
