package com.xzzz.sbspringbootmodule.autoconfig.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;

import javax.annotation.PostConstruct;

/**
 * @author : jasmineXz
 */
@Slf4j
@AutoConfigureOrder(2)
public class AutoOrderConfig2 {

    @PostConstruct
    public void print () {
        log.trace("[MODULE] ==========> {} 生效",this.getClass().getSimpleName());
    }
}
