package com.jasmine.learingsb.config.condition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnJava;
import org.springframework.boot.system.JavaVersion;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author : jasmineXz
 */
@Slf4j
@Component
@ConditionalOnJava(JavaVersion.EIGHT)
public class TestConditionalOnJava {
    @PostConstruct
    public void print () {
        log.trace("==========> {} 生效", this.getClass().getSimpleName());
    }
}
