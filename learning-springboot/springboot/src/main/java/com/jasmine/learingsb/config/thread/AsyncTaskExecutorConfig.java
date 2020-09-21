package com.jasmine.learingsb.config.thread;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author : jasmineXz
 */
@Configuration
public class AsyncTaskExecutorConfig {

    /**
     * 自定义线程池，当配置多个executor时，被@Async("id")指定使用；也被作为线程名的前缀
     */
    @Bean
    public AsyncTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("asyncExecutor");//线程池名字
        executor.setMaxPoolSize(50); //最大线程数
        executor.setCorePoolSize(3);//最小线程数
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());// 使用预定义的异常处理类
        // 自定义拒绝策略
        /*executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                //........
            }
        });*/
        return executor;
    }
}