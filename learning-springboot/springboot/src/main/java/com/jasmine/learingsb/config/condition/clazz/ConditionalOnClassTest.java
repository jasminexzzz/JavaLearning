package com.jasmine.learingsb.config.condition.clazz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author : jasmineXz
 */
@Slf4j
@Configuration
// @ConditionalOnClass(ConditionalOnClassTargetTest.class)
@ConditionalOnClass(name = "com.jasmine.learingsb.config.condition.clazz.ConditionalOnClassTargetTest")
public class ConditionalOnClassTest {
    @PostConstruct
    public void print () {
        log.trace("==========> {} 生效", this.getClass().getSimpleName());
    }
}
