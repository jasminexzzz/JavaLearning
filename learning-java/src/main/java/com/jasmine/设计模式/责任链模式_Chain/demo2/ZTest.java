package com.jasmine.设计模式.责任链模式_Chain.demo2;



/**
 * 业务说明
 * 这个责任链不包含返回值 (其实包含与否并不影响, 操作的都是同一个对象的值)
 * 链式调用中会增加 {@link Request#num} 的大小
 *
 * 责任链
 * 该种责任链实际上是将多个对象放入了循环中一次调用,从而形成链式调用,这种的中断方式通常比较
 *
 */
public class ZTest {

    public static void main(String[] args) {
        FilterChain chain = new FilterChain();
        registerFilter(chain);
        Request request = new Request(1);
        chain.doFilter(request);
        System.out.println(request.getNum());
    }

    static void registerFilter (FilterChain filterChain) {
        Filter filter1 = new Filter1();
        Filter filter2 = new Filter2();
        Filter filter3 = new Filter3();
        filterChain.setFilter(filter1);
        filterChain.setFilter(filter2);
        filterChain.setFilter(filter3);

        filterChain.getOrder();
    }
}
