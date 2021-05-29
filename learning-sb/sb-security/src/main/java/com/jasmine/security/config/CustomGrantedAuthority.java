package com.jasmine.security.config;

import org.springframework.security.core.GrantedAuthority;

/**
 * 查询用户权限
 */
public class CustomGrantedAuthority implements GrantedAuthority {

    private String role;

    public CustomGrantedAuthority(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
