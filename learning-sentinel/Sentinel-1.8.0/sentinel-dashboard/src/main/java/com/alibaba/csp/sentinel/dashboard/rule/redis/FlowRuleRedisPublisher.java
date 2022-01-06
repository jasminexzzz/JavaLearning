package com.alibaba.csp.sentinel.dashboard.rule.redis;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
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

    private final Logger logger = LoggerFactory.getLogger(FlowControllerV1.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void publish(String app, List<FlowRuleEntity> rules) throws Exception {
        String ruleStr = JsonUtil.toJson(rules);

        logger.warn("[Learning] [发布]流控规则: {}", ruleStr);

        // 以事务的方式发送消息
        SessionCallback<List<?>> sessionCallback = new SessionCallback<List<?>>() {
            @Override
            public List<?> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                // 持久化到redis
                operations.opsForValue().set(RedisRuleConstant.FLOW_RULE_KEY + app, ruleStr);
                // 发布内容
                operations.convertAndSend(RedisRuleConstant.FLOW_CHANNEL + app, ruleStr);
                return operations.exec();
            }
        };
        stringRedisTemplate.execute(sessionCallback);

    }
}
