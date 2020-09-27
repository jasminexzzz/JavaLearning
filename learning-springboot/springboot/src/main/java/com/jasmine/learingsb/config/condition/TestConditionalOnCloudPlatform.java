package com.jasmine.learingsb.config.condition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author : jasmineXz
 */
@Slf4j
@Component
@ConditionalOnCloudPlatform(CloudPlatform.CLOUD_FOUNDRY)
public class TestConditionalOnCloudPlatform {
    @PostConstruct
    public void print () {
        log.trace("==========> {} 生效", this.getClass().getSimpleName());
    }
}
