package com.xzzz.sbspringboot.config.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author : jasmineXz
 */
@Slf4j
@Configuration
public class AsyncTaskExecutorConfig {

    static final String TASK_NAME = "learningCustomTask";

    /**
     * 自定义线程池,当配置多个executor时,被@Async("id")指定使用;也被作为线程名的前缀
     */
    @Bean
    public AsyncTaskExecutor learningCustomTask() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 线程池名字
        executor.setThreadNamePrefix(TASK_NAME);
        // 核心线程数
        executor.setCorePoolSize(5);
        // 最大线程数
        executor.setMaxPoolSize(10);
        // 队列中最大的数目
        executor.setQueueCapacity(Integer.MAX_VALUE);
        // 线程空闲后的最大存活时间
        executor.setKeepAliveSeconds(60);
        // 对拒绝task的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());// 使用预定义的异常处理类
        // 自定义拒绝策略
        /*executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                //........
            }
        });*/
        //加载
        executor.initialize();
        log.trace("==========> 线程池创建完毕");
        return executor;
    }
}
