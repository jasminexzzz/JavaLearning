package com.jasmine.sbspringbootmodule.autoconfig.impot.pattern3;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author : jasmineXz
 */
class ImportTargetRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annoMetadata, BeanDefinitionRegistry beanDefRegistry) {
        //指定bean定义信息（包括bean的类型、作用域...）
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(ImportTarget1.class);
        //注册一个bean指定bean名字（id）
        beanDefRegistry.registerBeanDefinition("ImportTarget1111",rootBeanDefinition);
    }
}
