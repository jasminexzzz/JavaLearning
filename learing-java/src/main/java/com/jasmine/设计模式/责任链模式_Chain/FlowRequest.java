package com.jasmine.设计模式.责任链模式_Chain;

/**
 * 一次请求
 * @author : jasmineXz
 */
public class FlowRequest {
    private int key;
    private String name;

    public FlowRequest () {

    }

    public FlowRequest key(int key) {
        this.key = key;
        return this;
    }

    public FlowRequest name(String name) {
        this.name = name;
        return this;
    }
}
