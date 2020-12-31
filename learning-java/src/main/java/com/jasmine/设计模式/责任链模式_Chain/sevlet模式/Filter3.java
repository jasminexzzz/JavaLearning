package com.jasmine.设计模式.责任链模式_Chain.sevlet模式;

public class Filter3 implements Filter {

    @Override
    public void doFilter(Request request, Respone response, FilterChain filterChain) throws Exception {
        request.setUrl(request.getUrl() + "-> filter3");
        filterChain.doFilter(request,response);
    }
}
