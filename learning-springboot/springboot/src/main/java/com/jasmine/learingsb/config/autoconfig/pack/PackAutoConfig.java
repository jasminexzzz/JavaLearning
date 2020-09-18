package com.jasmine.learingsb.config.autoconfig.pack;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author : jasmineXz
 */
@Slf4j
@Configuration
@AutoConfigurationPackage(basePackages = "com.jasmine.testpack")
public class PackAutoConfig {

    @Autowired
    private BeanFactory beanFactory;

    @PostConstruct
    public void print () {
        log.trace("############################## 生效的package [开始] ##############################");
        log.trace("==========> {} 生效",this.getClass().getSimpleName());
        List<String> packages = AutoConfigurationPackages.get(beanFactory);
        packages.forEach(log::trace);
        log.trace("############################## 生效的package [结束] ##############################");
    }

}
