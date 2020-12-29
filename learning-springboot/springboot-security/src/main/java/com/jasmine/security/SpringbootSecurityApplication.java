package com.jasmine.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@SpringBootApplication
public class SpringbootSecurityApplication {
    private static final Logger log = LoggerFactory.getLogger(SpringbootSecurityApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringbootSecurityApplication.class, args);
        log.warn("========== 启动完成 ==========");
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

}
