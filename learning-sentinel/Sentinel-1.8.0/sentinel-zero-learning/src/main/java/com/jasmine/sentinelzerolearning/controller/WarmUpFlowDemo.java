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
package com.jasmine.sentinelzerolearning.controller;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * When {@link FlowRule#controlBehavior} set to {@link RuleConstant#CONTROL_BEHAVIOR_WARM_UP}, real passed qps will
 * gradually increase to {@link FlowRule#count}, other than burst increasing.
 * <p/>
 * Run this demo, results are as follows:
 * <pre>
 * ...
 * 1530497805902, total:1, pass:1, block:0 // run in slow qps
 * 1530497806905, total:3, pass:3, block:0
 * 1530497807909, total:2, pass:2, block:0
 * 1530497808913, total:3, pass:3, block:0
 * 1530497809917, total:269, pass:6, block:263 // request qps burst increase, warm up behavior triggered.
 * 1530497810917, total:3676, pass:7, block:3669
 * 1530497811919, total:3734, pass:9, block:3725
 * 1530497812920, total:3692, pass:9, block:3683
 * 1530497813923, total:3642, pass:10, block:3632
 * 1530497814926, total:3685, pass:10, block:3675
 * 1530497815930, total:3671, pass:11, block:3660
 * 1530497816933, total:3660, pass:15, block:3645
 * 1530497817936, total:3681, pass:21, block:3661 // warm up process end, pass qps increased to {@link FlowRule#count}
 * 1530497818940, total:3737, pass:20, block:3716
 * 1530497819945, total:3663, pass:20, block:3643
 * 1530497820950, total:3723, pass:21, block:3702
 * 1530497821954, total:3680, pass:20, block:3660
 * ...
 * </pre>
 *
 * @author jialiang.linjl
 */
public class WarmUpFlowDemo {

    private static final String KEY = "abc";

    private static AtomicInteger pass = new AtomicInteger();
    private static AtomicInteger block = new AtomicInteger();
    private static AtomicInteger total = new AtomicInteger();

    private static volatile boolean stop = false;

    private static final int threadCount = 100;
    private static int seconds = 60 + 40;

    private static long begin;

    public static void main(String[] args) throws Exception {
        initFlowRule();
        // 触发哨兵内部初始化
        Entry entry = null;
        try {
            entry = SphU.entry(KEY);
        } catch (Exception e) {
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }

        Thread timer = new Thread(new TimerTask());
        timer.setName("sentinel-timer-task");
        timer.start();

        // first make the system run on a very low condition
        // 先启动低流量线程
        for (int i = 0; i < 3; i++) {
            Thread t = new Thread(new WarmUpTask());
            t.setName("sentinel-warmup-task");
            t.start();
        }

        // 主线程睡眠 20 秒
        Thread.sleep(20000);

        System.out.println("\n流量增多!!!\n");
        /*
         * Start more thread to simulate more qps. Since we use {@link RuleConstant.CONTROL_BEHAVIOR_WARM_UP} as
         * {@link FlowRule#controlBehavior}, real passed qps will increase to {@link FlowRule#count} in
         * {@link FlowRule#warmUpPeriodSec} seconds.
         */
        // 再启动高流量线程
        for (int i = 0; i < threadCount; i++) {
            Thread t = new Thread(new RunTask());
            t.setName("sentinel-run-task");
            t.start();
        }
    }

    private static void initFlowRule() {
        begin = System.currentTimeMillis();
        FlowRule rule1 = new FlowRule();
        rule1.setResource(KEY);
        rule1.setLimitApp("default");
        rule1.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_WARM_UP);
        rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule1.setCount(50);
        rule1.setWarmUpPeriodSec(60);

        FlowRuleManager.loadRules(CollUtil.newArrayList(rule1));
    }


    /**
     * 低流量请求
     */
    static class WarmUpTask implements Runnable {
        @Override
        public void run() {
            while (!stop) {
                Entry entry = null;
                try {
                    entry = SphU.entry(KEY);
                    // token acquired, means pass
                    pass.addAndGet(1);
                } catch (BlockException e1) {
                    block.incrementAndGet();
                } catch (Exception e2) {
                    // biz exception
                } finally {
                    total.incrementAndGet();
                    if (entry != null) {
                        entry.exit();
                    }
                }
                Random random2 = new Random();
                try {
                    TimeUnit.MILLISECONDS.sleep(random2.nextInt(1000));
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        }
    }

    /**
     * 高流量请求
     */
    static class RunTask implements Runnable {
        @Override
        public void run() {
            while (!stop) {
                Entry entry = null;
                try {
                    entry = SphU.entry(KEY);
                    pass.addAndGet(1);
                } catch (BlockException e1) {
                    block.incrementAndGet();
                } catch (Exception e2) {
                    // biz exception
                } finally {
                    total.incrementAndGet();
                    if (entry != null) {
                        entry.exit();
                    }
                }
                Random random2 = new Random();
                try {
                    TimeUnit.MILLISECONDS.sleep(random2.nextInt(50));
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        }
    }


    /**
     * 打印统计的线程,每秒输出请求数, pass数, block数
     */
    static class TimerTask implements Runnable {

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            System.out.println("\n开始数据统计!!!\n");
            long oldTotal = 0;
            long oldPass = 0;
            long oldBlock = 0;
            while (!stop) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                }

                long globalTotal = total.get();
                long oneSecondTotal = globalTotal - oldTotal;
                oldTotal = globalTotal;

                long globalPass = pass.get();
                long oneSecondPass = globalPass - oldPass;
                oldPass = globalPass;

                long globalBlock = block.get();
                long oneSecondBlock = globalBlock - oldBlock;
                oldBlock = globalBlock;

                System.out.println(
//                    TimeUtil.currentTimeMillis(),
                    (System.currentTimeMillis() - begin) / 1000 + "s"
                    + ", 总数:" + oneSecondTotal
                    + ", 通过:" + oneSecondPass
                    + ", 拒绝:" + oneSecondBlock);
                if (seconds-- <= 0) {
                    stop = true;
                }
            }

            long cost = System.currentTimeMillis() - start;
            System.out.println("time cost: " + cost + " ms");
            System.out.println("total:" + total.get() + ", pass:" + pass.get()
                + ", block:" + block.get());
            System.exit(0);
        }
    }
}
