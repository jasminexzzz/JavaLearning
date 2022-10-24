package com.xzzz.B3_design_mode.责任链模式_Chain.demo1;

/**
 * @author : jasmineXz
 */
public class Filter1 extends AbstractFilter {

    @Override
    public FlowRequest doFilter(FlowRequest request) {
        System.out.println("======> " + this.getClass().getName());

        if(10 > request.getAge() && request.getAge() > 0 ) {
            return request;
        }
        return next(request);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
