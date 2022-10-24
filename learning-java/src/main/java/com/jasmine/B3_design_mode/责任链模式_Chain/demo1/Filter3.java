package com.jasmine.B3_design_mode.责任链模式_Chain.demo1;

/**
 * @author : jasmineXz
 */
public class Filter3 extends AbstractFilter {

    @Override
    public FlowRequest doFilter(FlowRequest request) {
        System.out.println("======> " + this.getClass().getName());

        if(30 > request.getAge() && request.getAge() > 20 ) {
            return request;
        }
        return next(request);
    }

    @Override
    public int getOrder() {
        return 3;
    }
}
