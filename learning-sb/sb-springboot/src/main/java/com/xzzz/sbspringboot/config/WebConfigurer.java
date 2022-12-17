package com.xzzz.sbspringboot.config;

import com.xzzz.sbspringboot.config.filters.TestFilter;
import com.xzzz.sbspringboot.config.interceptors.MyInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

/**
 * @author jasmineXz
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截所有请求，通过判断是否有 @myInterceptor 注解 决定是否需要登录
        registry.addInterceptor(myInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public MyInterceptor myInterceptor() {
        return new MyInterceptor();
    }


    @Bean
    public FilterRegistrationBean<TestFilter> filterRegistrationBean() {
        FilterRegistrationBean<TestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TestFilter());
        registrationBean.setUrlPatterns(Collections.singletonList("/*"));
        registrationBean.setOrder(Integer.MIN_VALUE); // 过滤器的优先级
        return registrationBean;
    }
}
