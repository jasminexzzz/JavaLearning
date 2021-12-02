package com.jasmine.设计模式.责任链模式_Chain.sevlet模式;

public interface FilterChain extends Filter{

    void doFilter(Request request,Respone response) throws Exception;

    @Override
    default void doFilter(Request request, Respone response, FilterChain filterChain) throws Exception {

    }

}
