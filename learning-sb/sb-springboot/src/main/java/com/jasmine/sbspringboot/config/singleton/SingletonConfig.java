package com.jasmine.sbspringboot.config.singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jasmineXz
 */
@Configuration
public class SingletonConfig {

    @Bean
    public Singleton singleton() {
        return Singleton.getInstance();
    }
}
