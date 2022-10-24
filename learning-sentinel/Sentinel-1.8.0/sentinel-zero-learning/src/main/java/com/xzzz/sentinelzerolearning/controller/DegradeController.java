package com.xzzz.sentinelzerolearning.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreaker;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStateChangeObserver;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.EventObserverRegistry;
import org.springframework.core.annotation.Order;
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
@Order
@RestController
@RequestMapping("/test/degrade")
public class DegradeController {


    // region 慢调用比例

    /**
     * 慢调用比例
     * @param qps 请求数
     * @param slowRatio 慢调用比例
     * @param slowRt 慢调用的时间长度
     */
    @GetMapping("/averagert")
    public String averageRt(int qps, double slowRatio, int slowRt) {
        DegradeRule rule = new DegradeRule();
        rule.setResource("source_degrade_慢比例");
        rule.setGrade(RuleConstant.DEGRADE_GRADE_RT);   // 慢调用比例熔断
        rule.setCount(slowRt);                          // 慢调用时间, 请求时间超过该时间就认为是满调用
        rule.setTimeWindow(5);                          // 熔断时间, 熔断该时间后将会进入半开状态, 单位秒
        rule.setMinRequestAmount(5);                    // 熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断
        rule.setStatIntervalMs(1000);                   // 统计时长（单位为 ms），如 60*1000 代表分钟级
        rule.setSlowRatioThreshold(slowRatio);          // 慢调用比例阈值，仅慢调用比例模式有效
        DegradeRuleManager.loadRules(CollUtil.newArrayList(rule));

        EventObserverRegistry.getInstance().addStateChangeObserver("logging",new CbStateChangeEvent());

        System.out.println("========================================");

        CompletableFuture<Void>[] arr = new CompletableFuture[qps];
        for (int i = 0; i < qps; i++) {
            if (i > qps - qps * slowRatio - 2) {
                arr[i] = CompletableFuture.runAsync(new AverageRtTask(i + 1 + "", slowRt));
            } else {
                arr[i] = CompletableFuture.runAsync(new AverageRtTask(i + 1 + "", 0));
            }

        }

        CompletableFuture.allOf(arr);

        return "done";
    }

    /**
     * 请求线程
     */
    static class AverageRtTask extends Thread {
        private String name;
        private Integer sleep;

        public AverageRtTask(String name, Integer sleep) {
            this.name = name;
            this.sleep = sleep;
        }

        @Override
        public void run() {
            try (Entry ignored = SphU.entry("source_degrade_慢比例")) {
                if (sleep != 0) {
                    Thread.sleep(sleep);
                }
                println(fill(name,3) + " : SUCC","green");
            } catch (BlockException | InterruptedException e) {
                if (!BlockException.isBlockException(e)) {
                    Tracer.trace(e);
                }
                println(fill(name,3) + " : FAIL","red");
            }
        }
    }

    /**
     * 状态发生变化
     */
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
            System.err.println(
                String.format("熔断状态变化: %s : %s -> %s",
                    DateUtil.now(),
                    fill(prevState.name(),9),
                    fill(newState.name(),9)
                )
            );
        }
    }


    // endregion


    // region 异常比例熔断


    /**
     * 异常比例熔断
     */
    @GetMapping("/exception/ratio")
    public String exceptionRatio(int qps, double errorRatio) {
        DegradeRule rule = new DegradeRule();
        rule.setResource("source_degrade_异常比例");
        rule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO);   // 异常比例
        rule.setCount(errorRatio);                      // 异常数
        rule.setTimeWindow(5);                          // 熔断时间, 熔断该时间后将会进入半开状态, 单位秒
        rule.setMinRequestAmount(5);                    // 熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断
        rule.setStatIntervalMs(1000);                   // 统计时长（单位为 ms），如 60*1000 代表分钟级
        DegradeRuleManager.loadRules(CollUtil.newArrayList(rule));
        EventObserverRegistry.getInstance().addStateChangeObserver("logging",new CbStateChangeEvent());
        System.out.println("========================================");

        CompletableFuture<Void>[] arr = new CompletableFuture[qps];
        for (int i = 0; i < qps; i++) {
            if (i > qps - qps * errorRatio - 2) {
                arr[i] = CompletableFuture.runAsync(new ExceptionRatioTask(i + 1 + "", true));
            } else {
                arr[i] = CompletableFuture.runAsync(new ExceptionRatioTask(i + 1 + "", false));
            }
        }
        CompletableFuture.allOf(arr);
        return "done";
    }

    /**
     * 请求线程
     */
    static class ExceptionRatioTask extends Thread {
        private String name;
        private Boolean error;

        public ExceptionRatioTask(String name, Boolean error) {
            this.name = name;
            this.error = error;
        }

        @Override
        public void run() {
            Entry entry = null;
            try {
                entry = SphU.entry("source_degrade_异常比例");
                if (error) {
                    throw new BusinessException("系统发生异常");
                }
                println(fill(name,3) + " : SUCC","green");
            } catch (Throwable e) {
                // 如果异常不为BlockException, 则认为是业务异常, 需要记录业务异常数, 并以此作为熔断异常依据
                if (!BlockException.isBlockException(e)) {
                    Tracer.trace(e);
                }
                println(fill(name,3) + " : FAIL " + e.getMessage(),"red");
            } finally {
                if (entry != null) {
                    entry.exit();
                }
            }
        }
    }

    /**
     * 定义业务异常
     */
    static class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }


    // endregion


    // region 异常数熔断


    /**
     * 异常数熔断
     */
    @GetMapping("/exception/count")
    @SentinelResource
    public String exceptionCount(int qps, double errorCount) {
        DegradeRule rule = new DegradeRule();
        rule.setResource("source_degrade_异常数");
        rule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT);   // 异常数
        rule.setCount(errorCount);                      // 异常数, 异常数超过该值就熔断
        rule.setTimeWindow(5);                          // 熔断时间, 熔断该时间后将会进入半开状态, 单位秒
        rule.setMinRequestAmount(5);                    // 熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断
        rule.setStatIntervalMs(1000);                   // 统计时长（单位为 ms），如 60*1000 代表分钟级
        DegradeRuleManager.loadRules(CollUtil.newArrayList(rule));
        EventObserverRegistry.getInstance().addStateChangeObserver("logging",new CbStateChangeEvent());
        System.out.println("========================================");

        CompletableFuture<Void>[] arr = new CompletableFuture[qps];
        for (int i = 0; i < qps; i++) {
            if (i >= errorCount - 1) {
                arr[i] = CompletableFuture.runAsync(new ExceptionCountTask(i + 1 + "", true));
            } else {
                arr[i] = CompletableFuture.runAsync(new ExceptionCountTask(i + 1 + "", false));
            }
        }
        CompletableFuture.allOf(arr);
        return "done";
    }

    static class ExceptionCountTask extends Thread {
        private String name;
        private Boolean error;

        public ExceptionCountTask(String name, Boolean error) {
            this.name = name;
            this.error = error;
        }

        @Override
        public void run() {
            Entry entry = null;
            try {
                entry = SphU.entry("source_degrade_异常数");
                if (error) {
                    throw new BusinessException("系统发生异常");
                }
                println(fill(name,3) + " : SUCC","green");
            } catch (Throwable e) {
                // 如果异常不为BlockException, 则认为是业务异常, 需要记录业务异常数, 并以此作为熔断异常依据
                if (!BlockException.isBlockException(e)) {
                    Tracer.trace(e);
                }
                println(fill(name,3) + " : FAIL " + e.getMessage(),"red");
            } finally {
                if (entry != null) {
                    entry.exit();
                }
            }
        }
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
