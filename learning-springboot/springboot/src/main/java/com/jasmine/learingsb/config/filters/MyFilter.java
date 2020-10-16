package com.jasmine.learingsb.config.filters;

import com.jasmine.learingsb.config.WebConfigurer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

/**
 * web体系的filter并不是与spring,它是Web体系的Filter,并不是bean
 *
 * 配置地址:
 * @see WebConfigurer#filterRegistrationBean()
 *
 * @author jasmineXz
 */
@Slf4j
public class MyFilter implements Filter {

    /**
     * 过滤器初始化时
     * @param filterConfig
     */
    @Override
    public void init(FilterConfig filterConfig) {
        log.trace("MyFilter ==> [01] init");
    }

    /**
     * 过滤器执行时
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.trace("MyFilter ==> [02] doFilter");
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {
        log.trace("MyFilter ==> [03] destroy");
    }
}
