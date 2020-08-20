package com.jasmine.JavaBase.C_线程.线程安全.线程组;





class MyExHandler implements Thread.UncaughtExceptionHandler
{
    // 实现uncaughtException方法，该方法将处理线程的未处理异常
    public void uncaughtException(Thread t, Throwable e)
    {
        System.out.println(t + " 线程出现了异常：" + e);
    }
}
public class ExHandler
{
    public static void main(String[] args)
    {
        // 设置主线程的异常处理器
        Thread.currentThread().setUncaughtExceptionHandler(new MyExHandler());
        int a = 5 / 0;     // ①
        System.out.println("程序正常结束！");
    }
}
