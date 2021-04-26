package com.jasmine.sentinelzerolearning.controller;

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
        initFlowRules();
        for (int i = 1; i <= 20; i++) {
            try (Entry entry = SphU.entry("getTest")) {
                System.out.println("succ: " + i);
            } catch (BlockException e) {
                System.err.println("fail: " + i);
            }
        }
        return "over";
    }

    @GetMapping("/get/forever")
    public String testAll () throws InterruptedException {
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

    @GetMapping("/stop")
    public String stop () {
        synchronized (this) {
            stop = true;
        }
        return "死循环已停止";
    }

    private static void initFlowRules(){
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource("getTest");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setCount(15);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }
}
