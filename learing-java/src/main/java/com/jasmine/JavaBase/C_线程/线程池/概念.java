package com.jasmine.JavaBase.C_线程.线程池;

/**
 * @author : jasmineXz
 */
public class 概念 {
    /**
     {
     参考资料
     @link https://blog.csdn.net/weixin_28760063/article/details/81266152
     }

     一. Java中的ThreadPoolExecutor类
         public ThreadPoolExecutor(int corePoolSize,
             int maximumPoolSize,
             long keepAliveTime,
             TimeUnit unit,
             BlockingQueue<Runnable> workQueue,
             ThreadFactory threadFactory,
             RejectedExecutionHandler handler) {
                 if (corePoolSize < 0 ||
                 maximumPoolSize <= 0 ||
                 maximumPoolSize < corePoolSize ||
                 keepAliveTime < 0)
                 throw new IllegalArgumentException();
                 if (workQueue == null || threadFactory == null || handler == null)
                 throw new NullPointerException();
                 this.acc = System.getSecurityManager() == null ?
                 null :
                 AccessController.getContext();
                 this.corePoolSize = corePoolSize;
                 this.maximumPoolSize = maximumPoolSize;
                 this.workQueue = workQueue;
                 this.keepAliveTime = unit.toNanos(keepAliveTime);
                 this.threadFactory = threadFactory;
                 this.handler = handler;
         }

        主要参数:
         corePoolSize    : 核心池的大小.在创建了线程池后，默认情况下，线程池中并没有任何线程，而是等待有任务到来才创建线程去执行任务，
         maximumPoolSize : 线程池最大线程数,它表示在线程池中最多能创建多少个线程.
         keepAliveTime   : 表示线程没有任务执行时最多保持多久时间会终止.默认情况下,只有当线程池中的线程数大于corePoolSize时,keepAliveTime
                           才会起作用,直到线程池中的线程数不大于corePoolSize,即当线程池中的线程数大于corePoolSize时,如果一个线程空闲的时
                           间达到keepAliveTime,则会终止,直到线程池中的线程数不超过corePoolSize。但是如果调用了allowCoreThreadTimeOut(bo
                           olean)方法,在线程池中的线程数不大于corePoolSize时,keepAliveTime参数也会起作用,直到线程池中的线程数为0；
         unit            : 参数keepAliveTime的时间单位，有7种取值，在TimeUnit类中有7种静态属性：
                             TimeUnit.DAYS;              //天
                             TimeUnit.HOURS;             //小时
                             TimeUnit.MINUTES;           //分钟
                             TimeUnit.SECONDS;           //秒
                             TimeUnit.MILLISECONDS;      //毫秒
                             TimeUnit.MICROSECONDS;      //微妙
                             TimeUnit.NANOSECONDS;       //纳秒
         workQueue       : 一个阻塞队列，用来存储等待执行的任务，这个参数的选择也很重要，会对线程池的运行过程产生重大影响，一般来说，这里的阻塞
                           队列有以下几种选择：


     */
}
