package com.jasmine.JavaBase.C_线程.线程安全.关键字_volatile;

/**
 * ****************************************************************************************
 *
 * 这个例子很好理解，main函数里启动一个线程，其run方法是一个以flag为标志位的无限循环。如果flag为
 * true则跳出循环。当main执行到12行的时候，flag被置为true，按逻辑分析此时线程该结束，即整个程序
 * 执行完毕。
 *
 * ****************************************************************************************
 *
 * 执行一下看看是什么结果？结果是令人惊讶的，程序始终也不会结束。main是肯定结束了的，其原因就是线
 * 程的run方法未结束，即run方法中的flag仍然为false。
 *
 * ****************************************************************************************
 *
 * 把第3行加上volatile修饰符，即private static volatile boolean flag = false;
 * 再执行一遍看看？结果是程序正常退出，volatile生效了。
 *
 * ***************************************************************************************
 *
 * 我们再修改一下。去掉volatile关键字，恢复到起始的例子，然后把 <code>while(!flag){}</code> 改为
 * <code>while(!flag){System.out.println(1);}，</code> 再执行一下看看。按分析，没有volatile关键
 * 字的时候，程序不会执行结束，虽然加上了打印语句，但没有做任何的关键字/逻辑的修改，应该程序也不会
 * 结束才对，但执行结果却是：程序正常结束。
 *
 * @author : jasmineXz
 */
public class VolatileExample extends Thread {
    //设置类静态变量,各线程访问这同一共享变量
    private static boolean flag = false;
//    private static volatile boolean flag = false;
    //无限循环,等待flag变为true时才跳出循环
    @Override
    public void run() {
        while (!flag){
//            System.out.println(1);
        }
    }

    public static void main(String[] args) throws Exception {
        new VolatileExample().start();
        //sleep的目的是等待线程启动完毕,也就是说进入run的无限循环体了
        Thread.sleep(100);
        flag = true;
    }
}
