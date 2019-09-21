package com.jasmine.JavaBase.线程;

public class 概念 {
    /**
     一.线程的常用方法
        1.Thread对象方法
          isAlive()       当线程处于就绪,运行,阻塞第三种状态时,返回true,处于新建,死亡两种状态时,返回false
          join()          当在某个程序执行流中调用其他线程的join方法时,调用线程将会阻塞,等调用join的线程执行完毕后才会继续
          setDeamon(true) 设置线程为守护线程
          isDeamon()      判断是否为守护线程

        2.静态方法
          sleep()         线程睡眠进入阻塞状态,即使CPU有空闲也不会调用
          yield()         线程让步,只是让当前线程暂停一下转入就绪状态,让系统的线程调度器重新调度一次,所以有可能刚调用该方法
                          ,调度器就立刻让该线程执行了起来
          setPriority()   设置线程优先级 Thread.MAX_PRIORITY / Thread.MIN_PRIORITY

        3.Condition 线程之间的调度
          Condition 实例被绑定在一个Lock对象上,一般通过Lock的newCondition方法获取
          private final Lock lock = new ReentrantLock();
          private final Condition cond = lock.newCondition();

          cond.await()     类似于隐式同步监视器(Synchronized)上的wait()方法,导致当前线程等待,直到其他线程调用该Condition
                           的signal方法或signalAll()方法来唤醒该线程.
          cond.signal()    唤醒此Lock对象上等待的单个线程,如果所有线程都在该Lock对象上等待,则会唤醒其中一个线程,选择是任
                           意性的,只有当前线程放弃对该Lock对象的锁定后,才会执行被唤醒的线程.
          cond.signalAll() 唤醒在此Lock对象上等待的所有线程,只有当前线程放弃对该Lock对象的锁定后,才会执行被唤醒的线程.


     二.线程的生命周期
        1) 新建 new关键字创建线程,为其分配内存,并初始化成员变量的值
        2) 就绪 调用start()方法后,线程处于就绪状态,虚拟机为其创建方法调用栈和程序计数器,此时并没有运行,而是随时可以运行
                的状态,至于何时运行, 取决于JVM里的线程调度器的调度
                启动线程是start()方法,而不是run()方法,直接调用run()方法,并不会启动线程,而是把run()方法当做普通方法执行
        3) 运行 如果一个获得了CUP,则线程处于运行状态.
        4) 阻塞 当发生如下情况时,线程将会进入阻塞状态
            (1) 线程调用sleep()方法主动放弃所占用的处理器资源
            (2) 调用了一个阻塞式IO方法,在该方法返回之前,该线程被阻断.
            (3) 线程试图获得一个同步监视器,但该同步监视器正准备其他线程所持有.
            (4) 线程在等待某个通知（notify）
            (5) 程序调用了线程的suspend()方法将该线程挂起,但这个方法容易导致死锁,所以应该尽量避免使用此方法.
        5) 死亡 run()或call()方法执行完成,线程正常结束
               线程抛出一个未捕获的Exception或Error
               直接调用该该线程的stop()方法来结束该线程,该方法容易导致死锁,通常不推荐使用

      2.线程的级别
          当主线程结束时,其他线程不受任何影响,并不会随之结束.一旦子线程启动起来后,它就拥有和主线程相同的地位,它不会受主线程
          的影响.


      3.同步锁 加锁 -> 修改 -> 释放锁
       1) synchronize
         (1).同步代码块 synchronize(object){} 线程开始执行下面代码时,必须先获得对同步监视器的锁定
         (2).修饰方法,同步方法的同步监视器是this,也就是调用该方法的对象.
       2) lock lock锁是当前线程对象获取到锁
          Lock lock = new ReentrantLock();

          new ReentrantLock() 默认实现是非公平锁,传入false为公平锁.非公平锁的效率理论更高,但可能有某些线程会隔很久才会执行到.

          (1). 加锁方法 :
            lock.lock()              : 尝试加锁,若无法获得锁则等待.
            lock.tryLock()           : 尝试加锁,获取不到锁则返回.
            lock.lockInterruptibly() : 获取一个可被中断的锁,中断方法为获得锁的线程Thread.interrupt().

          (2). 解锁方法
            在finally代码块中写lock.unlock();

       3) 锁的种类
        @see com.jasmine.JavaBase.线程.锁.概念
        @see com.jasmine.JavaBase.线程.锁.总结.md



    三. 多线程的使用场景
        1.  常见的浏览器,Web服务(现在写的web是中间件帮你完成了线程的控制),web处理请求,各种专用服务器(如游戏服务器).
        2.  servlet多线程.
        3.  FTP下载,多线程操作文件.
        4.  数据库用到的多线程.
        5.  分布式计算.
        6.  tomcat,tomcat内部采用多线程,上百个客户端访问同一个WEB应用,tomcat接入后就是把后续的处理扔给一个新的线程来处理,这个新的线程最后调用我们的servlet程序,比如doGet或者dpPost方法.
        7.  后台任务：如定时向大量(100W以上)的用户发送邮件；定期更新配置文件. 任务调度(如quartz),一些监控用于定期信息采集.
        8.  自动作业处理：比如定期备份日志. 定期备份数据库.
        9.  异步处理：如发微博. 记录日志.
        10. 页面异步处理：比如大批量数据的核对工作(有10万个手机号码,核对哪些是已有用户).
        11. 数据库的数据分析(待分析的数据太多),数据迁移.
        12. 多步骤的任务处理,可根据步骤特征选用不同个数和特征的线程来协作处理,多任务的分割,由一个主线程分割给多个线程完成.
        13. desktop应用开发,一个费时的计算开个线程,前台加个进度条显示.
        14. swing编程.

     !!!线程的一些额外只是!!!
      1.在Windows原理层面,CPU竞争都是线程级的.

      2.操作系统中,CPU竞争有很多种策略.
        Unix系统使用的是时间片算法,而Windows则属于抢占式的.

       *在时间片算法中,所有的进程排成一个队列.操作系统按照他们的顺序,给每个进程分配一段时间,即该进程允许运行的时间.
        如果在时间片结束时进程还在运行,则CPU将被剥夺并分配给另一个进程.如果进程在时间片结束前阻塞或结束,则CPU当即进行切换.
        调度程序所要做的就是维护一张就绪进程列表,当进程用完它的时间片后,它被移到队列的末尾.

       *所谓抢占式操作系统,在这种方式下,系统同样是把处理机分配给优先权最高的进程,使之执行.
        但在其执行期间,只要又出现了另一个其优先权更高的进程,进程调度程序就立即停止当前进程(原优先权最高的进程)的执行,
        重新将处理机分配给新到的优先权最高的进程.
        因此,在采用这种调度算法时,是每当系统中出现一个新的就绪进程i 时,就将其优先权Pi与正在执行的进程j 的优先权Pj进行比较.
        如果Pi≤Pj,原进程Pj便继续执行；但如果是Pi>Pj,则立即停止Pj的执行,做进程切换,使i 进程投入执行.
        显然,这种抢占式的优先权调度算法能更好地满足紧迫作业的要求,
        故而常用于要求比较严格的实时系统中,以及对性能要求较高的批处理和分时系统中.
     *
     */

}
