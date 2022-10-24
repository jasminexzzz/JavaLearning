package com.xzzz.sbspringboot.bean.beandefinition;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 演示如何注册 BeanDefinition
 *
 * @author wangyf
 */
@Component
@SpringBootTest(
        classes = {BeanDefinitionTest.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE // 不启动WEB容器
)
@SuppressWarnings("all")
public class BeanDefinitionTest implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 第一种方式注册 BeanDefinition, 这种方式会比 {@link BeanDefinitionTest#register2} 优先
     *
     * 通过实现 {@link BeanDefinitionRegistryPostProcessor} 接口来执行回调
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        System.out.println(String.format("BeanDefinitionRegistry 的实现类: %s", registry.getClass().getName()));

        // 通过 BeanDefinitionBuilder 构建
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(BeanDefinitionTarget.class);
        beanDefinitionBuilder.addPropertyValue("name", "第一种方式");
        BeanDefinition target = beanDefinitionBuilder.getBeanDefinition();
        target.setPrimary(true);

        // 创建一个 BeanDefinitionHolder, 用于注入到容器中, 可以指定 Bean 的名称, 这样会影响到通过名称注入 Bean
        BeanDefinitionHolder holder = new BeanDefinitionHolder(target, BeanDefinitionTarget.class.getSimpleName() + "Primary");

        /**
         * 注册, 注意需要传入一个 {@link BeanDefinitionRegistry}, 这里使用的是实现 {@link BeanDefinitionRegistryPostProcessor}
         * 接口来获取到 BeanDefinitionRegistry
         */
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);

        BeanDefinitionTarget bean = (BeanDefinitionTarget) applicationContext.getBean("BeanDefinitionTargetPrimary");
        System.out.println(String.format("第一种方式注册 BeanDefinition, 并获取Bean: %s", bean.getName()));
    }


    /**
     * 第二种方式注册 BeanDefinition
     *
     * 执行下列代码时注入 BeanDefinition
     */
    @Test
    public void register2() {
        // 通过 BeanDefinitionBuilder 构建
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(BeanDefinitionTarget.class);
        beanDefinitionBuilder.addPropertyValue("name", "第二种方式");
        BeanDefinition target = beanDefinitionBuilder.getBeanDefinition();

        // 创建一个 BeanDefinitionHolder, 用于注入到容器中
        BeanDefinitionHolder holder = new BeanDefinitionHolder(target, BeanDefinitionTarget.class.getSimpleName());

        /**
         * 注册, 注意需要传入一个 {@link BeanDefinitionRegistry}, {@link AnnotationConfigApplicationContext} 是其中一个
         * 实现类, 可以通过 {@link ApplicationContextAware} 接口获取
         *
         */
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, (AnnotationConfigApplicationContext)applicationContext);

        BeanDefinitionTarget bean = (BeanDefinitionTarget) applicationContext.getBean("BeanDefinitionTarget");
        System.out.println(String.format("第二种方式注册 BeanDefinition, 并获取Bean: %s", bean.getName()));
    }


    /**
     * 第三种方式注册 BeanDefinition, 如果要测试本方法, 你需要先将 {@link BeanDefinitionTest#register2} 注释掉
     *
     * 通过实现 {@link BeanDefinitionRegistryPostProcessor} 接口来执行回调
     */
    @Test
    public void register3() {
        // 通过 BeanDefinitionBuilder 构建
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(BeanDefinitionTarget.class);
        beanDefinitionBuilder.addPropertyValue("name", "第三种方式");
        BeanDefinition target = beanDefinitionBuilder.getBeanDefinition();

        /**
         * 直接使用 {@link BeanDefinitionRegistry} 注册, 这种方式无法通过 beanName 获取 Bean, 只能通过类型获取 Bean
         */
        BeanDefinitionRegistry registry = (AnnotationConfigApplicationContext)applicationContext;
        registry.registerBeanDefinition(target.getClass().getSimpleName(), target);

        BeanDefinitionTarget bean = applicationContext.getBean(BeanDefinitionTarget.class);
        System.out.println(String.format("第三种方式注册 BeanDefinition, 并获取Bean: %s", bean.getName()));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
