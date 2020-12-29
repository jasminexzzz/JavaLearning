package com.jasmine.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("@rolePre.check('ROLE_HELLO')")
    @GetMapping("/helloWithHello")
    public String helloWithHello() {
        return "Hello With ROLE_Hello";
    }

    @GetMapping("/helloWithoutRole")
    public String helloWithoutRole() {
        return "Hello Without ROLE_Hello";
    }

}
