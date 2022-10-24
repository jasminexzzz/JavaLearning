package com.xzzz.feign;

import com.xzzz.common.core.util.spring.SpringEnvUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jasmine
 */
@Slf4j
@SpringBootApplication
public class SbFeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbFeignApplication.class, args);
        log.warn("\n" +
            "=============== 启动完成 =============\n" +
            "说明 : {}\n" +
            "版本 : {}\n" +
            "端口 : {}\n" +
            "=====================================\n",
            SpringEnvUtil.getDesc(),
            SpringEnvUtil.getFrameworkVersion(),
            SpringEnvUtil.getPort()
        );
    }

}

