package com.xzzz.security.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class CustomUserDetailsService implements UserDetailsService {


    //TODO 注入一个查用户的mapper

    //TODO 注入一个查权限的mapper

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        System.out.println(String.format("=====> 查询用户信息, 用户名: %s", s));

        // TODO 用户 mapper 查询用户信息

        // TODO 权限 mapper 查询用户的权限

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new CustomGrantedAuthority("ROLE_ADMIN"));

        UserDetails userDetails = new CustomUserDetails(
            "user",
            "admin",
            roles
        );

        return userDetails;
    }
}
