package com.jasmine.B3_design_mode.责任链模式_Chain.sevlet模式;

public interface Filter {
    public void doFilter(Request request,Respone response,FilterChain filterChain) throws Exception;

}
