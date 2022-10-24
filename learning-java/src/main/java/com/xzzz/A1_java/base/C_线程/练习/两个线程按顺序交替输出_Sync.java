package com.xzzz.A1_java.base.C_线程.练习;

/**
 * 两个线程交替进行
 * @author : jasmineXz
 */
@SuppressWarnings("all")
public class 两个线程按顺序交替输出_Sync{

    public static void main(String[] args) {
        两个线程按顺序交替输出_Sync s = new 两个线程按顺序交替输出_Sync();
        R r = new R(s);
        Thread t1 = new Thread(r,"- - -");
        Thread t2 = new Thread(r,"+ + +");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("eeeeeeeeee");
    }
}


class R implements Runnable{

    private 两个线程按顺序交替输出_Sync s;

    private int a;

    public R(两个线程按顺序交替输出_Sync s){
        this.s = s;
    }

    private void add(){
        synchronized (s){
            for(int i = 0 ; i < 10 ; i ++){
                try {
                    System.out.println(Thread.currentThread().getName() + " " + ++a);
                    s.notify();
                    s.wait(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void run() {
        add();
    }
}


