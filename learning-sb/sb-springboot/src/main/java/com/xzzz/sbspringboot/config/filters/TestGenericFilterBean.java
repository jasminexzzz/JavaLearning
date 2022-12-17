package com.xzzz.sbspringboot.config.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 不注入不生效
 *
 * @author : jasmineXz
 */
@Slf4j
@Component
public class TestGenericFilterBean extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        log.trace("TestGenericFilterBean");
        filterChain.doFilter(request, response);
    }
}
