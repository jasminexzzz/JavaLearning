package com.jasmine.设计模式.责任链模式_Chain;

/**
 * @author : jasmineXz
 */
public class Filter2 extends AbstractFilter {

    @Override
    public void doFilter(String request, String resp) {
        System.out.println("======> " + this.getClass().getName());
        next(request,resp);
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
