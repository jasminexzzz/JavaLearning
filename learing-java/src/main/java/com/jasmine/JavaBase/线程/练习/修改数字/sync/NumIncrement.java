package com.jasmine.JavaBase.线程.练习.修改数字.sync;


/**
 * 多线程把数字加到100
 */
@SuppressWarnings("all")
public class NumIncrement implements Runnable {
    private int num;

    private synchronized void sync(){
        System.out.println(Thread.currentThread().getName() + ":" + ++num);
    }

    private void notSync(){
        System.out.println("run --> "+Thread.currentThread().getName() + ":" + ++num);
    }

    @Override
    public void run() {
        for (int i = 1 ; i <= 50 ; i++ ) {
            sync();
        }
    }


    public static void main(String[] args) {
        NumIncrement st = new NumIncrement();     // ①
        // 通过new Thread(target , name)方法创建新线程
        new Thread(st , "新线程1").start();
        new Thread(st , "新线程2").start();
    }
}

