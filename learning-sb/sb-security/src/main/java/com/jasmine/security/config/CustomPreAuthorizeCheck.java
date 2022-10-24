package com.xzzz.security.config;

import cn.hutool.core.util.StrUtil;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * @see SecurityExpressionRoot Spring—EL表达式解析类。
 */
@Service("rolePre") // 服务起别名方便调用
public class CustomPreAuthorizeCheck {

    public boolean check(String role){
        if (StrUtil.isBlank(role)) {
            return false;
        }

        // 授权上下文中获取当前登录用户权限
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(StringUtils::hasText)
                .anyMatch(x -> PatternMatchUtils.simpleMatch(role, x));
    }
}
