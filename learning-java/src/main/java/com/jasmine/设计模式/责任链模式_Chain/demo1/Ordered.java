package com.jasmine.设计模式.责任链模式_Chain.demo1;

/**
 * 排序
 * @author : jasmineXz
 */
public interface Ordered {

    /**
     * 获取该过滤器顺序,越小越先执行
     */
    int getOrder();
}
