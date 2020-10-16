package com.jasmine.learingsb.config.autowire;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

/**
 * @author : jasmineXz
 */
@Slf4j
public class TestAbstractAutowireCapableBeanFactory {

    // DefaultListableBeanFactory
    private AutowireCapableBeanFactory autowireBeanFactory;


    public TestAbstractAutowireCapableBeanFactory(AutowireCapableBeanFactory autowireCapableBeanFactory) {
        this.autowireBeanFactory = autowireCapableBeanFactory;
        log.trace(autowireCapableBeanFactory.toString());
    }

    @SuppressWarnings("unchecked")
    <T> T postProcess(T object) {
        T result = null;
        try {
            result = (T) this.autowireBeanFactory.initializeBean(object, object.toString());
        }
        catch (RuntimeException e) {
            Class<?> type = object.getClass();
            throw new RuntimeException(
                    "Could not postProcess " + object + " of type " + type, e);
        }
        this.autowireBeanFactory.autowireBean(object);
        log.trace(((AutowireBeanFactoryTarget)result).getAutowireBeanFactoryTarget1().toString());
        return result;
    }

}
