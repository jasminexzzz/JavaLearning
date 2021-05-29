package com.jasmine.sbspringboot.config.autowire;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author : jasmineXz
 */
@Slf4j
public class TestAbstractAutowireCapableBeanFactory implements ApplicationContextAware {

    // DefaultListableBeanFactory
    private AutowireCapableBeanFactory autowireBeanFactory;

    private ApplicationContext applicationContext;


    public TestAbstractAutowireCapableBeanFactory(AutowireCapableBeanFactory autowireCapableBeanFactory) {
        this.autowireBeanFactory = autowireCapableBeanFactory;
        log.trace(autowireCapableBeanFactory.toString());
    }

    /**
     * 本测试用例传入{@link AutowireBeanFactoryTarget}
     */
    @SuppressWarnings("unchecked")
    <T> T postProcess(T object) {
        T result = null;
        try {
            // 初始化Bean
            result = (T) this.autowireBeanFactory.initializeBean(object, object.toString());
        } catch (RuntimeException e) {
            Class<?> type = object.getClass();
            throw new RuntimeException("Could not postProcess " + object + " of type " + type, e);
        }

        // 装配bean
        this.autowireBeanFactory.autowireBean(object);

        // 检查用此方法创建的Bean中的对象是否为IOC中的Bean, 是
        log.trace("==========> [A] {} 装配的是正常注入的Bean", ((AutowireBeanFactoryTarget) result).getAutowireBeanFactoryTarget1().toString());

//        AutowireBeanFactoryTarget a = autowireBeanFactory.getBean(AutowireBeanFactoryTarget.class);
////        // 检查此对象是否注入到IOC中
////        log.trace(a.toString());

        return result;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
