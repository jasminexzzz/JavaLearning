package com.jasmine.设计模式.适配器模式_Adapter.对象适配器;

/**
 * @author : jasmineXz
 */
public class Usber  implements Usb {
    @Override
    public void isUsb() {
        System.out.println("USB口");
    }
}
