package com.jasmine.learingsb.config.autowire;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @author : jasmineXz
 */
@Slf4j
class AutowireBeanFactoryTarget implements BeanNameAware {

    private String name;

    @Autowired
    private AutowireBeanFactoryTarget1 autowireBeanFactoryTarget1;

    AutowireBeanFactoryTarget() {
        this.name = "123";
    }

    @Override
    public void setBeanName(String name) {
        this.name = name;
    }

    public AutowireBeanFactoryTarget1 getAutowireBeanFactoryTarget1() {
        return autowireBeanFactoryTarget1;
    }

    @PostConstruct
    public void print () {
        log.trace("==========> [B] {} 已注入IOC", this);
    }
}
