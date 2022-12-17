package com.xzzz.sbspringboot.context.scan;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Spring 提供的一种最常用的扫描类的方式, 本例通过扫描包含某个注解的类
 *
 * @author wangyf
 */
@Component
@SpringBootTest(
    classes = {TestScanner.class},
    webEnvironment = SpringBootTest.WebEnvironment.NONE // 不启动WEB容器
)
public class TestScanner implements EnvironmentAware, ResourceLoaderAware {

    private Environment environment;

    private ResourceLoader resourceLoader;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * 获取扫描器
     * @return 扫描器
     */
    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (!beanDefinition.getMetadata().isAnnotation()) {
                        isCandidate = true;
                    }
                }
                return isCandidate;
            }
        };
    }

    /**
     * 扫描
     */
    @Test
    public void scan() {
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);

        // 要扫描的注解
        scanner.addIncludeFilter(new AnnotationTypeFilter(TargetAnnotation.class));

        // 指定一个路径, 返回一个 BeanDefinition 的集合
        Set<BeanDefinition> beans = scanner.findCandidateComponents("com.jasmine.sbspringboot");

        // 打印扫描到的 BeanDefinition
        beans.forEach(bean -> {
            System.out.println(bean.getBeanClassName());
        });
    }


}
