package com.xzzz.security.config;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 默认的密码转换类
 */
public class CustomPasswordEncoder implements PasswordEncoder {

    // 生成密码, 默认明文返回
    @Override
    public String encode(CharSequence charSequence) {
        System.out.println(String.format("=====> 生成密码: %s",charSequence));
        return (String) charSequence;
    }

    // 判断密码, 默认明文密码
    @Override
    public boolean matches(CharSequence charSequence, String s) {
        System.out.println(String.format("=====> 判断密码: %s / %s",charSequence,s));
        return charSequence.equals(s);
    }
}
