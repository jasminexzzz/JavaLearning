package com.jasmine.sbspringboot.config.aware;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.PostConstruct;

/**
 * @author : jasmineXz
 */
@Slf4j
@Configuration
public class TestResourceLoaderAware implements ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void print() {
        log.trace("==========> {} 生效, {}",
                this.getClass().getSimpleName(),
                this.resourceLoader);
    }
}
