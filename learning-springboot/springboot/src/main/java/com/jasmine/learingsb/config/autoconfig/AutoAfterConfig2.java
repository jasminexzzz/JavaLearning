package com.jasmine.learingsb.config.autoconfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;

import javax.annotation.PostConstruct;

/**
 * @author : jasmineXz
 */
@Slf4j
@AutoConfigureAfter(AutoAfterConfig1.class)
public class AutoAfterConfig2 {

    @PostConstruct
    public void print () {
        log.trace("==========> {} 生效",this.getClass().getSimpleName());
    }
}
