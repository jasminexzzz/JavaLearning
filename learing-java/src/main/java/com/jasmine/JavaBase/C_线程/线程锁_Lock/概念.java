package com.jasmine.JavaBase.C_线程.线程锁_Lock;

/**
 * @author : jasmineXz
 */
public class 概念 {
    /**
     一. 概念
        1. CLH队列锁


     1. 创建锁
        Lock lock = new ReentrantLock();
        默认会创建一个new NonfairSync();非公平锁
        NonfairSync 会继承 Sync 并且实现lock方法
            Sync 继承 AbstractQueuedSynchronizer

     2. lock.lock()加锁方法
            compareAndSetState调用方法,其实是调用unsafe.compareAndSwapInt(this, stateOffset, expect, update);
            因为非公平锁会先尝试加锁,如果获取到锁,则进入setExclusiveOwnerThread
            代码为 :
             final void lock() {
                 if (compareAndSetState(0, 1))
                    setExclusiveOwnerThread(Thread.currentThread());
                 else
                    acquire(1);
             }

            否则 acquire
             public final void acquire(int arg) {
                 //tryAcquire先尝试获取"锁"
                 //如果成功,直接返回,失败继续执行后续代码
                 //addWaiter是给当前线程创建一个节点,并将其加入等待队列
                 //acquireQueued是当线程已经加入等待队列之后的行为.
                 if (!tryAcquire(arg) && acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
                    selfInterrupt();
             }
     */
}
