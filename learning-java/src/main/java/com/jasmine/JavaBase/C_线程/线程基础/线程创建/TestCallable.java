package com.jasmine.JavaBase.C_线程.线程基础.线程创建;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author jasmineXz
 */
public class TestCallable implements Callable<String> {
    private static final Logger log = LoggerFactory.getLogger(TestCallable.class);


    @Override
    public String call() throws Exception {
        log.info("子线程执行完毕");
        return "abc";
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> future = new FutureTask<>(new TestCallable());
        Thread thread = new Thread(future);
        thread.start();

        log.info("获取返回值:{}",future.get()); // get方法会使主线程阻塞
        log.info("主线程执行完毕");
    }
}
