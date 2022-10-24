package com.xzzz.sentinelzerolearning.controller;

import cn.hutool.core.date.DateUtil;
import com.xzzz.sentinelzerolearning.config.R;
import com.xzzz.sentinelzerolearning.config.RUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

/**
 * 持久化动态规则测试
 *
 * @author : jasmineXz
 */
@RestController
@RequestMapping("/test/dynamic/datasource")
public class DynamicDataSourceController {

    @GetMapping("/flow")
    public R dynamicFlowRule(Integer qps) {
        final String resource = "dynamic_datasource_flow";
        System.out.println(String.format("==================== %s ====================", DateUtil.now()));
        CompletableFuture<?>[] arr = new CompletableFuture[qps];
        for (int i = 0; i < qps; i++) {
            arr[i] = CompletableFuture.runAsync(new RateLimitController.Task(resource));
        }
        CompletableFuture.allOf(arr);
        return RUtil.succ("done");
    }
}
