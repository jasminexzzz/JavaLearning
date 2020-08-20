package com.jasmine.设计模式.责任链模式_Chain;

/**
 * @author : jasmineXz
 */
public class Filter1 extends AbstractFilter {

    @Override
    public Object doFilter(FlowRequest request) {
        System.out.println("======> " + this.getClass().getName());

        if(10 > request.getAge() && request.getAge() > 0 ) {
            return request.getAge();
        }
        return next(request);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
