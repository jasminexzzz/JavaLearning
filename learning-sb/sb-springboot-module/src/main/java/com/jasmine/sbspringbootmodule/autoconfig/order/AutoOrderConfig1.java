package com.jasmine.sbspringbootmodule.autoconfig.order;

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
        log.trace("[MODULE] ==========> {} 生效",this.getClass().getSimpleName());
    }
}
