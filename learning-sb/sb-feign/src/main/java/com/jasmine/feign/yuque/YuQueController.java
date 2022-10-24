package com.xzzz.feign.yuque;

import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * @author wangyf
 */
@Slf4j
@RestController
@RequestMapping("/feign/yuque")
public class YuQueController {

    private YuQueFeignClient yuQueFeignClient;

    @PostConstruct
    public void init() {
        yuQueFeignClient = Feign.builder()
                .client(new OkHttpClient())
                .decoder(new JacksonDecoder())
                .encoder(new JacksonEncoder())
                .logger(new Slf4jLogger(YuQueFeignClient.class))
                .logLevel(Logger.Level.FULL)
                .target(YuQueFeignClient.class, "https://www.yuque.com");
    }

    @GetMapping("/user")
    public Object user() {
        return yuQueFeignClient.user();
    }

    /**
     * 获取文档集合
     *
     * @param id 1:10858843
     *           2:11236337
     * @return 文档集合
     */
    @GetMapping("/docs")
    public Object docs(Integer id) {
        return yuQueFeignClient.docs(id);
    }

}
