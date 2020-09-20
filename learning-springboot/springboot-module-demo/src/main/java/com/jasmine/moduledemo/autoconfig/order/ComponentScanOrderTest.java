package com.jasmine.moduledemo.autoconfig.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;

/**
 * 此扫描类用于测试扫描与 spring.factories 同时存在时的问题
 *
 * @author : jasmineXz
 */
@Slf4j
@ComponentScan
public class ComponentScanOrderTest {

    @PostConstruct
    public void print () {
        log.trace("[MODULE] ==========> {} 生效",this.getClass().getSimpleName());
    }

}
