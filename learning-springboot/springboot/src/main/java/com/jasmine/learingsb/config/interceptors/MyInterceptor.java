package com.jasmine.learingsb.config.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 配置拦截器
 *
 * 只对controller生效
 *
 * @author jasmineXz
 */
@Slf4j
public class MyInterceptor implements HandlerInterceptor {

    private boolean showLog = false;

    /**
     * 拦截（Controller方法调用之前）
     *
     * @param request request
     * @param response response
     * @param o    o
     * @return 通过与否
     * @throws Exception 异常处理
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) {
        if(showLog) {
            log.info("MyInterceptor ==> [01] preHandle");
        }

        return true;
    }

    // 此方法为处理请求之后调用（调用过controller方法之后，跳转视图之前）
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView mav) {
        if(showLog) {
            log.info("MyInterceptor ==> [02] postHandle");
        }
    }

    // 此方法为整个请求结束之后进行调用
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        if(showLog) {
            log.info("MyInterceptor ==> [03] afterCompletion");
        }
    }
}
