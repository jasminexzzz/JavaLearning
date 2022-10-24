package com.jasmine.B3_design_mode.单例模式_Singleton.懒汉式;


/**
 * 懒汉式单例
 * @author : jasmineXz
 */
public class Singleton {

    /* **********************************************************************
     * 线程不安全
     * pool-1-thread-1:com.jasmine.设计模式.单例模式_Singleton.懒汉式.Singleton@3856c761
     * pool-1-thread-7:com.jasmine.设计模式.单例模式_Singleton.懒汉式.Singleton@2f7b4da
     *
     * 运行可发现,至少有一个线程与其他线程生成实例不是同一个,所以有线程安全问题
     *********************************************************************** */
    private Singleton() {}
    private static volatile Singleton single = null;
    public static Singleton getInstance() {
        if(single == null){
            single = new Singleton();
        }
        return single;
    }
    /* *********************************************************************
     * 那么该如何解决呢
     * 1.在方法前面加synchronized修饰。这样肯定不会再有线程安全问题。
     *********************************************************************** */
    public static synchronized Singleton getInstance线程安全() {
        if(single == null){
            single = new Singleton();
        }
        return single;
    }
    /* ***********************************************************************
     * 但是上面方法也有缺点
     *
     * 假如有100个线程同时执行，每次去执行getInstance线程安全方法时都要先获得锁再去执
     * 行方法体，其他线程如果没有获得锁，就要等待,等待时间最长的线程白白耗费了太多时间，
     * 感觉像是变成了串行处理。
     *
     * 所以我们需要更好的解决办法
     * 2.增加同步代码块,减少锁的颗粒大小,也就是减少需要线程同步的执行次数
     *********************************************************************** */
    public static Singleton getInstance线程安全调优() {
        if(single == null){
            synchronized (Singleton.class){
                single = new Singleton();
            }
        }
        return single;
    }
    /* ***********************************************************************
     * 执行上面方法,发现又出现的线程安全问题
     * 原理如同线程不同步的方法getInstance(),当线程A判断完if(single == null)后,此时
     * CPU被线程B抢去,B也会进入if(single == null),然后两个线程都创建了新的线程.
     *
     * 我们仍旧需要解决这个问题
     * 3.双重检查.
     *   很简单,在同步代码块内再判断一次不就行了吗
     *********************************************************************** */
    public static Singleton getInstance线程安全调优双重检查() {
        if(single == null){// 1
            synchronized (Singleton.class){
                if(single == null) {
                    single = new Singleton();// 2
                }
            }
        }
        return single;
    }
    /* ***********************************************************************
     * 截止到此,我们感觉没什么问题了,线程安全问题也解决了,效率问题也解决了,感觉自己
     * 棒棒哒.我离大神又进了一步.
     * 但是，双重检查加锁并不代码百分百一定没有线程安全问题了。因为，这里会涉及到一
     * 个指令重排序问题。instance = new Singleton2()其实可以分为下面的步骤：
     * 1.申请一块内存空间；
     * 2.在这块空间里实例化对象；
     * 3.instance的引用指向这块空间地址；
     * 指令重排序存在的问题是：
     *   对于以上步骤，指令重排序很有可能不是按上面123步骤依次执行的。比如，先执行1申
     *   请一块内存空间，然后执行3步骤，instance的引用去指向刚刚申请的内存空间地址，
     *   那么，当它再去执行2步骤，判断instance时，由于instance已经指向了某一地址，它
     *   就不会再为null了，因此，也就不会实例化对象了。这就是所谓的指令重排序安全问题。
     *   那么，如何解决这个问题呢？
     * 加上volatile关键字，因为volatile可以禁止指令重排序。
     *********************************************************************** */

//    public static void main(String[] args) {
//        ExecutorService threadPool = Executors.newFixedThreadPool(20);
//        for (int i = 0; i< 20; i++) {
//            threadPool.execute(
//                    () -> System.out.println(Thread.currentThread().getName()+"  :"+Singleton.getInstance线程安全调优双重检查()));
//        }
//        threadPool.shutdown();
//    }
}
