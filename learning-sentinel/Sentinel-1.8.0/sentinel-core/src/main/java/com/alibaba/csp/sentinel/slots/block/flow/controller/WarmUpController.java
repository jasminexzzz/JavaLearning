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
    // 最大令牌数
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

    private void construct(double count, int warmUpPeriodInSec, int coldFactor) {

        if (coldFactor <= 1) {
            throw new IllegalArgumentException("Cold factor should be larger than 1");
        }

        this.count = count;

        this.coldFactor = coldFactor;

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
        long passQps = (long) node.passQps();

        long previousQps = (long) node.previousPassQps();
        // 重新计算令牌同内的令牌数
        syncToken(previousQps);

        // 开始计算它的斜率
        // 如果进入了警戒线，开始调整他的qps
        // 获取令牌数
        long restToken = storedTokens.get();
        // 令牌数大于等于警戒值,如果小于警戒值,则拒绝?
        if (restToken >= warningToken) {
            // 超过的个数
            long aboveToken = restToken - warningToken;
            // 消耗的速度要比warning快，但是要比慢
            // current interval = restToken*slope+1/count
            // 计算当前时间的最大qps
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
     * @param passQps 通过的请求数量
     */
    protected void syncToken(long passQps) {
        long currentTime = TimeUtil.currentTimeMillis(); // 当前时间
        currentTime = currentTime - currentTime % 1000; // 当前时间所在的秒级窗口起点,也就是所在秒的整秒数
        long oldLastFillTime = lastFilledTime.get(); // 上次填充日期
        if (currentTime <= oldLastFillTime) {
            return;
        }

        // 桶中的旧令牌数
        long oldValue = storedTokens.get();
        // 桶中的新令牌数
        long newValue = coolDownTokens(currentTime, passQps);

        if (storedTokens.compareAndSet(oldValue, newValue)) {
            long currentValue = storedTokens.addAndGet(0 - passQps); // 从令牌桶中拿掉通过的数量
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
     * @param passQps 通过的请求数量
     * @return 新的令牌数
     */
    private long coolDownTokens(long currentTime, long passQps) {
        long oldValue = storedTokens.get();
        long newValue = oldValue;

        // 添加令牌的判断前提条件:
        // 当令牌的消耗程度远远低于警戒线的时候
        // 当令牌桶的令牌小于警戒值的时候,就刷新令牌桶
        if (oldValue < warningToken) {
            // 旧的token数 + (当前时间 - 上次添加时间) * 最大个数 / 1000
            /*
             * currentTime - lastFilledTime.get() 可以计算出距离上次添加令牌过去了多少毫秒
             * / 1000 可以计算出距离上次添加令牌过去了多少秒,也就是将秒转为毫秒
             * * count 可以计算出过去这段时间应该添加的令牌个数
             * 也就是计算距离上次添加令牌,这次请求时实际令牌桶里的令牌数
             *
             * 第一次进入: 会将令牌桶填满
             */
            newValue = (long)(oldValue + (currentTime - lastFilledTime.get()) * count / 1000);
        } else if (oldValue > warningToken) {
            // 当通过的qps小于最大数量的1/3,则计算新的令牌桶
            if (passQps < (int)count / coldFactor) {
                newValue = (long)(oldValue + (currentTime - lastFilledTime.get()) * count / 1000);
            }
        }
        // 返回较小的那个
        return Math.min(newValue, maxToken);
    }

    public static void main(String[] args) {
        // 1     : 1.0000001
        // 100   : 100.00001
        // 10000 : 10000.001
        int i = 10000;
        System.out.println(Math.nextUp(i));
    }

}
