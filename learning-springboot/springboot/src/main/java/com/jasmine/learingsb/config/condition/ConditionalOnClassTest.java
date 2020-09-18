package com.jasmine.learingsb.config.condition;

import com.jasmine.learingsb.config.condition.model.TestBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author : jasmineXz
 */
@Slf4j
@Configuration
@ConditionalOnClass(TestBean.class)
public class ConditionalOnClassTest {

    @PostConstruct
    public void print () {
        log.trace("==========> ConditionalOnClassTest 生效");
    }
}
