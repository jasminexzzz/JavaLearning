package com.alibaba.csp.sentinel.dashboard.rule.redis;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 发布熔断规则
 *
 * @author wangyf
 * @since 2.0.1
 */
@Component("degradeRuleRedisPublisher")
public class DegradeRuleRedisPublisher implements DynamicRulePublisher<List<DegradeRuleEntity>> {

    @Override
    public void publish(String app, List<DegradeRuleEntity> rules) throws Exception {

    }
}
