package com.jasmine.learingsb.config.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author jasmineXz
 */
public class MyFilter implements Filter {
    private final Logger log = LoggerFactory.getLogger(MyFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("MyFilter ==> [01] init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        log.info("MyFilter ==> [02] doFilter");
        filterChain.doFilter(request,response);
    }

    @Override
    public void destroy() {
        log.info("MyFilter ==> [03] destroy");
    }
}