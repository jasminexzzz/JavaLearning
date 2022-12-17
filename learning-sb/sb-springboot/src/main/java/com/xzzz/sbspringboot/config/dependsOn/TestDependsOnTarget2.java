package com.xzzz.sbspringboot.config.dependsOn;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

/**
 * @author : jasmineXz
 */
@Slf4j
public class TestDependsOnTarget2 {

    @PostConstruct
    public void print() {
        log.trace("==========> {} 生效", this.getClass().getSimpleName());
    }

}
