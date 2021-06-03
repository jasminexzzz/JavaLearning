package com.jasmine.sentinelzerolearning.controller;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
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

/**
 * @author wangyf
 * @since 0.0.1
 */
@RestController
@RequestMapping("/test")
public class TestController {

    private boolean stop = false;

    @GetMapping("/get")
    public String test () {

        /*=================================================================================
         * 初始化规则
         *=================================================================================*/
        FlowRule rule = new FlowRule();
        rule.setLimitApp("default"); // 流控针对的调用方
        rule.setResource("getTest"); // 资源名

        // 1. 直接拒绝策略
        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);  // 直接决绝
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);                      // 限流阈值类型，QPS 或线程数
        rule.setCount(15);                                               // 阈值个数




        FlowRuleManager.loadRules(CollUtil.newArrayList(rule));

        for (int i = 1; i <= 20; i++) {
            try (Entry ignored = SphU.entry("getTest")) {
                System.out.println("succ: " + i);
            } catch (BlockException e) {
                System.err.println("fail: " + i);
            }
        }
        return "over";
    }

    @GetMapping("/get/forever/defaultContext")
    public String foreverDefaultContext () throws InterruptedException {
        initFlowRules();
        int i = 1;
        for (;;) {
            if (stop) {
                System.out.println("循环已停止");
                break;
            }
            try (Entry entry = SphU.entry("forever")) {
                System.out.println("succ: " + i);
            } catch (BlockException e) {
                System.err.println("fail: " + i);
            } finally {
                i++;
                Thread.sleep(100);
            }
        }

        return "死循环已停止";
    }

    @GetMapping("/get/forever/customContext")
    public String foreverCustomContext () throws InterruptedException {
        initFlowRules();
        int i = 1;
        ContextUtil.enter("jasmine_context");
        for (;;) {
            if (stop) {
                System.out.println("循环已停止");
                break;
            }
            try (Entry entry = SphU.entry("forever")) {
                System.out.println("succ: " + i);
            } catch (BlockException e) {
                System.err.println("fail: " + i);
            } finally {
                i++;
                Thread.sleep(100);
            }
        }

        return "死循环已停止";
    }

    @GetMapping("/stop")
    public String stop () {
        synchronized (this) {
            stop = true;
        }
        return "死循环已停止";
    }

    /**
     * 初始化流控规则
     * https://sentinelguard.io/zh-cn/docs/basic-api-resource-rule.html
     */
    private static void initFlowRules(){
        List<FlowRule> rules = new ArrayList<>();

        for (int i = 0; i < 1; i++) {
            FlowRule rule = new FlowRule();
            rule.setLimitApp("default"); // 流控针对的调用方
            rule.setResource("getTest"); // 资源名
            rule.setGrade(RuleConstant.FLOW_GRADE_QPS); // 限流阈值类型，QPS 或线程数
            rule.setCount(15);

            /*
             * 流控效果（直接拒绝 / 排队等待 / 慢启动模式），不支持按调用关系限流
             * RuleConstant.CONTROL_BEHAVIOR_DEFAULT = 0;       // 直接拒绝
             * RuleConstant.CONTROL_BEHAVIOR_WARM_UP = 1;       // 慢启动
             * RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER = 2;  // 排队等待
             * RuleConstant.CONTROL_BEHAVIOR_WARM_UP_RATE_LIMITER = 3;
             */
            rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);
            rules.add(rule);
        }
        FlowRuleManager.loadRules(rules);
    }
}
