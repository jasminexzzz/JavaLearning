package com.xzzz.sbspringbootmodule.autoconfig.scan;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;

/**
 * @author : jasmineXz
 */
@Slf4j
@ComponentScan
public class ComponentScanTest {

    @PostConstruct
    public void print () {
        log.trace("[MODULE] ==========> {} 生效",this.getClass().getSimpleName());
    }

}
