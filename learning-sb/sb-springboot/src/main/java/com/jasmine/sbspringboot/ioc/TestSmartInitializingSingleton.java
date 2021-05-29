package com.jasmine.sbspringboot.ioc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Iterator;

/**
 * @author : jasmineXz
 */
@Slf4j
@Component
public class TestSmartInitializingSingleton implements SmartInitializingSingleton {

    @Autowired
    private DefaultListableBeanFactory defaultListableBeanFactory;

    @Override
    public void afterSingletonsInstantiated() {
        log.trace("==========> 我是一个单例Bean, 调用了我的实例化完成后回调, 在@PostConstruct 之后执行");
        Iterator<String> beanNamesIterator = defaultListableBeanFactory.getBeanNamesIterator();
        while (beanNamesIterator.hasNext()) {
            log.trace(beanNamesIterator.next());
        }
    }

    @PostConstruct
    public void print() {
        log.trace("==========> {} 已注入IOC", this);
    }

}
