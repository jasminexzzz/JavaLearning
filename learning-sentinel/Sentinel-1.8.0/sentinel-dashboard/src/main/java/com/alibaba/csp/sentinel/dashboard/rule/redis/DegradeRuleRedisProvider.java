package com.alibaba.csp.sentinel.dashboard.rule.redis;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 获取熔断规则
 *
 * @author wangyf
 * @since 2.0.1
 */
@Component("degradeRuleRedisProvider")
public class DegradeRuleRedisProvider implements DynamicRuleProvider<List<DegradeRuleEntity>> {

    @Override
    public List<DegradeRuleEntity> getRules(String appName) throws Exception {
        return null;
    }
}
