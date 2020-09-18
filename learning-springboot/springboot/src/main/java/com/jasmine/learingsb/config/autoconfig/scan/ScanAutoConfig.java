package com.jasmine.learingsb.config.autoconfig.scan;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author : jasmineXz
 */
@Slf4j
@Configuration
public class ScanAutoConfig {
    @PostConstruct
    public void print () {
        log.trace("==========> {} 生效",this.getClass().getSimpleName());
    }
}
