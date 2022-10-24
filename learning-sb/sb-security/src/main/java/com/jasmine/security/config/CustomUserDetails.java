package com.xzzz.security.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private String username;
    private String password;
    private Collection<GrantedAuthority> roles;

    public CustomUserDetails(String username, String password, Collection<GrantedAuthority> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }


    // 需要修改为true
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 需要修改为true
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 需要修改为true
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 需要修改为true
    @Override
    public boolean isEnabled() {
        return true;
    }
}
