package com.jasmine.设计模式.责任链模式_Chain.demo2;

public class Filter2 implements Filter {

    @Override
    public void doFilter(Request request) {
        request.setNum(request.getNum() + 1);
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
