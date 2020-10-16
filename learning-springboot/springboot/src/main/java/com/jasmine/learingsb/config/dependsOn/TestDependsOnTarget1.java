package com.jasmine.learingsb.config.dependsOn;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

/**
 * @author : jasmineXz
 */
@Slf4j
public class TestDependsOnTarget1 {

    @PostConstruct
    public void print () {
        log.trace("==========> {} 生效", this.getClass().getSimpleName());
    }

}
