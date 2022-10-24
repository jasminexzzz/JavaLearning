package com.xzzz.sbspringboot.config.condition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author : jasmineXz
 */
@Slf4j
@Configuration
@ConditionalOnResource(resources = "/logback-spring.xml")
public class TestConditionalOnResource {

    @PostConstruct
    public void print() {
        log.trace("==========> {} 生效", this.getClass().getSimpleName());
    }

}
