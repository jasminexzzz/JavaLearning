package com.jasmine.learingsb.config.condition.expression;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 字符串 : @ConditionalOnExpression("'${bjs.value.name}'.equals('jasmine')")
 * 数字　 : @ConditionalOnExpression("${bjs.value.id} == 1")
 * 布尔　 : @ConditionalOnExpression("${bjs.value.enabled:true}")
 * 多个校验: @ConditionalOnExpression("'${bjs.value.name}'.equals('jasmine') && ${bjs.value.id} == 2") // 换成 || 也可以
 *
 * @author : jasmineXz
 */
@Slf4j
@Configuration
@ConditionalOnExpression("'${bjs.value.name}'.equals('jasmine')")
public class TestConditionalOnExpression {
    @PostConstruct
    public void print () {
        log.trace("==========> {} 生效", this.getClass().getSimpleName());
    }
}
