package com.jasmine.JavaBase.线程.ThreadTest;

public class JoinThread extends Thread
{
    // 提供一个有参数的构造器，用于设置该线程的名字
    public JoinThread(String name) {
        super(name);
    }

    // 重写run()方法，定义线程执行体
    public void run() {
        for (int i = 1; i <= 20 ; i++ )
        {
            System.out.println(getName() + "  " + i);
        }
    }

    public static void main(String[] args)throws Exception {
        // 启动子线程
        new JoinThread("新线程").start();
        for (int i = 1; i <= 20 ; i++ )
        {
            if (i == 5)
            {
                JoinThread jt = new JoinThread("被Join的线程1");
                jt.start();
                // main线程调用了jt线程的join()方法，main线程
                // 必须等jt执行结束才会向下执行
                jt.join();


                JoinThread jt2 = new JoinThread("被Join的线程2");
                jt2.start();
                // main线程调用了jt线程的join()方法，main线程
                // 必须等jt执行结束才会向下执行
                jt2.join();
            }
            System.out.println(Thread.currentThread().getName() + "  " + i);
        }
    }
}
