package com.jasmine.security.config;

import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            AuthenticationException e) throws IOException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        String msg = e.getMessage();
        if (e instanceof LockedException) {
            msg = "账户被锁定，请联系管理员!";
        } else if (e instanceof CredentialsExpiredException) {
            msg = "密码过期，请联系管理员!";
        } else if (e instanceof AccountExpiredException) {
            msg = "账户过期，请联系管理员!";
        } else if (e instanceof DisabledException) {
            msg = "账户被禁用，请联系管理员!";
        } else if (e instanceof BadCredentialsException) {
            msg = "用户名或者密码输入错误，请重新输入!";
        }
        out.write(msg);
        out.flush();
        out.close();
    }
}
