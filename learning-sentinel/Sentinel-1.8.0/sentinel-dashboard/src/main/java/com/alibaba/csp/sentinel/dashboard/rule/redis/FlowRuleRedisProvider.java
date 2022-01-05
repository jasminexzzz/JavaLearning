package com.alibaba.csp.sentinel.dashboard.rule.redis;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 读取限流规则
 *
 * @author wangyf
 * @since 2.0.1
 */
@Component("flowRuleRedisProvider")
public class FlowRuleRedisProvider implements DynamicRuleProvider<List<FlowRuleEntity>> {

    private final Logger logger = LoggerFactory.getLogger(FlowControllerV1.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<FlowRuleEntity> getRules(String appName) throws Exception {
        String rules = stringRedisTemplate.opsForValue().get(RedisRuleConstant.FLOW_RULE_KEY + appName);

        logger.warn("[Learning] [读取]流控规则: {}", rules);

        if (StringUtil.isNotBlank(rules)) {
            return JsonUtil.toObject(rules, new TypeReference<List<FlowRuleEntity>>(){});
        }
        return new ArrayList<>();
    }
}
