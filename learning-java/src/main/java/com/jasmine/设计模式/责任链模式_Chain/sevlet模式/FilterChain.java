package com.jasmine.设计模式.责任链模式_Chain.sevlet模式;

public interface FilterChain {
    public void doFilter(Request request,Respone response) throws Exception;
}
