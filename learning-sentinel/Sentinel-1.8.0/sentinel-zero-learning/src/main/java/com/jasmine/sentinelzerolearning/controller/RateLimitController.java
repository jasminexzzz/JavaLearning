package com.jasmine.sentinelzerolearning.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowItem;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

/**
 * @author wangyf
 * @since 0.0.1
 */
@RestController
@RequestMapping("/test/rate")
@SuppressWarnings("all")
public class RateLimitController {


    // region 快速拒绝

    /**
     * 最普通的示例
     * @param maxQps
     * @param qps
     * @return
     */
    @GetMapping("/fastfail")
    public String fastFail(Integer maxQps, Integer qps) {
        final String resource = "resource_fastfail";
        // ============================== 初始化规则 ==============================
        /**
         * 如果在 rule 中指定了 limitApp，则需要在上下文指定 ContextUtil.enter(String name, String origin) 中的 oragin 参数
         * <code>
         *     rule.setLimitApp("limit_app_test");
         *     ContextUtil.enter(上下文, "limit_app_test)
         * <code/>
         */
        FlowRule rule = new FlowRule();
        rule.setResource(resource);
        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT); // 快速失败
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);                     // 阈值类型,QPS/线程数
        rule.setCount(15);                                          // 阈值个数,漏桶每秒放行是个数
        FlowRuleManager.loadRules(CollUtil.newArrayList(rule));			// 设置规则

        System.out.println("========================================");
        CompletableFuture<?>[] arr = new CompletableFuture[qps];
        for (int i = 0; i < qps; i++) {
            arr[i] = CompletableFuture.runAsync(new Task(resource));
        }

        CompletableFuture.allOf(arr);

        return "done";
    }

    /**
     * 持续请求, 可以观察不同的上下文中同一资源的请求情况
     * @param maxQps
     * @param qps
     * @return
     */
    @GetMapping("/fastfail/continue")
    public String fastFailContinue(Integer maxQps, Integer qps) throws InterruptedException {
        final String resource = "resource_fastfail_continue";
        // ============================== 初始化规则 ==============================
        FlowRule rule = new FlowRule();
        rule.setResource(resource);
        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT); // 快速失败
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);                     // 阈值类型,QPS/线程数
        rule.setCount(maxQps);                                          // 阈值个数,漏桶每秒放行是个数
        FlowRuleManager.loadRules(CollUtil.newArrayList(rule));			// 设置规则

        System.out.println("========================================");
        CompletableFuture<?>[] arr = new CompletableFuture[qps];

        for (int s = 0; s < 60; s++) {
            for (int i = 0; i < qps; i++) {
                Task t = new Task(resource);
                if (RandomUtil.randomBoolean()) {
                    t.setContext("context_fastfail_1");
                }
                arr[i] = CompletableFuture.runAsync(t);
            }
            System.out.println("─────────────────────────────────────────────────────────────────────────────────────");
            Thread.sleep(1000);
        }

        return "done";
    }

    /**
     * 快速失败限流方式, 包含了优先请求
     * @param maxQps 最大QPS
     * @param qps QPS
     */
    @GetMapping("/fastfail/priority")
    public String fastFailPriority(Integer maxQps, Integer qps) {
        final String resource = "resource_fastfail_priority";
        // ============================== 初始化规则 ==============================
        FlowRule rule = new FlowRule();
        rule.setResource(resource);
        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT); // 快速失败
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);                     // 阈值类型,QPS/线程数
        rule.setCount(maxQps);                                          // 阈值个数,漏桶每秒放行是个数
        FlowRuleManager.loadRules(CollUtil.newArrayList(rule));			// 设置规则

        System.out.println("========================================");
        CompletableFuture<?>[] arr = new CompletableFuture[qps];
        for (int i = 0; i < arr.length; i++) {
            Task t = new Task(resource);
            if (i > maxQps) {
                t.setType("priority");
            }
            arr[i] = CompletableFuture.runAsync(t);
        }

        CompletableFuture.allOf(arr);

        return "done";
    }


    /**
     * 模拟不同的线程的请求
     */
    static class Task extends Thread {
        private String type;     // 是否优先请求
        private String resource; // 资源名称
        private String context = "context_fastfail";  // 上下文名称

        public Task(String resource) {
            this.resource = resource;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setContext(String context) {
            this.context = context;
        }

        @Override
        public void run() {
            ContextUtil.enter(context);
            Entry entry = null;
            try {
                if ("priority".equals(type)) {
                    entry = SphU.entryWithPriority(resource);
                } else {
                    entry = SphU.entry(resource);
                }
                Thread.sleep(50);
                println(fill(Thread.currentThread().getName(),33) + ":" + context + " SUCC", "green");
            } catch (BlockException e) {
                println(fill(Thread.currentThread().getName(),33) + ">" + context + " FAIL", "red");
            } catch (InterruptedException e) {
                e.printStackTrace();
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
        CompletableFuture<?>[] arr = new CompletableFuture[qps];
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
            Thread.sleep(1000);
        }

        System.out.println("====================< over  >====================\n");
        return "over";
    }



    // endregion








    // region 热点参数限流

    /**
     * 热点参数限流
     */
    @PostMapping("/param")
    public String param() {
        final String resource = "resource_param";
        ParamFlowRule rule = new ParamFlowRule();
        rule.setResource(resource);
        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT); // 快速失败，热点限流只有快速失败和匀速排队两种
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);                     // 阈值类型,QPS/线程数
        rule.setParamIdx(0);                                            // 参数索引 entry 的 args... 参数
        rule.setCount(15);                                              // 阈值个数,漏桶每秒放行是个数

        // String 类型的参数 “1”，最大QPS：10
        ParamFlowItem p1 = new ParamFlowItem();
        p1.setClassType(String.class.getTypeName());
        p1.setObject("1");
        p1.setCount(10);

        // Integer 类型的参数 1，最大QPS：5
        ParamFlowItem p2 = new ParamFlowItem();
        p2.setClassType(Integer.class.getTypeName());
        p2.setObject("1");
        p2.setCount(5);

        rule.setParamFlowItemList(CollUtil.newArrayList(p1,p2));
        ParamFlowRuleManager.loadRules(CollUtil.newArrayList(rule));	// 设置规则

        System.out.println("========================================");
        for (int i = 0; i < 20; i++) {
            /**
             * 进入
             * @param name        资源名称
             * @param trafficType 类型
             * @param batchCount  数量
             * @param args        参数，数组类型，可以一次性传入多个参数，但对应数组的下标需要配置后才能对该参数进行限流，
             *                    设置下标的方式：{@link ParamFlowRule#setParamIdx(Integer)}
             */
            try (Entry e = SphU.entry(resource, EntryType.IN, 1,"2")) {
                println(fill(Thread.currentThread().getName(),33) + ": SUCC", "green");
            } catch (BlockException e) {
                println(fill(Thread.currentThread().getName(),33) + "> FAIL", "red");
            }
        }

        return "done";
    }

    // endregion







    // region 系统级保护

    @GetMapping("/system")
    public String system() {
        final String resource = "系统级保护";
        SystemRule rule = new SystemRule();
        rule.setResource(resource);   // 设置资源名称
        rule.setQps(50);              // 入口节点的最大QPS
        rule.setAvgRt(100);           // 平均响应时间
        rule.setMaxThread(1);         // 最大线程数
        rule.setHighestCpuUsage(0.1); // CPU使用比例
        /*
         * 设置最高负荷。负载与Linux系统负载不一样，不够敏感。要计算负载，需要同时考虑Linux系统负载、当前全局响应时间和全局QPS，
         * 这意味着我们需要与{@link #setAvgRt(long)}和{@link #setQps(double)}协调。这个参数只在类Unix机器上生效.
         */
        rule.setHighestSystemLoad(0.1);
        SystemRuleManager.loadRules(CollUtil.newArrayList(rule));

        System.out.println("========================================");
        for (int i = 0; i < 20; i++) {
            try (Entry e = SphU.entry(resource,EntryType.IN)) {
                println(fill(Thread.currentThread().getName(),33) + ": SUCC", "green");
            } catch (BlockException e) {
                println(fill(Thread.currentThread().getName(),33) + "> FAIL", "red");
            }
        }

        return "done";

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
