package com.jasmine.sentinelzerolearning.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreaker;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStateChangeObserver;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.EventObserverRegistry;
import com.alibaba.csp.sentinel.util.TimeUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

/**
 * 熔断测试
 *
 * @author wangyf
 * @since 1.2.4
 */
@RestController
@RequestMapping("/test/degrade")
public class DegradeController {

    /**
     * 慢调用比例
     * @param qps 请求数
     * @param threshold 满调用比例
     * @param count 慢调用的时间长度
     */
    @GetMapping("/averagert")
    public String averageRt(int qps, double threshold, int count) {
        DegradeRule rule = new DegradeRule();
        rule.setResource("source_degrade");
        rule.setGrade(RuleConstant.DEGRADE_GRADE_RT);
        rule.setCount(count);                           // 慢调用时间, 请求时间超过该时间就认为是满调用
        rule.setTimeWindow(5);                          // 熔断时间, 熔断该时间后将会进入半开状态, 单位秒
        rule.setMinRequestAmount(5);                    // 熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断
        rule.setStatIntervalMs(1000);                   // 统计时长（单位为 ms），如 60*1000 代表分钟级
        rule.setSlowRatioThreshold(threshold);          // 慢调用比例阈值，仅慢调用比例模式有效
        DegradeRuleManager.loadRules(CollUtil.newArrayList(rule));

        EventObserverRegistry.getInstance().addStateChangeObserver("logging",new CbStateChangeEvent());

        System.out.println("========================================");

        CompletableFuture<Void>[] arr = new CompletableFuture[qps];
        for (int i = 0; i < qps; i++) {
            if (i > qps - qps * threshold - 2) {
                arr[i] = CompletableFuture.runAsync(new AverageRtTask(i + 1 + "", count));
            } else {
                arr[i] = CompletableFuture.runAsync(new AverageRtTask(i + 1 + "", 0));
            }

        }

        CompletableFuture.allOf(arr);

        return "done";
    }

    static class AverageRtTask extends Thread {
        private String name;
        private Integer sleep;

        public AverageRtTask(String name, Integer sleep) {
            this.name = name;
            this.sleep = sleep;
        }

        @Override
        public void run() {
            try (Entry ignored = SphU.entry("source_degrade")) {
                if (sleep != 0) {
                    Thread.sleep(sleep);
                }
                println(fill(name,3) + " : SUCC","green");
            } catch (BlockException | InterruptedException e) {
                println(fill(name,3) + " : FAIL","red");
            }
        }
    }

    static class CbStateChangeEvent implements CircuitBreakerStateChangeObserver {
        /**
         * 状态变更
         * @param prevState     断路器以前的状态
         * @param newState      断路器的新状态
         * @param rule          相关的规则
         * @param snapshotValue 断路器的触发值打开(如果新状态是CLOSED或HALF_OPEN则为空)
         */
        @Override
        public void onStateChange(CircuitBreaker.State prevState, CircuitBreaker.State newState, DegradeRule rule, Double snapshotValue) {
//            if (newState == CircuitBreaker.State.OPEN) {
//                // 变换至 OPEN state 时会携带触发时的值
//                System.err.println(String.format("%s : %s -> OPEN , snapshotValue=%.2f", DateUtil.now(), prevState.name() , snapshotValue));
//            } else {
            System.err.println(String.format("%s : %s -> %s", DateUtil.now(), prevState.name(), newState.name()));
//            }
        }
    }


    /**
     * 异常比例熔断
     */
    @GetMapping("/exception/ratio")
    public String exceptionRatio() {
        DegradeRule rule = new DegradeRule();
        rule.setResource("source_degrade");
        rule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO);
        DegradeRuleManager.loadRules(CollUtil.newArrayList(rule));
        return "done";
    }


    /**
     * 异常数熔断
     */
    @GetMapping("/exception/count")
    public String a() {
        DegradeRule rule = new DegradeRule();
        rule.setResource("source_degrade");
        rule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT);
        DegradeRuleManager.loadRules(CollUtil.newArrayList(rule));
        return "done";
    }


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
}
