package com.jasmine.B3_design_mode.责任链模式_Chain.demo2;


public interface Filter extends Order {

    void doFilter(Request request);

}
