package com.xzzz.A1_java.base.C_线程.线程安全.Condition;

/**
 * 存钱的线程
 */
public class DepositThread extends Thread
{
    // 模拟用户账户
    private Account account;
    // 当前取钱线程所希望存款的钱数
    private double depositAmount;


    /**
     * 存钱的线程
     */
    public DepositThread(String name , Account account, double depositAmount) {
        super(name);
        this.account = account;
        this.depositAmount = depositAmount;
    }

    // 重复100次执行存款操作
    public void run() {
        for (int i = 1 ; i <= 10 ; i++ )
        {
            account.deposit(depositAmount,i);
        }
    }
}
