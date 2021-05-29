package com.jasmine.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jasmine
 */
@Slf4j
@SpringBootApplication
public class SbSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbSecurityApplication.class, args);
        log.warn("SbSecurityApplication 启动完成");
    }

}
