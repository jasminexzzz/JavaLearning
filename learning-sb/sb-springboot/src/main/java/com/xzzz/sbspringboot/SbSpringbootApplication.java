package com.xzzz.sbspringboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jasmine
 */
@Slf4j
@SpringBootApplication
public class SbSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbSpringbootApplication.class, args);
        log.warn("SbSpringbootApplication 启动完成");
    }

}
