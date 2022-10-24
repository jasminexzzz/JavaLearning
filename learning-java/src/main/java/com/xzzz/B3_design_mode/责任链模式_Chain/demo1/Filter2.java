package com.xzzz.B3_design_mode.责任链模式_Chain.demo1;

/**
 * @author : jasmineXz
 */
public class Filter2 extends AbstractFilter {

    @Override
    public FlowRequest doFilter(FlowRequest request) {
        System.out.println("======> " + this.getClass().getName());

        if(20 > request.getAge() && request.getAge() > 10 ) {
            return request;
        }
        return next(request);
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
