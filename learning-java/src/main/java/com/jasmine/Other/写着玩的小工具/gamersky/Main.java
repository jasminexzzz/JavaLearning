package com.jasmine.Other.写着玩的小工具.gamersky;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {

    static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            5,
            10,
            200,
            TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(3)
    );


    public static void main(String[] args) {
        executor.execute(new DownLoadTask());
        executor.execute(new DownLoadTask());
        executor.execute(new DownLoadTask());
//        Thread t1 = new Thread(new DownLoadTask(),"download-task-1");// 下载线程1
//        t1.start();

        String url = GSUtil.urlResolver();
        System.out.println("请求地址: " + url);
        GSRequest request = GSUtil.requestResolver();
        System.out.println("请求参数: " + request);

        GSReadPage reader = new GSReadPage(url,request);
        reader.read();
    }
}

class DownLoadTask extends GSDownQueue implements Runnable {

    @Override
    public void run() {
        System.out.println("线程启动");
        // 队列消费无限循环
        while (true) {
            take();
        }
    }
}

