package com.xzzz.B3_design_mode.责任链模式_Chain.demo1;

/**
 * 一次请求
 * @author : jasmineXz
 */
public class FlowRequest {
    private int age;

    public FlowRequest (int age) {
        this.age = age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

}
