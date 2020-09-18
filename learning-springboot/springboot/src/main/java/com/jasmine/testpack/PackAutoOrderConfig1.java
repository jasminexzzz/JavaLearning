package com.jasmine.testpack;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author : jasmineXz
 */
@Slf4j
@Component
@Configuration
public class PackAutoOrderConfig1 {

    @PostConstruct
    public void print () {
        log.trace("==========> {} 生效",this.getClass().getSimpleName());
    }
}
