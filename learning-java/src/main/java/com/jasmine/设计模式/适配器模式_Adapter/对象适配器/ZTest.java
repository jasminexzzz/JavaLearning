package com.jasmine.设计模式.适配器模式_Adapter.对象适配器;

/**
 * @author : jasmineXz
 */
public class ZTest {
    public static void main(String[] args) {
        Ps2 p = new Adapter(new Usber());
        p.isPs2();
    }
}
