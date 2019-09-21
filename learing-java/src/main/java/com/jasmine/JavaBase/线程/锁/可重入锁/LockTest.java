package com.jasmine.JavaBase.线程.锁.可重入锁;


public class LockTest{
    boolean isLocked = false;
    Thread  lockedBy = null;
    int lockedCount = 0;

    public synchronized void lock() throws InterruptedException{
        //currentThread 可以获取当前线程的引用
        Thread thread = Thread.currentThread();

        //1.线程如果未被锁 2.当前线程不为重入线程 则加锁
        while(isLocked && lockedBy != thread){
            wait();
        }
        isLocked = true;
        lockedCount++;
        lockedBy = thread;
    }

    public synchronized void unlock(){
        if(Thread.currentThread() == this.lockedBy){
            lockedCount--;
            if(lockedCount == 0){
                isLocked = false;
                notify();
            }
        }
    }
}
