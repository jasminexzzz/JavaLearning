package com.jasmine.A1_java.base.C_线程.线程安全.线程锁_Lock.例子1;


@SuppressWarnings("all")
public class DrawTest
{
    public static void main(String[] args)
    {
        // 创建一个账户
        Account acct = new Account();
        new DrawThread("******" , acct , 800).start();
        new DepositThread("甲）" , acct , 1500).start();
        new DepositThread("乙）" , acct , 2000).start();
//        new DepositThread("丙存" , acct , 800).start();
    }
}

