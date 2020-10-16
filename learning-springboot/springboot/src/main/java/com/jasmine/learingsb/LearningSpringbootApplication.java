package com.jasmine.learingsb;

import com.jasmine.learingsb.config.aware.EnableTestImportAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


/**
 * @author jasmineXz
 */
@Slf4j
@EnableAsync
@EnableTestImportAware
@SpringBootApplication
public class LearningSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningSpringbootApplication.class, args);
        log.info("========== 启动完成 ==========");
	}
}
