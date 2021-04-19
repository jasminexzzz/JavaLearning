package com.jasmine.springbootsentinel.controller;

import com.alibaba.csp.sentinel.SphU;
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

    @GetMapping("/get")
    public String test () {
        initFlowRules();
        for (int i = 0; i < 20; i++) {
            try {
                SphU.entry("getTest");
                System.out.println("succ: " + i);
            } catch (BlockException e) {
                System.err.println("fail: " + i);
            }
        }
        return "over";
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
