package com.jasmine.learingsb.config.autoconfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;

import javax.annotation.PostConstruct;

/**
 * @author : jasmineXz
 */
@Slf4j
@AutoConfigureOrder(1)
public class AutoOrderConfig1 {
    @PostConstruct
    public void print () {
        log.trace("==========> {} 生效",this.getClass().getSimpleName());
    }
}
