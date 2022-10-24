package com.xzzz.B3_design_mode.责任链模式_Chain.sevlet模式;

public class Filter2 implements Filter {

    @Override
    public void doFilter(Request request, Respone response, FilterChain filterChain) throws Exception {
        request.setUrl(request.getUrl() + "-> filter2");
        if (request.getUrl().contains("filter2")) {
            throw new Exception("123123");
        }
        filterChain.doFilter(request,response);
    }
}
