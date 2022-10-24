package com.xzzz.A1_java.base.C_线程.锁.读写锁;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteTest {

    public static void main(String[] args) {
        ReadWriteLock lock = new ReentrantReadWriteLock();
        Thread read1 = new Thread(new ReadTask(lock));
        Thread read2 = new Thread(new ReadTask(lock));
        Thread write = new Thread(new WriteTask(lock));
        read1.start();
        read2.start();
        write.start();
    }

    public static class ReadTask implements Runnable {

        private final ReadWriteLock readWriteLock;

        public ReadTask(ReadWriteLock readWriteLock) {
            this.readWriteLock = readWriteLock;
        }

        @Override
        public void run() {
            while (true) {
                Lock lock = readWriteLock.readLock();
                try {
                    if (lock.tryLock(1,TimeUnit.SECONDS)) {
                        try {
                            System.out.println("read");
                        } finally {
                            lock.unlock();
                        }
                    } else {
                        System.out.println("read fail");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ThreadUtil.safeSleep(500);
            }
        }
    }


    public static class WriteTask implements Runnable {

        private final ReadWriteLock readWriteLock;

        public WriteTask(ReadWriteLock readWriteLock) {
            this.readWriteLock = readWriteLock;
        }

        @Override
        public void run() {
            while (true) {
                Lock lock = readWriteLock.writeLock();
                try {
                    if (lock.tryLock(5, TimeUnit.SECONDS)) {
                        try {
                            System.out.println("write >>>>>>>>>>>>>>>>>>>>>>>>>>> start");
                            ThreadUtil.safeSleep(3000);
                            System.out.println("write >>>>>>>>>>>>>>>>>>>>>>>>>>> end");
                        } finally {
                            lock.unlock();
                        }
                    } else {
                        System.out.println("write fail");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ThreadUtil.safeSleep(3000);
            }
        }
    }

}
