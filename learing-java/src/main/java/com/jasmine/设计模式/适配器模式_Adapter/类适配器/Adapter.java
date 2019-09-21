package com.jasmine.设计模式.适配器模式_Adapter.类适配器;

/**
 * @author : jasmineXz
 */

public class Adapter extends Usber implements Ps2 {

    @Override
    public void isPs2() {
        isUsb();
    }

}
