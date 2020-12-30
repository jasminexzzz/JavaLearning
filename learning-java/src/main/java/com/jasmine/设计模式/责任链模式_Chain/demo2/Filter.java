package com.jasmine.设计模式.责任链模式_Chain.demo2;


public interface Filter extends Order {

    void doFilter(Request request);

}
