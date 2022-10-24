package com.jasmine.B3_design_mode.代理模式_Proxy.动态代理.例子一;

public class 卖大福 implements 卖香烟 {
    @Override
    public void sell(int num) {
        System.out.println("卖了"+num+"盒大福");
    }
}
