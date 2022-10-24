package com.xzzz.sbspringboot.config.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * @author : jasmineXz
 */
@Slf4j
@Service
public class TestTaskService {

    @Async(AsyncTaskExecutorConfig.TASK_NAME)
    public CompletableFuture<Integer> invoke(int i) {
        log.trace("==> 1. 异步开始");
        CompletableFuture<Integer> future = new CompletableFuture<>();

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        future.complete(i * 10);
        log.trace("==> 2. 异步结束");
        return future;
    }
}
