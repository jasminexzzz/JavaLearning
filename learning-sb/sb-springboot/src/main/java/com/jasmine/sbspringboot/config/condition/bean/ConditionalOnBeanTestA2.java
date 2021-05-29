package com.jasmine.sbspringboot.config.condition.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author : jasmineXz
 */
@Slf4j
@Component
@ConditionalOnBean(name = "conditionalOnBeanTestA1")
public class ConditionalOnBeanTestA2 {

    @PostConstruct
    public void print() {
        log.trace("==========> {} 生效", this.getClass().getSimpleName());
    }
}
