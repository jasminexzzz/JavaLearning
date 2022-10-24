package com.xzzz.B3_design_mode.责任链模式_Chain.sevlet模式;

public class ZTest {

    public static void main(String[] args) throws Exception {

        FilterConfig filterConfig = new FilterConfig();

        Filter1 filter1 = new Filter1();
        Filter2 filter2 = new Filter2();
        Filter3 filter3 = new Filter3();

        filterConfig.addFilter(filter1);
        filterConfig.addFilter(filter2);
        filterConfig.addFilter(filter3);


        //web容器创建RequestInface对象，ResponseInface对象，FilterChain对象
        Request request = new Request("开始");
        Respone response = new Respone();
        FilterChain filterChain = new FilterChainImpl(filterConfig);


        filterConfig.getFilters().get(0).doFilter(request, response, filterChain);

        System.out.println(request.getUrl());
    }
}
