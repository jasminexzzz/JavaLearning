/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.slots.block.flow.controller;

import com.alibaba.csp.sentinel.node.Node;
import com.alibaba.csp.sentinel.node.OccupyTimeoutProperty;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.PriorityWaitException;
import com.alibaba.csp.sentinel.slots.block.flow.TrafficShapingController;
import com.alibaba.csp.sentinel.util.TimeUtil;

/**
 * Default throttling controller (immediately reject strategy).
 *
 * @author jialiang.linjl
 * @author Eric Zhao
 */
public class DefaultController implements TrafficShapingController {

    private static final int DEFAULT_AVG_USED_TOKENS = 0;

    private double count;
    private int grade;

    /**
     * 创建该类时说明是以QPS还是线程数进行限流
     * @see com.alibaba.csp.sentinel.slots.block.flow.FlowRuleUtil#buildFlowRuleMap 构造 {@link FlowRule} 时会为规则配置
     * 对应的 {@link TrafficShapingController} 实现类, 如果规则是controlBehavior, 则配置的就是本类
     *
     * @param count 阈值
     * @param grade 类型,
     */
    public DefaultController(double count, int grade) {
        this.count = count;
        this.grade = grade;
    }

    @Override
    public boolean canPass(Node node, int acquireCount) {
        return canPass(node, acquireCount, false);
    }

    /**
     * 请求是否能通过
     * @param node 节点
     * @param acquireCount 对应指标本次增加数
     * @param prioritized 是否优先请求
     * @return 是否通过,true:通过,false:不通过
     */
    @Override
    public boolean canPass(Node node, int acquireCount, boolean prioritized) {
        int curCount = avgUsedTokens(node);

        // 如果当前节点的指标数 + 本次增加的数量 > 阈值
        OccupyTimeoutProperty.updateTimeout(1000);
        if (curCount + acquireCount > count) {
            // 如果请求时优先请求,并且限制类型是QPS,则允许请求优先处理
            if (prioritized && grade == RuleConstant.FLOW_GRADE_QPS) {
                long currentTime;
                long waitInMs;
                // 当前时间
                currentTime = TimeUtil.currentTimeMillis();
                // 获取等待的毫秒, 本线程需要休眠该毫秒数后才能继续执行, 优先请求是指不进行阻塞, 而是将该请求延后到对应时间后继续执行
                waitInMs = node.tryOccupyNext(currentTime, acquireCount, count);
                /*
                 * 延后时间也不是无线延后的, 延后时间需要小于配置的最大延迟时间, 默认是500毫秒, 也就是滑动窗口中单个窗口的长度
                 *
                 * 如果要修改延迟, 可使用 {@link OccupyTimeoutProperty#updateTimeout}, 修改范围为 0~1000毫秒
                 * 范围控制在1000ms以内是因为优先请求抢占的最多只能是下一个整秒的窗口的指标, 如果范围超出了1000ms, 则成为了占用下下个
                 * 滑动窗口的指标, 原则上是不允许这样处理的, 会造成某个请求等待时间过长, 如果多个线程都处于较长时间的睡眠中, 那么会浪费
                 * 很多CPU性能
                 *
                 * 如果要修改范围, 可使用 {@link IntervalProperty#updateInterval}, 一定需要注意的是, 修改范围将会修改所有统计节点
                 * {@link StatisticNode} 的秒级滑动窗口的长度, 如修改为2000ms, 则全局秒级滑动窗口的长度都变为了2000ms, 窗口个数仍
                 * 然是2个, 所以不建议修改该值
                 */
                if (waitInMs < OccupyTimeoutProperty.getOccupyTimeout()) {
                    /*
                     * 占用下个窗口的指标, 这个值会设置到 {@link OccupiableBucketLeapArray#borrowArray} 中
                     */
                    node.addWaitingRequest(currentTime + waitInMs, acquireCount);
                    // 设置当前统计的等待请求书
                    node.addOccupiedPass(acquireCount);
                    System.out.println(Thread.currentThread().getName() + ": 延后请求(" + waitInMs + ")");
                    sleep(waitInMs);
                    // PriorityWaitException indicates that the request will pass after waiting for {@link @waitInMs}.
                    // PriorityWaitException表示请求将在等待{@link @waitInMs}之后通过。
                    throw new PriorityWaitException(waitInMs);
                }
            }
            // 如果请求不是优先请求,或者是优先请求但限制的不是QPS,则不允许通过
            return false;
        }
        return true;
    }

    /**
     * 判断是校验QPS,还是线程数
     * @param node 节点
     * @return 返回对应节点的阈值,QPS或线程数
     */
    private int avgUsedTokens(Node node) {
        if (node == null) {
            return DEFAULT_AVG_USED_TOKENS;
        }
        return grade == RuleConstant.FLOW_GRADE_THREAD ? node.curThreadNum() : (int)(node.passQps());
    }

    private void sleep(long timeMillis) {
        try {
            Thread.sleep(timeMillis);
        } catch (InterruptedException e) {
            // Ignore.
        }
    }
}
