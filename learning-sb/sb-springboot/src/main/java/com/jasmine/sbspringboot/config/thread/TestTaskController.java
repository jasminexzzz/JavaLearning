package com.jasmine.sbspringboot.config.thread;

import com.jasmine.learingsb.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author : jasmineXz
 */
@Slf4j
@RestController
@RequestMapping("/test/task")
public class TestTaskController {

    @Autowired
    private TestTaskService testTaskService;

    @GetMapping("/async/{num}")
    public R test(@PathVariable("num") int num) throws ExecutionException, InterruptedException {

        log.trace("======> 收到请求");

        CompletableFuture<Integer> f1 = testTaskService.invoke(num);
//        CompletableFuture f2 = testTaskService.invoke(num);
//        CompletableFuture f3 = testTaskService.invoke(num);

//        CompletableFuture.allOf(f1,f2,f3).join();

        f1.whenComplete((v, t) -> {
            v = v + 100;
            log.trace("==> 3. 异步回调: " + v);
        });
        return R.ok(f1.get());
    }
}
