package com.xzzz.sbspringboot.config.autowire;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author : jasmineXz
 */
@Slf4j
@Component
class AutowireBeanFactoryTarget1 implements BeanNameAware {

    private String beanName;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @PostConstruct
    public void print() {
        log.trace("==========> [A] {} 已注入IOC", this);
    }

}
