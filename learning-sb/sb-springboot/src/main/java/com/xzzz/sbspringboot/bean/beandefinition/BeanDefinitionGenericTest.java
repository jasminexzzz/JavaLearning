package com.xzzz.sbspringboot.bean.beandefinition;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 演示如何创建 BeanDefinition
 *
 * @author wangyf
 */
@Component
@SpringBootTest(
    classes = {BeanDefinitionGenericTest.class},
    webEnvironment = SpringBootTest.WebEnvironment.NONE // 不启动WEB容器
)
public class BeanDefinitionGenericTest implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 第一种创建 BeanDefinition 的方式
     */
    @Test
    public void generic1() {
        // 1.通过 BeanDefinitionBuilder 构建
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(BeanDefinitionTarget.class);

        // 可以设置属性
        builder.addPropertyValue("name", "第一种创建方式");

        // 获取 BeanDefinition 实例,并非 Bean 终态，可以自定义修改
        BeanDefinition target = builder.getBeanDefinition();

        // 打印
        System.out.println(target.getBeanClassName());
    }


    /**
     * 第二种创建 BeanDefinition 的方式
     */
    @Test
    public void generic2() {
        // 2. 通过 AbstractBeanDefinition 以及派生类
        GenericBeanDefinition target = new GenericBeanDefinition();

        // 设置 Bean 类型
        target.setBeanClass(BeanDefinitionTarget.class);

        // 通过 MutablePropertyValues 批量操作属性
        MutablePropertyValues propertyValues = new MutablePropertyValues();

        //推荐使用add 而不是 addProperty
        propertyValues.add("name", "第二种创建方式");

        // 通过 set MutablePropertyValues 批量操作属性
        target.setPropertyValues(propertyValues);

        // 打印
        System.out.println(target.getBeanClassName());
    }


    /**
     * 在创建时指定构造的执行方法
     */
    @Test
    public void generic3() {

        // 在创建 builder 时指定方法, 该放回会在真正创建对象时执行, 返回的对象即为在 IOC 中的 Bean 的实例
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(BeanDefinitionTarget.class, () -> {
            BeanDefinitionTarget beanTarget = new BeanDefinitionTarget();
            beanTarget.setName("第三种创建方式");
            return beanTarget;
        });


        BeanDefinition target = builder.getBeanDefinition();

        BeanDefinitionHolder holder = new BeanDefinitionHolder(target, BeanDefinitionTarget.class.getSimpleName());
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, (AnnotationConfigApplicationContext)applicationContext);

        // 将 BeanDefinition 注册为 Bean 后, 可以查看 name 的变化
        BeanDefinitionTarget bean = (BeanDefinitionTarget) applicationContext.getBean("BeanDefinitionTarget");
        System.out.println(String.format("第二种方式注册 BeanDefinition, 并获取Bean: %s", bean.getName()));

    }
}
