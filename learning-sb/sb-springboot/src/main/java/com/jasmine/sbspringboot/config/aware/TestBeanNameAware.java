package com.jasmine.sbspringboot.config.aware;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author : jasmineXz
 */
@Slf4j
@Configuration
public class TestBeanNameAware implements BeanNameAware {

    private String beanName;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @PostConstruct
    public void print() {
        log.trace("==========> {} 生效, {}",
                this.getClass().getSimpleName(),
                this.beanName);
    }
}
