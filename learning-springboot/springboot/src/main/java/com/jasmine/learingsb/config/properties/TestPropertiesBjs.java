package com.jasmine.learingsb.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author : jasmineXz
 */
@Configuration
@ConfigurationProperties(prefix = "bjs")
public class TestPropertiesBjs {
    private Key key;
    private Value value;

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    @PostConstruct
    public void init () {
    }
}
