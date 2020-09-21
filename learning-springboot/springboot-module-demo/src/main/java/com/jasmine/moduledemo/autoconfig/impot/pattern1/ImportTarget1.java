package com.jasmine.moduledemo.autoconfig.impot.pattern1;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

/**
 * @author : jasmineXz
 */
@Slf4j
class ImportTarget1 {

    @PostConstruct
    public void print () {
        log.trace("[MODULE] ==========> {} 生效",this.getClass().getSimpleName());
    }
}