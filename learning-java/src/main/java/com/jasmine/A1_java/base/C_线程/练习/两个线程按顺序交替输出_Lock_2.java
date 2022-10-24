package com.jasmine.A1_java.base.C_线程.练习;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 两个线程交替进行
 * @author : jasmineXz
 */
public class 两个线程按顺序交替输出_Lock_2 {
    public static void main(String[] args) {
        Resource resource = new Resource();
        List<Runnable> runList = new ArrayList<>();
        // 添加一个runnable接口,并实现run方法,方法内容即为 resource.printNnm()
        // 执行后线程1等待,线程2唤醒.
        runList.add(() -> {
            resource.printNnm(resource.firstCondition, resource.secondCondition);
        });

        runList.add(() -> {
            resource.printNnm(resource.secondCondition, resource.firstCondition);
        });
//

        for (int i = 1; i <= runList.size(); i++) {
            new Thread(runList.get(i - 1), "" + i).start();
        }
    }


    static class Resource {
        int num = 0; // 初始值
        Lock lock = new ReentrantLock();
        Condition firstCondition = lock.newCondition();
        Condition secondCondition = lock.newCondition();

        public void printNnm(Condition self, Condition next) {
            lock.lock();
            try { // 打印到99
                while (num < 20) {
                    num += 1;
                    System.out.println("线程" + Thread.currentThread().getName() + " " + num);
                    next.signal();
                    self.await();
                } // 最后一个打印99结束也要唤醒下一个线程，保证下一个线程不在阻塞状态
                next.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}

