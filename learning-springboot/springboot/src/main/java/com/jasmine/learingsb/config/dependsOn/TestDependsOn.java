package com.jasmine.learingsb.config.dependsOn;

import com.jasmine.learingsb.config.condition.bean.ConditionalOnBeanTestA1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @author : jasmineXz
 */
@Slf4j
@Configuration
public class TestDependsOn {

    @Bean(name = "testDependsOnTarget2")
    @DependsOn("testDependsOnTarget1")
    public TestDependsOnTarget2 getTestDependsOnTarget2(){
        return new TestDependsOnTarget2();
    }

    @Bean(name = "testDependsOnTarget1")
    public TestDependsOnTarget1 getTestDependsOnTarget1(){
        return new TestDependsOnTarget1();
    }


    private ConditionalOnBeanTestA1 conditionalOnBeanTestA1;

    /**
     * 注入是在创建bean之前
     * @param conditionalOnBeanTestA1
     */
    @Autowired
    public void setConditionalOnBeanTestA1 (ConditionalOnBeanTestA1 conditionalOnBeanTestA1) {
        log.trace("注入Bean: conditionalOnBeanTestA1");
        this.conditionalOnBeanTestA1 = conditionalOnBeanTestA1;
    }
}
