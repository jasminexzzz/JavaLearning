package com.jasmine.learingsb.config.condition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import javax.annotation.PostConstruct;

/**
 * @author : jasmineXz
 */
@Slf4j
@ConditionalOnBean(name = "testBean")
@AutoConfigureAfter(TestBeanConfig.class)
public class ConditionalOnBeanTest {

    @PostConstruct
    public void print () {
        log.trace("==========> ConditionalOnBeanTest 生效");
    }
}
