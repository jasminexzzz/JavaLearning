package com.jasmine.learingsb.config;

import com.jasmine.learingsb.config.filters.TestFilter;
import com.jasmine.learingsb.config.interceptors.MyInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

/**
 *
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
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new TestFilter());
        registrationBean.setUrlPatterns(Arrays.asList("/*"));
        registrationBean.setOrder(Integer.MIN_VALUE); // 过滤器的优先级
        return registrationBean;
    }
}
