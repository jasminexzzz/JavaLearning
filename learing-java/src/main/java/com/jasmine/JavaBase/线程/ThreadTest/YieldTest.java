package com.jasmine.JavaBase.线程.ThreadTest;

public class YieldTest extends Thread{
    public YieldTest(String name){
        super(name);
    }
    // 定义run方法作为线程执行体
    public void run(){
        for (int i = 0; i < 50 ; i++ )
        {
            System.out.println(getName() + "  " + i);
            // 当i等于20时，使用yield方法让当前线程让步
            if (i == 20)
            {
                Thread.yield();
            }
        }
    }
    public static void main(String[] args)throws Exception{
        // 启动两条并发线程
        YieldTest yt1 = new YieldTest("HHHHHH");
        // 将ty1线程设置成最高优先级
        yt1.setPriority(Thread.MAX_PRIORITY);
        yt1.start();
        YieldTest yt2 = new YieldTest("LLLLLL");
        // 将yt2线程设置成最低优先级
        yt2.setPriority(Thread.MIN_PRIORITY);
        yt2.start();
    }
}
