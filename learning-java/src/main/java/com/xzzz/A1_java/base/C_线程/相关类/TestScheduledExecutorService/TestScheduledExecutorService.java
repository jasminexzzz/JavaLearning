package com.xzzz.A1_java.base.C_线程.相关类.TestScheduledExecutorService;

import cn.hutool.core.util.RandomUtil;

import java.time.LocalTime;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 定时任务
 *
 * @author : jasmineXz
 */
public class TestScheduledExecutorService {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        scheduleAtFixedRate();
    }


    /**
     * 1
     * ====================================================================================================
     * 次数: 永远
     * 延迟: 参数2 initialDelay 配置首次延迟, 之后间隔为 period
     * 阻塞: 调用 ScheduledFuture.get() 主线程阻塞
     * 返回: 无
     * ====================================================================================================
     *
     * 与 scheduleWithFixedDelay 的区别
     * scheduleAtFixedRate:    固定频率是相对于任务开始时间
     * scheduleWithFixedDelay: 固定评率是相对于任务结束时间
     */
    private static void scheduleAtFixedRate() throws ExecutionException, InterruptedException {
        final int[] a = {0};
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);

        /**
         * @param command 要执行的任务
         * @param initialDelay 首次执行的延迟时间
         * @param period 连续执行之间的周期
         * @param unit initialDelay和period参数的时间单位
         * @return 一个ScheduledFuture代表待完成的任务，其 get()方法将在取消时抛出异常
         * @throws RejectedExecutionException 如果任务无法安排执行
         * @throws NullPointerException 如果命令为空
         * @throws IllegalArgumentException 如果period小于或等于零
         */
        executorService.scheduleAtFixedRate(new TestTask("task-1"), 1000, 1000, TimeUnit.MILLISECONDS);
        executorService.scheduleAtFixedRate(new TestTask("task-2"), 1000, 1000, TimeUnit.MILLISECONDS);
        executorService.scheduleAtFixedRate(new TestTask("task-3"), 1000, 1000, TimeUnit.MILLISECONDS);
        executorService.scheduleAtFixedRate(new TestTask("task-4"), 1000, 1000, TimeUnit.MILLISECONDS);
        executorService.scheduleAtFixedRate(new TestTask("task-5"), 1000, 1000, TimeUnit.MILLISECONDS);
        executorService.scheduleAtFixedRate(new TestTask("task-6"), 1000, 1000, TimeUnit.MILLISECONDS);
        System.out.println("b: " + LocalTime.now());
//        System.out.println(future.get() + "");
        System.out.println("e");
    }

    static class TestTask extends Thread {

        private String name;

        TestTask(String name) {
            this.name = name;
        }

        final int[] a = {0};
        @Override
        public void run() {
            try {
                Thread.sleep(RandomUtil.randomInt(1,4));
                a[0]++;
                System.out.println(this.name + " " + Thread.currentThread().getName() + " : " + a[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 2
     * ====================================================================================================
     * 次数: 永远
     * 延迟: 参数2 initialDelay 配置首次延迟, 之后间隔为 任务执行时间 + delay
     * 阻塞: 调用 ScheduledFuture.get() 主线程阻塞
     * 返回: 无
     * ====================================================================================================
     *
     * 与 scheduleAtFixedRate 的区别
     * scheduleAtFixedRate:    固定频率是相对于任务开始时间
     * scheduleWithFixedDelay: 固定评率是相对于任务结束时间
     */
    private static void scheduleWithFixedDelay () throws ExecutionException, InterruptedException {
        final int[] a = {0};
        ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(5);
        /**
         * @param command 要执行的任务
         * @param initialDelay 首次执行的延迟时间
         * @param delay 一次执行终止和下一次执行开始之间的延迟
         * @param unit initialDelay和delay参数的时间单位
         * @return 表示挂起任务完成的ScheduledFuture，并且其get()方法在取消后将抛出异常
         * @throws RejectedExecutionException 如果任务不能安排执行
         * @throws NullPointerException 如果command为null
         * @throws IllegalArgumentException 如果delay小于等于0
         */
        ScheduledFuture<?> future = scheduled.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    a[0]++;
                    System.out.println(a[0] + " : " + LocalTime.now());
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1000L, 1000L, TimeUnit.MILLISECONDS);
        System.out.println("b : " + LocalTime.now());
        System.out.println(future.get() + "");
        System.out.println("e");
    }


    /**
     * 3
     * ====================================================================================================
     * 次数: 1次
     * 延迟: 参数2 delay 配置延迟
     * 阻塞: 调用 ScheduledFuture.get() 主线程阻塞
     * 返回: 永远为 Null
     * ====================================================================================================
     */
    private static void scheduleRunnable () throws ExecutionException, InterruptedException {
        ScheduledExecutorService scheduled = Executors.newSingleThreadScheduledExecutor();

        /**
         * 创建并执行在给定延迟后启用的一次性操作
         *
         * @param command 要执行的任务
         * @param delay 从现在开始延迟执行的时间
         * @param unit 延时参数的时间单位
         * @return 表示任务等待完成，并且其的ScheduledFuture get()方法将返回 null
         * @throws RejectedExecutionException 如果任务无法安排执行
         * @throws NullPointerException 如果命令为空
         */
        ScheduledFuture<?> future = scheduled.schedule(() -> {
            try {
                System.out.println("开始执行任务");
                TimeUnit.SECONDS.sleep(3);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("执行完毕");
        }, 1000, TimeUnit.MILLISECONDS);
        System.out.println("阻塞开始");
        System.out.println(future.get() + "");
        System.out.println("阻塞结束");
    }


    /**
     * 4
     * ====================================================================================================
     * 次数: 1次
     * 延迟: 参数2 delay 配置延迟
     * 阻塞: 调用 ScheduledFuture.get() 主线程阻塞
     * 返回: 有返回值
     * ====================================================================================================
     */
    private static void scheduleCallable () throws ExecutionException, InterruptedException {
        ScheduledExecutorService scheduled = Executors.newSingleThreadScheduledExecutor();
        /**
         * 创建并执行在给定延迟后启用的ScheduledFuture
         *
         * @param callable 执行的功能
         * @param delay 从现在开始延迟执行的时间
         * @param unit 延迟参数的时间单位
         * @param <V> the 可调用结果的类型
         * @return 一个可用于提取结果或取消的ScheduledFuture
         * @throws RejectedExecutionException 如果该任务无法安排执行
         * @throws NullPointerException 如果callable为空
         */
        ScheduledFuture<String> future = scheduled.schedule(() -> {
            try {
                System.out.println("开始执行任务");
                TimeUnit.SECONDS.sleep(3);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("执行完毕");
            return "success";
        }, 1000, TimeUnit.MILLISECONDS);
        System.out.println("阻塞开始");
        System.out.println(future.get() + "");
        System.out.println("阻塞结束");
    }


    private static void shutdown () {
        final AtomicInteger count = new AtomicInteger(0);
//        final CountDownLatch countDownLatch = new CountDownLatch(1);
        ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1, new PrintThreadFactory("testTask-1"));

        Runnable runnable = () -> {
            System.out.println("开始计数: ");
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + " num " + count.getAndIncrement());
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        schedule.scheduleAtFixedRate(runnable, 0L, 2L, TimeUnit.SECONDS);

        try {
//            countDownLatch.await();
//            schedule.shutdown();  //平滑停止线程，不处理新任务，完成正在执行的任务
            schedule.shutdownNow();  // 尝试强制停止线程，让终止的线程去设置休眠会抛出异常

//            if (schedule.isShutdown()) {
//                System.out.println("计划关闭");
//            }
            if (schedule.awaitTermination(100L, TimeUnit.SECONDS)) {
                System.out.println("termination");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class PrintThreadFactory implements ThreadFactory {

    private String name;

    PrintThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, name);
    }
}
