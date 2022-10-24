package com.jasmine.A1_java.base.C_线程.练习.修改数字.VolatileAndAtomic;

/**
 * @author jasmineXz
 */
public class NumPlusVolatile {

    public static void main(String[] args) {
        RunDemo r = new RunDemo();
        for (int i = 0; i < 4; i++) {
            Thread t = new Thread(r);
            t.start();
        }
    }
}

class RunDemo implements Runnable {
    private volatile long num = 0;

    @Override
    public void run() {
        for (int i = 0; i < 50000; i++) {
            System.out.println(++num);
        }
    }
}
