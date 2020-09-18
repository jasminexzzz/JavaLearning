package com.jasmine.testpack;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;

import javax.annotation.PostConstruct;

/**
 * @author : jasmineXz
 */
@Slf4j
@AutoConfigureOrder(2)
public class PackAutoOrderConfig2 {

    @PostConstruct
    public void print () {
        log.trace("==========> {} 生效",this.getClass().getSimpleName());
    }
}
