package com.jasmine.es;

import com.jasmine.common.core.util.spring.SpringEnvUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

/**
 * @author jasmine
 */
@Slf4j
@SpringBootApplication
public class SbEsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbEsApplication.class, args);
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

