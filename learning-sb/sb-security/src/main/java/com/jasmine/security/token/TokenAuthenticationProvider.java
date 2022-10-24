package com.xzzz.security.token;//package com.jasmine.security.token;
//
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class TokenAuthenticationProvider implements AuthenticationProvider {
//
//    // 根用户拥有全部的权限
//    private final List<GrantedAuthority> authorities = Arrays.asList(
//            new SimpleGrantedAuthority("CAN_SEARCH"),
//            new SimpleGrantedAuthority("CAN_EXPORT"),
//            new SimpleGrantedAuthority("CAN_IMPORT"),
//            new SimpleGrantedAuthority("CAN_BORROW"),
//            new SimpleGrantedAuthority("CAN_RETURN"),
//            new SimpleGrantedAuthority("CAN_REPAIR"),
//            new SimpleGrantedAuthority("CAN_DISCARD"),
//            new SimpleGrantedAuthority("CAN_EMPOWERMENT"),
//            new SimpleGrantedAuthority("CAN_BREED"));
//
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//
//        if (authentication.isAuthenticated()) {
//            return authentication;
//        }
//
//        //获取过滤器封装的token信息
//        CustomToken authenticationToken = (CustomToken) authentication;
//
//        // TODO 查询TOKEN
//
//
////        不通过
//        if (authenticationToken == null) {
//            throw new BadCredentialsException("授权token无效，请重新登陆");
//        }
//
//        Map<String, String> map = new HashMap<>();
//        map.put("a","1");
//        map.put("b","2");
//        map.put("c","3");
//
//        CustomToken authenticationResult = new CustomToken(map, authorities);
//
//        return authenticationResult;
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return CustomToken.class.isAssignableFrom(authentication);
//    }
//
//}
