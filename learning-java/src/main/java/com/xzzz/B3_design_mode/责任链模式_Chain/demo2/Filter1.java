package com.xzzz.B3_design_mode.责任链模式_Chain.demo2;

public class Filter1 implements Filter {

    @Override
    public void doFilter(Request request) {
        request.setNum(request.getNum() + 1);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
