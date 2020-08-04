package com.jasmine.JavaBase.C_线程.线程安全.线程Lock.例子1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 主要理解await和signalall
 */
public class Account {
    //可重入锁
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition cond = lock.newCondition();
    public static final double MONEY = 4000;

    //是否有钱 否-没钱  是-有钱
    private boolean flag = false;

    private double balance;




    /**
     * 取钱
     * 一次存一千 够4000了就取出来
     * @param drawAmount
     */
    public void draw(double drawAmount,int num){
        // 加锁
        lock.lock();
        System.out.println(Thread.currentThread().getName()+"第 "+num+" 次 - 开始");
        try{
            //如果没钱，暂停等着取
            if(!flag){
                System.out.println(Thread.currentThread().getName()+"第 "+num+" 次 - 没钱 ，当前："+ balance +"，不取了 我要暂停等着");
                cond.await();
            }else{
                //如果有钱 取了 再把flag改成没钱
                System.out.println(Thread.currentThread().getName()+"第 "+num+" 次 - 有钱 ，当前："+ balance +"，取完让所有人继续存");
                if(MONEY <= balance){
                    balance -= MONEY;
                    cond.signalAll();
                    flag = false;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 修改完成，释放锁
            System.out.println(Thread.currentThread().getName()+"第 "+num+" 次 - 结束");
            lock.unlock();
        }
    }

    /**
     * 存钱
     * @param depositAmount
     */
    public void deposit(double depositAmount,int num) {
        //lock只能保证同一个线程不会多次进入，比如甲第二次进入必须要等第一次出才可以
        lock.lock();
        System.out.println(Thread.currentThread().getName()+"第 "+num+" 次 + 开始");
        try{
            //如果有钱 我就不存了 等着别人取
            if(flag){
                System.out.println(Thread.currentThread().getName()+"第 "+num+" 次 + ，当前："+balance+"，有钱 不存了");
                cond.await();
            }else{
                System.out.println(Thread.currentThread().getName()+"第 "+num+" 次 + ，当前："+balance+"，钱不够，继续存！");
                balance += depositAmount;
                if(MONEY <= balance){
                    cond.signalAll();
                    flag = true;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            System.out.println(Thread.currentThread().getName()+"第 "+num+" 次 + 结束");
            lock.unlock();
        }
    }
}
