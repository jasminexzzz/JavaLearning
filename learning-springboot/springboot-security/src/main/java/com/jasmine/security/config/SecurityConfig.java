package com.jasmine.security.config;

import com.jasmine.security.token.CustomTokenAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity(debug = true)
// 开启全局方法安全验证
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 自定义密码转换方式
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }


    /**
     * 配置 HttpSecurity
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //
            .anyRequest().authenticated()

            .and()
            .formLogin()
            // 自定义登陆成功处理方式
            .successHandler(new CustomAuthenticationSuccessHandler())
            // 自定义登陆失败处理方式
            .failureHandler(new CustomAuthenticationFailureHandler())
            .permitAll()

            .and()
            .logout()
            .logoutUrl("/logout")
            // 自定义注销成功处理方式
            .logoutSuccessHandler(new CustomLogoutSuccessHandler())
            .permitAll()

            .and()
            .addFilterBefore(new CustomTokenAuthenticationFilter("/**"), UsernamePasswordAuthenticationFilter.class)
            .csrf().disable()
            // 未登陆的拒绝策略
            .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());
    }

    /**
     * 配置自定义用户登陆查询
     */
    @Bean
    protected UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

}

