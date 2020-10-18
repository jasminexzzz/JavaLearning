package com.jasmine.learingsb.config.autowire;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : jasmineXz
 */
@Slf4j
@Configuration
public class Config {

    @Bean
    public TestAbstractAutowireCapableBeanFactory testAbstractAutowireCapableBeanFactory(
            AutowireCapableBeanFactory autowireCapableBeanFactory) {
        return new TestAbstractAutowireCapableBeanFactory(autowireCapableBeanFactory);
    }

    @Autowired
    public void setAutowireBeanFactory(TestAbstractAutowireCapableBeanFactory testAbstractAutowireCapableBeanFactory) {
        testAbstractAutowireCapableBeanFactory.postProcess(new AutowireBeanFactoryTarget());
    }
}
