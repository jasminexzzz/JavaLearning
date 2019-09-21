package com.jasmine.JavaBase.线程.线程同步.Condition;

@SuppressWarnings("all")
public class DrawTest
{
    public static void main(String[] args)
    {
        // 创建一个账户
        Account acct = new Account("1234567" , 0);
        new DrawThread("取钱" , acct , 800).start();
        new DepositThread("甲" , acct , 800).start();
        new DepositThread("乙" , acct , 800).start();
//        new DepositThread("丙存" , acct , 800).start();
    }
}

