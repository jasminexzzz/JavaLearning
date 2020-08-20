package com.jasmine.JavaBase.C_线程.锁.不可重入锁;

public class Lock{

    private boolean isLocked = false;

    public synchronized void lock() throws InterruptedException{
        while(isLocked){
            wait();
        }
        isLocked = true;
    }

    public synchronized void unlock(){
        isLocked = false;
        notify();
    }

}
