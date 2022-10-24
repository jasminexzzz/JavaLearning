package com.xzzz.sbspringboot.config.condition.property;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author : jasmineXz
 */
@Slf4j
@Component
@ConditionalOnProperty(
//     value = "bjs.value.name"
        name = "bjs.value.name",
        havingValue = "jasmine"
)
public class ConditionalOnPropertiesTest {
    @PostConstruct
    public void print() {
        log.trace("==========> {} 生效", this.getClass().getSimpleName());
    }
}
