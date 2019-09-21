package com.jasmine.JavaBase.线程.线程同步.线程Lock.例子1;


/**
 * 取钱的线程
 */
public class DrawThread extends Thread
{
    // 模拟用户账户
    private Account account;
    // 当前取钱线程所希望取的钱数
    private double drawAmount;

    /**
     * 取钱的线程
     */
    public DrawThread(String name , Account account, double drawAmount) {
        super(name);
        this.account = account;
        this.drawAmount = drawAmount;
    }
    // 重复100次执行取钱操作
    public void run() {
        for (int i = 1 ; i <= 10 ; i++ )
        {
            account.draw(drawAmount,i);
        }
    }
}
