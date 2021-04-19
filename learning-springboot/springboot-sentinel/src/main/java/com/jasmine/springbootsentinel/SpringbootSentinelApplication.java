package com.jasmine.springbootsentinel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SpringbootSentinelApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootSentinelApplication.class, args);
        log.warn("========== 启动完成 ==========");
    }

}
