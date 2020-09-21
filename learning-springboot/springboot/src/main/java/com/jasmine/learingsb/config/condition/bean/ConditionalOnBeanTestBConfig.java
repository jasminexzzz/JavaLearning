package com.jasmine.learingsb.config.condition.bean;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : jasmineXz
 */
@Configuration
public class ConditionalOnBeanTestBConfig {

    @Bean
    public ConditionalOnBeanTestB1 conditionalOnBeanTestB1 () {
        return new ConditionalOnBeanTestB1();
    }

    @Bean
    @ConditionalOnBean(name = "conditionalOnBeanTestB1")
    public ConditionalOnBeanTestB2 conditionalOnBeanTestB2 () {
        return new ConditionalOnBeanTestB2();
    }
}
