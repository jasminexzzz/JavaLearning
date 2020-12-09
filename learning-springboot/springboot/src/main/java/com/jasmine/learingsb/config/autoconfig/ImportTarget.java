package com.jasmine.learingsb.config.autoconfig;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

/**
 * @author : jasmineXz
 */
@Slf4j
public class ImportTarget {

    @PostConstruct
    public void init () {
        log.trace("==========> ImportTarget 生效");
    }
}
