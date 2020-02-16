package com.jasmine.设计模式.责任链模式_Chain;

/**
 * @author : jasmineXz
 */
public class Filter2 extends AbstractFilter {

    @Override
    public Object doFilter(FlowRequest request) {
        System.out.println("======> " + this.getClass().getName());
        if(20 > request.getAge() && request.getAge() > 10 ) {
            return request.getAge();
        }
        return next(request);
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
