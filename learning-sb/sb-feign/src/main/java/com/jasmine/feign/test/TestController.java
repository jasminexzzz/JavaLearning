package com.jasmine.feign.test;

import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

/**
 * @author wangyf
 */
@RestController
@RequestMapping("/feign/test")
public class TestController {

    private TestFeignClient testFeignClient;

    @PostConstruct
    public void init() {
        testFeignClient = Feign.builder()
                .client(new OkHttpClient())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logger(new Slf4jLogger(TestFeignClient.class))
                .logLevel(Logger.Level.FULL)
                .target(TestFeignClient.class, "http://localhost:12345");
    }

    @GetMapping("/user1")
    public User user1(String name, Integer age) {
        return testFeignClient.get1(name, age);
    }

    @GetMapping("/user2")
    public User user2(@ModelAttribute User user) {
        return testFeignClient.get2(user);
    }

    @PostMapping("/user")
    public User addUser(@RequestBody User user) {
        return testFeignClient.post1(user);
    }

    @PostMapping("/user/param")
    public User addUser(@RequestParam String name, @RequestBody User user) {
        return testFeignClient.post2(name, user);
    }
}
