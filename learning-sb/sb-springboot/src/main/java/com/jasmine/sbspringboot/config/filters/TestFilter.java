package com.jasmine.sbspringboot.config.filters;

import com.jasmine.sbspringboot.config.WebConfigurer;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

/**
 * web体系的filter并不是与spring,它是Web体系的Filter,并不是bean
 * <p>
 * 配置地址:
 *
 * @author jasmineXz
 * @see WebConfigurer#filterRegistrationBean()
 */
@Slf4j
public class TestFilter implements Filter {

    /**
     * 过滤器初始化时
     *
     * @param filterConfig
     */
    @Override
    public void init(FilterConfig filterConfig) {
        log.trace("TestFilter ==> [01] init");
    }

    /**
     * 过滤器执行时
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.trace("TestFilter ==> [02] doFilter");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        log.trace("TestFilter ==> [03] destroy");
    }
}
