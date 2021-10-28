package com.jasmine.sentinelzerolearning.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author wangyf
 * @since 0.0.1
 */
@RestController
@RequestMapping("/test/rate")
public class RateLimitController {

    private boolean stop = false;



    // region 快速拒绝

    /**
     * 快速失败限流方式
     * @param maxQps 最大QPS
     * @param qps QPS
     */
    @GetMapping("/fastfail")
    public String fastFail(Integer maxQps, Integer qps) {
        // ============================== 初始化规则 ==============================
        FlowRule rule = new FlowRule();
        // 流控针对的调用方
        // 如果指定了limitApp，则需要在上下文指定 ContextUtil.enter(String name, String origin) 中的oragin参数
//        rule.setLimitApp("limit_app_test");
        // 资源名，需要和SphU.entry(String name)参数相同
        rule.setResource("resource_default");

        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT); // 快速失败
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);                     // 阈值类型,QPS/线程数
        rule.setCount(maxQps);                                          // 阈值个数,漏桶每秒放行是个数
        FlowRuleManager.loadRules(CollUtil.newArrayList(rule));			// 设置规则

        System.out.println("========================================");
        CompletableFuture[] arr = new CompletableFuture[qps];
        for (int i = 0; i < arr.length; i++) {
            if (i > maxQps) {
                arr[i] = CompletableFuture.runAsync(new Task("priority"));
            } else {
                arr[i] = CompletableFuture.runAsync(new Task(""));
            }
        }

        CompletableFuture.allOf(arr);

        return "done";
    }


    static class Task extends Thread {
        private String type;

        public Task(String type) {
            this.type = type;
        }

        @Override
        public void run() {
            ContextUtil.enter("context_test1");
            Entry entry = null;
            try {
                if ("priority".equals(type)) {
                    entry = SphU.entryWithPriority("resource_default");
                } else {
                    entry = SphU.entry("resource_default");
                }
                println(Thread.currentThread().getName() + ": SUCC", "green");
            } catch (BlockException e) {
                println(Thread.currentThread().getName() + "> FAIL", "red");
            } finally {
                if (entry != null) {
                    entry.exit();
                }
            }
        }
    }

    // endregion








    // region 匀速排队


    /**
     * 匀速排队限流方式
     * @param maxQps    最大QPS
     * @param qps       QPS
     * @param blockTime 最大阻塞时间
     */
    @GetMapping("/limit")
    public String rateLimit(Integer maxQps, Integer qps, Integer blockTime) {
        // ============================== 初始化规则 ==============================
        FlowRule rule = new FlowRule();
        rule.setResource("resource_rate_limit");

        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER); // 排队阻塞
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);                          // 阈值类型,QPS/线程数
        rule.setCount(maxQps);                                               // 阈值个数,漏桶每秒放行是个数,漏桶间隔就是 1000 / count
        rule.setMaxQueueingTimeMs(blockTime);                                // 最大排队等待时间(毫秒)
        FlowRuleManager.loadRules(CollUtil.newArrayList(rule));

        ContextUtil.enter("context_test");
        System.out.println("========================================");
        CompletableFuture<Void>[] arr = new CompletableFuture[qps];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = CompletableFuture.runAsync(new LimitTask(i+1+""));
        }
        CompletableFuture.allOf(arr);
        return "done";
    }

    /**
     * 匀速排队线程
     */
    static class LimitTask extends Thread {
        private String name;

        public LimitTask(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            long last = System.currentTimeMillis();
            String num = fill(name,3);
            try (Entry ignored = SphU.entry("resource_rate_limit")) {
                println(num + "SUCC : " + (System.currentTimeMillis() - last),"green");
            } catch (BlockException e) {
                println(num + "FAIL : " + (System.currentTimeMillis() - last),"red");
            }
        }
    }

    // endregion








    // region 系统预热

    /**
     * 打印内容
     * 0s = (Q:00|00)(C:1)(T:300)1
     * 0s = Q:当前窗口的QPS|上一个窗口的QPS C
     */
    @GetMapping("/warmup")
    public String warmUp () throws InterruptedException {
        // ============================== 初始化规则 ==============================
        FlowRule rule = new FlowRule();
        rule.setResource("resource_warm_up"); // 资源名

        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_WARM_UP); // 冷启动
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);                     // 阈值类型,QPS/线程数
        rule.setCount(15);                                              // 阈值个数,允许的最大QPS
        rule.setWarmUpPeriodSec(20);                                    // 允许最大QPS所需要的时间

        FlowRuleManager.loadRules(CollUtil.newArrayList(rule));

        long begin = System.currentTimeMillis();

        System.out.println("(Q:当前窗口的QPS|上一个窗口的QPS)\n" +
                "(C:放令牌: 1:最大速度放令牌/第一次填满令牌桶; 2:不重置令牌桶; 3:补充令牌桶)\n" +
                "(T:当前令牌桶的中剩余令牌)");
        System.out.println("=====================< begin >===================");

        // 运行时间
        for (int j = 0; j < 90; j++) {
            System.out.print(fill((System.currentTimeMillis() - begin) / 1000 + "s = ",6));

            int maxI = 25;
            if (j < 60 && j > 30) {
                maxI = 3;
            }
            // 每秒请求数
            for (int i = 1; i <= maxI; i++) {
                try (Entry ignored = SphU.entry("resource_warm_up")) {
                    print(fill(i + "", 3),"green");
                } catch (BlockException e) {
                    print(fill("-",3),"red");
                }
            }
            System.out.println("");
            Thread.sleep(1000);
        }

        System.out.println("====================< over  >====================\n");
        return "over";
    }



    // endregion









    // region 工具方法


    public static String fill(String str, int len) {
        return StrUtil.fill(str, ' ', len, false);
    }

    public static void println(String context, String type) {
        if ("red".equals(type)) {
            System.out.println(String.format("\033[31;1m%s\033[0m",context));
        } else if ("green".equals(type)) {
            System.out.println(String.format("\33[32;1m%s\33[0m",context));
        }
    }

    public static void print(String context, String type) {
        if ("red".equals(type)) {
            System.out.print(String.format("\033[31;1m%s\033[0m",context));
        } else if ("green".equals(type)) {
            System.out.print(String.format("\33[32;1m%s\33[0m",context));
        }
    }

    // endregion

}
