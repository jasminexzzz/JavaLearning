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

import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.csp.sentinel.util.TimeUtil;
import com.alibaba.csp.sentinel.node.Node;
import com.alibaba.csp.sentinel.slots.block.flow.TrafficShapingController;

/**
 * <p>
 * The principle idea comes from Guava. However, the calculation of Guava is
 * rate-based, which means that we need to translate rate to QPS.
 *
 * 主要的想法来自 Guava。但是，Guava 的计算是基于费率的，这意味着我们需要将费率转换为QPS。
 * </p>
 *
 * <p>
 * Requests arriving at the pulse may drag down long idle systems even though it
 * has a much larger handling capability in stable period. It usually happens in
 * scenarios that require extra time for initialization, e.g. DB establishes a connection,
 * connects to a remote service, and so on. That’s why we need “warm up”.
 *
 * 即使在稳定时期有更大的处理能力的系统，在长时间空闲后突然收到大量的请求，也可能拖慢系统的处理速度。
 * 它通常发生在需要额外时间初始化的场景中，例如DB建立连接，连接到远程服务，等等。这就是为什么我们需要“热身”。
 * </p>
 *
 * <p>
 * Sentinel's "warm-up" implementation is based on the Guava's algorithm.
 * However, Guava’s implementation focuses on adjusting the request interval,
 * which is similar to leaky bucket. Sentinel pays more attention to
 * controlling the count of incoming requests per second without calculating its interval,
 * which resembles token bucket algorithm.
 *
 * 哨兵的“热身”实现是基于Guava的算法。然而，Guava的实现侧重于调整请求间隔，这与漏桶类似。
 * Sentinel更注重控制每秒传入请求的数量，而不计算它的间隔，类似于令牌桶算法。
 * </p>
 *
 * <p>
 * The remaining tokens in the bucket is used to measure the system utility.
 * Suppose a system can handle b requests per second. Every second b tokens will
 * be added into the bucket until the bucket is full. And when system processes
 * a request, it takes a token from the bucket. The more tokens left in the
 * bucket, the lower the utilization of the system; when the token in the token
 * bucket is above a certain threshold, we call it in a "saturation" state.
 *
 * 桶中剩余的令牌用于度量系统实用程序。假设一个系统每秒可以处理b个请求。每秒钟就有b个令牌被添加到桶中，直到桶满为止。
 * 当系统处理一个请求时，它从桶中获取一个令牌。桶中剩余的令牌越多，系统利用率越低;
 * 当令牌桶中的令牌超过某个阈值时，我们称其为“饱和”状态。
 * </p>
 *
 * <p>
 * Base on Guava’s theory, there is a linear equation we can write this in the
 * form y = m * x + b where y (a.k.a y(x)), or qps(q)), is our expected QPS
 * given a saturated period (e.g. 3 minutes in), m is the rate of change from
 * our cold (minimum) rate to our stable (maximum) rate, x (or q) is the
 * occupied token.
 *
 * 根据 Guava 的理论, 有一个线性方程我们可以把它写成y = m * x + b, 其中y(亦或是y(x),或qps(q)),
 * 我们的预期QPS是给定一个饱和周期(例如3分钟)，m是从冷(最小)速率到稳定(最大)速率的变化率, X(或q)是被占用的令牌。
 * </p>
 *
 * @author jialiang.linjl
 */
public class WarmUpController implements TrafficShapingController {

    protected double count;
    // 负载因子,用来控制预热速度,在sentinel.properties文件中配置csp.sentinel.flow.cold.factor，默认值为3
    private int coldFactor;
    // 告警令牌数
    protected int warningToken = 0;
    // 最大令牌数, 最大速率 * 预热时间
    private int maxToken;
    // 斜率
    protected double slope;

    // 令牌桶中的数量
    protected AtomicLong storedTokens = new AtomicLong(0);
    // 上次添加令牌的时间
    protected AtomicLong lastFilledTime = new AtomicLong(0);

    public WarmUpController(double count, int warmUpPeriodInSec, int coldFactor) {
        construct(count, warmUpPeriodInSec, coldFactor);
    }

    public WarmUpController(double count, int warmUpPeriodInSec) {
        construct(count, warmUpPeriodInSec, 3);
    }

    /**
     * 构造方法中计算冷启动的各项参数
     * @param count 最大QPS
     * @param warmUpPeriodInSec 预热所需时间
     * @param coldFactor 负载因子
     */
    private void construct(double count, int warmUpPeriodInSec, int coldFactor) {

        if (coldFactor <= 1) {
            throw new IllegalArgumentException("Cold factor should be larger than 1");
        }

        this.count = count;

        this.coldFactor = coldFactor;

        // stableInterval = 1 / count
        // thresholdPermits = 0.5 * warmupPeriod / stableInterval.
        // 计算告警令牌数, (预热时长 * 最大数量) / 2
        warningToken = (int)(warmUpPeriodInSec * count) / (coldFactor - 1);

        // / maxPermits = thresholdPermits + 2 * warmupPeriod / (stableInterval + coldInterval)
        // 计算最大令牌数
        maxToken = warningToken + (int)(2 * warmUpPeriodInSec * count / (1.0 + coldFactor));

        // slope = (coldIntervalMicros - stableIntervalMicros) / (maxPermits - thresholdPermits);
        // 斜率
        slope = (coldFactor - 1.0) / count / (maxToken - warningToken);

    }


    @Override
    public boolean canPass(Node node, int acquireCount) {
        return canPass(node, acquireCount, false);
    }

    @Override
    public boolean canPass(Node node, int acquireCount, boolean prioritized) {
        // 当前时间点的QPS
        long passQps = (long) node.passQps();
        // 上个窗口的QPS
        long previousQps = (long) node.previousPassQps();

        printQps(passQps, previousQps);

        // 重新计算令牌同内的令牌数
        syncToken(previousQps);

        // 开始计算它的斜率
        // 如果进入了警戒线，开始调整他的qps
        // 获取令牌数
        long restToken = storedTokens.get();

        printTokens(restToken);

        // 令牌数大于等于警戒值,如果小于警戒值,则拒绝?
        if (restToken >= warningToken) {

            // 超过警戒值的令牌数
            long aboveToken = restToken - warningToken;
            // 消耗的速度要比warning快，但是要比慢
            // current interval = restToken*slope+1/count

            /*
             * 计算当前时间的允许的最大QPS, 令牌桶中的数量越少, 则允许通过的QPS就越大
             *
             * aboveToken * slope                         是计算令牌数对应在斜线上的y轴位置,也就是1秒生成 (aboveToken) 个令牌时,每个令牌生成的速度
             * aboveToken * slope + (1.0 / count)         是加上1秒生成 (warningToken) 个令牌时,每个令牌生成的速度
             * 1.0 / (aboveToken * slope + (1.0 / count)) 是按上述速率求出的每秒能生成的令牌数量,也就是当前时间可以通过的QPS
             * Math.nextUp(double d)                      在无穷大的方向上返回与参数相邻的数字
             *
             * 举例 count = 50, sec = 60, slope = 0.000026666
             * aboveToken = 1500, 生成令牌的速度为:个/0.059999s, warningQps = 16.6
             * aboveToken = 1300, 生成令牌的速度为:个/0.054658s, warningQps = 18.2
             * aboveToken = 1000, 生成令牌的速度为:个/0.046666s, warningQps = 21.4
             * aboveToken = 500,  生成令牌的速度为:个/0.033333s, warningQps = 30.0
             * aboveToken = 100,  生成令牌的速度为:个/0.022666s, warningQps = 44.1
             * aboveToken = 10,   生成令牌的速度为:个/0.020266s, warningQps = 49.3
             */
            double warningQps = Math.nextUp(1.0 / (aboveToken * slope + 1.0 / count));
            if (passQps + acquireCount <= warningQps) {
                return true;
            }
        } else {
            if (passQps + acquireCount <= count) {
                return true;
            }
        }

        return false;
    }

    /**
     * 同步令牌桶
     * 此步会设置令牌桶中最新的令牌数
     *
     * @param passQps 上个窗口通过的请求数
     */
    protected void syncToken(long passQps) {
        long currentTime = TimeUtil.currentTimeMillis(); // 当前时间

        // 方便打断点
        if (passQps > 5) {
            System.out.print("");
        }

        currentTime = currentTime - currentTime % 1000;  // 当前时间所在的秒级窗口起点,也就是所在秒的整秒数
        long oldLastFillTime = lastFilledTime.get();     // 上次填充日期
        // 如果当前时间小于上次填充日期,则说明机器时间出现回滚,直接返回
        if (currentTime <= oldLastFillTime) {
            return;
        }

        // 令牌桶中的当前令牌数
        long oldValue = storedTokens.get();
        // 每个时间窗口开始时, 令牌桶重新计算令牌数
        long newValue = coolDownTokens(currentTime, passQps);

        // 设置令牌桶中的数量
        if (storedTokens.compareAndSet(oldValue, newValue)) {
            /*
             * 重新计算令牌后, 将上一个时间窗口的令牌数减掉
             */
            long currentValue = storedTokens.addAndGet(0 - passQps);
            // 如果桶中的令牌不够用了,则桶令牌数设置为0,不能允许桶中的数量为负数
            if (currentValue < 0) {
                storedTokens.set(0L);
            }
            lastFilledTime.set(currentTime);
        }

    }

    /**
     * 计算令牌桶中新的令牌数
     * 桶中的令牌数不需要每次更新都来刷新,只有在令牌数小于警戒值时才刷新,或者请求数小于最大允许数的1/3时才刷新
     *
     * @param currentTime 当前时间
     * @param passQps 上个窗口通过的请求数
     * @return 新的令牌数, 最大不会超过maxToken
     */
    private long coolDownTokens(long currentTime, long passQps) {
        long oldValue = storedTokens.get();
        long newValue = oldValue;

        /*
         * 两种情况需要刷新令牌桶
         * 1: 令牌桶中剩余的令牌数已经小于警戒值: 就说明当前请求速率已经 >= 稳定速率, 所以必须刷新令牌桶来保证后续请求可以拿到令牌
         * 2: 令牌桶数量大于警戒值,并且QPS小于触发值: 则说明当前请求速率已经不足以进入预热阶段, 需要刷新令牌桶
         */
        /*
         * 当令牌桶的令牌小于警戒值的时候,就刷新令牌桶
         * 此时说明,当前请求数已经 >= 稳定速率,令牌桶中的数量需要按照稳定速率计算了
         */
        if (oldValue < warningToken) {
            /*
             * 当前桶中应该的令牌数 = 实际的令牌数 + (当前时间 - 上次添加时间) * 最大个数 / 1000
             *
             * 第一次进入: 会将令牌桶填满
             *
             * 非第一次进入:
             * currentTime - lastFilledTime.get()                 可以计算出距离上次添加令牌过去了多少毫秒
             * currentTime - lastFilledTime.get() * count         可以计算出过去这段时间应该添加的令牌个数
             * currentTime - lastFilledTime.get() * count  / 1000 可以计算出距离上次添加令牌过去了多少秒,也就是将秒转为毫秒
             * 也就是计算距离上次添加令牌,这次请求时实际令牌桶里的令牌数
             *
             */
            newValue = (long)(oldValue + (currentTime - lastFilledTime.get()) * count / 1000);
            printCoolDown(1);

        }
        /*
         * 如果令牌桶中的数量大于警戒值,则说明还不需要按照稳定速率来放行请求
         * 那么就需要计算当前QPS是否连触发速率都没有达到,如果实际速率小于触发速率,则将桶中数量重置为最大数量
         */
        else if (oldValue > warningToken) {
            // 当通过的qps小于最大数量的1/3,则计算新的令牌桶
            if (passQps < (int)count / coldFactor) {
                newValue = (long)(oldValue + (currentTime - lastFilledTime.get()) * count / 1000);
                printCoolDown(3);
            } else {
                printCoolDown(2);
            }
        }


        /*
         * 重新计算的令牌数不能大于最大令牌数
         */
        return Math.min(newValue, maxToken);
    }

    /**
     * 输出qps
     * @param qps1 passQps
     * @param qps2 上一个窗口的passQps
     */
    private void printQps(long qps1, long qps2) {
        if (false) {
            return;
        }
        System.out.print("(Q:" + fill(qps1) + "|" + fill(qps2) + ")");
    }

    private void printTokens(long token) {
        if (false) {
            return;
        }
        System.out.print("(T:" + fill(token) + ")");
    }


    private void printCoolDown(int refresh) {
        System.out.print("(C:" + refresh  + ")");
    }

    private String fill(long i) {
        String s = i + "";
        if (s.length() < 2) {
            s = "0" + s;
        }
        return s;
    }

    public static void main(String[] args) {
        // 1     : 1.0000001
        // 100   : 100.00001
        // 10000 : 10000.001
        double i = 18.295583f;
        System.out.println(Math.nextUp(i));
    }

}
