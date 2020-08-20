package com.jasmine.JavaBase.C_线程.线程安全.Condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account
{
    // 显式定义Lock对象
    private final Lock lock = new ReentrantLock();//ReentrantLock是一个可重入锁
    // 获得指定Lock对象对应的Condition
    private final Condition cond  = lock.newCondition();
    // 封装账户编号、账户余额的两个成员变量
    private String accountNo;
    private double balance;
    // 标识账户中是否已有存款的旗标
    private boolean flag = false;

    public Account(){}
    // 构造器
    public Account(String accountNo , double balance){
        this.accountNo = accountNo;
        this.balance = balance;
    }

    // accountNo的setter和getter方法
    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }
    public String getAccountNo() {
        return this.accountNo;
    }
    // 因此账户余额不允许随便修改，所以只为balance提供getter方法，
    public double getBalance() {
        return this.balance;
    }

    /**
     * 取钱
     * @param drawAmount
     */
    public void draw(double drawAmount,int num) {
        // 加锁
        lock.lock();
        System.out.println("****第("+num+")次取");
        try {
            // 如果flag为假，表明账户中还没有人存钱进去，取钱方法阻塞
            if (!flag) {
                cond.await();
                System.out.println("没取到钱");
            } else {
                // 执行取钱
                System.out.println(Thread.currentThread().getName() + " 取钱:" +  drawAmount);
                balance -= drawAmount;
                System.out.println("账户余额为：" + balance);
                System.out.println("********************");
                // 将标识账户是否已有存款的旗标设为false。
                flag = false;
                // 唤醒其他线程
                cond.signalAll();
            }
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 存钱
     * @param depositAmount
     */
    public void deposit(double depositAmount,int num) {
        lock.lock();
        System.out.println(Thread.currentThread().getName()+"第 "+num+" 次存");
        try {
            // 如果flag为真，表明账户中已有人存钱进去，则存钱方法阻塞
            if (flag){
                cond.await();
                System.out.println(Thread.currentThread().getName() + "存时账户中有钱");
            } else {
                // 执行存款
                System.out.println(Thread.currentThread().getName() + " 存款:" +  depositAmount);
                balance += depositAmount;
                System.out.println("账户余额为：" + balance);
                // 将表示账户是否已有存款的旗标设为true
                flag = true;
                // 唤醒其他线程
                cond.signalAll();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }finally{
            lock.unlock();
        }
    }

    // 下面两个方法根据accountNo来重写hashCode()和equals()方法
    public int hashCode() {
        return accountNo.hashCode();
    }
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if (obj !=null && obj.getClass() == Account.class) {
            Account target = (Account)obj;
            return target.getAccountNo().equals(accountNo);
        }
        return false;
    }
}