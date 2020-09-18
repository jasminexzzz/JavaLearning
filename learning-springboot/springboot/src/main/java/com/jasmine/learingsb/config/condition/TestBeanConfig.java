package com.jasmine.learingsb.config.condition;

import com.jasmine.learingsb.config.condition.model.TestBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;

/**
 * @author : jasmineXz
 */
@Slf4j
public class TestBeanConfig {

    @Bean
    public TestBean testBean () {
        return new TestBean();
    }

    public TestBean testBeanOnMissing () {
        return new TestBean();
    }

    @PostConstruct
    public void print () {
        log.trace("==========> TestBeanConfig 生效");
    }

}
