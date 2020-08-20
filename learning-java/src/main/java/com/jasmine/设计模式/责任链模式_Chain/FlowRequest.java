package com.jasmine.设计模式.责任链模式_Chain;

/**
 * 一次请求
 * @author : jasmineXz
 */
public class FlowRequest {
    private int age;
    private String name;

    public FlowRequest () {

    }

    public FlowRequest age(int age) {
        this.age = age;
        return this;
    }

    public FlowRequest name(String name) {
        this.name = name;
        return this;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }
}
