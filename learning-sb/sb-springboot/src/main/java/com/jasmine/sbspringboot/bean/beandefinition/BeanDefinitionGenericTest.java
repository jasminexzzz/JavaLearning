package com.jasmine.sbspringboot.bean.beandefinition;

import org.junit.jupiter.api.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.test.context.SpringBootTest;
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
public class BeanDefinitionGenericTest {

    /**
     * 第一种创建 BeanDefinition 的方式
     */
    @Test
    public void generic1() {
        // 1.通过 BeanDefinitionBuilder 构建
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(BeanDefinitionTarget.class);

        // 可以设置属性
        beanDefinitionBuilder.addPropertyValue("name", "第一种创建方式");

        // 获取 BeanDefinition 实例,并非 Bean 终态，可以自定义修改
        BeanDefinition target = beanDefinitionBuilder.getBeanDefinition();

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
}
