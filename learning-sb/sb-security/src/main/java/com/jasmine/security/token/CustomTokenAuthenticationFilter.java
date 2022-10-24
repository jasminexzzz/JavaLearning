package com.xzzz.security.token;//package com.jasmine.security.token;
//
//import com.jasmine.security.config.CustomAuthenticationFailureHandler;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationServiceException;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//public class CustomTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
//
//
//
//    public CustomTokenAuthenticationFilter() {
//        super("/login");
//        super.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());
//    }
//
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
//            throws AuthenticationException {
//
//        if (!request.getMethod().equals(HttpMethod.POST.name())) {
//            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
//        }
//
//        String token = obtainToken(request);
//        if (token == null || token.length() == 0) {
//            throw new BadCredentialsException("uid or token is null.");
//        }
//
//        CustomToken authRequest = new CustomToken(token);
//
//        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
//
//        return this.getAuthenticationManager().authenticate(authRequest);
//
//    }
//
//    protected String obtainToken(HttpServletRequest request) {
//        // TODO 获取TOKEN, 需要从header中获取
//        String token = request.getParameter("token");
//        return token == null ? "" : token.trim();
//    }
//}
