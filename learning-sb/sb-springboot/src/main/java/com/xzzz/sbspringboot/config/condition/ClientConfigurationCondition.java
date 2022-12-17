package com.xzzz.sbspringboot.config.condition;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 自定义 @Condition 条件
 * <code>
 * 使用 : @Conditional({ClientConfigurationCondition.class})
 * </code>
 *
 * @author jasmineXz
 */
public class ClientConfigurationCondition implements Condition {

    /**
     * @param conditionContext      conditionContext.getRegistry()       : 获取注册的bean
     *                              conditionContext.getBeanFactory()    : 获取提供bean definition的解析,注册功能,再对单例来个预加载(解决循环依赖问题).
     *                              conditionContext.getEnvironment()    : 获取环境配置
     *                              conditionContext.getResourceLoader() : ResourceLoader所加载的资源
     *                              conditionContext.getClassLoader()    : 获取类加载器
     * @param annotatedTypeMetadata annotatedTypeMetadata                : 被加上注解的源数据信息。比如annotatedTypeMetadata.
     * @return true:生效;false:不生效;
     */
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {

        BeanDefinitionRegistry beanDefinitionRegistry = conditionContext.getRegistry();

        boolean bool = beanDefinitionRegistry.containsBeanDefinition("com.jasmine.arthur.client.ArthurClientConfig");
        // 判断某个bean是否存在
        if (bool) {
            System.out.println("ArthurClientConfig 存在");
        } else {
            System.out.println("ArthurClientConfig 不存在");
        }


        // 判断某个类是否存在
        try {
            conditionContext.getClassLoader().loadClass("com.jasmine.arthur.client.ArthurClientConfig");
            System.out.println("ArthurClientConfig 存在");
            return true;
        } catch (ClassNotFoundException e) {
            System.out.println("ArthurClientConfig 不存在");
        }


        if (conditionContext.getBeanFactory().containsBean("com.jasmine.arthur.gateway.ArthurClientGatewayConfig")) {
            System.out.println("ArthurClientGatewayConfig 存在");
            return true;
        } else {
            System.out.println("ArthurClientGatewayConfig 不存在");
        }

        return false;
    }
}
