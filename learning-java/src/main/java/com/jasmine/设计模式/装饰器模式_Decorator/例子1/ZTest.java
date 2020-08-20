package com.jasmine.设计模式.装饰器模式_Decorator.例子1;

/**
 * @author : jasmineXz
 */
public class ZTest {

    public static void main(String[] args) {
//        Computer c = new DiskComputer(new NormalComputer());
//        c.cpu();

        System.out.println("***************************");
        //也可以这样
        Computer c1 = new DiskComputer(new MemoryComputer(new NormalComputer()));
        c1.cpu();
    }
}
