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

import com.alibaba.csp.sentinel.slots.block.flow.TrafficShapingController;

import com.alibaba.csp.sentinel.util.TimeUtil;
import com.alibaba.csp.sentinel.node.Node;

/**
 * @author jialiang.linjl
 */
public class RateLimiterController implements TrafficShapingController {

    // 最大排队时间
    private final int maxQueueingTimeMs;
    // 每秒放行的请求数
    private final double count;

    private final AtomicLong latestPassedTime = new AtomicLong(-1);

    public RateLimiterController(int timeOut, double count) {
        this.maxQueueingTimeMs = timeOut;
        this.count = count;
    }

    @Override
    public boolean canPass(Node node, int acquireCount) {
        return canPass(node, acquireCount, false);
    }

    @Override
    public boolean canPass(Node node, int acquireCount, boolean prioritized) {
        // Pass when acquire count is less or equal than 0.
        if (acquireCount <= 0) {
            return true;
        }
        // Reject when count is less or equal than 0.
        // Otherwise,the costTime will be max of long and waitTime will overflow in some cases.
        if (count <= 0) {
            return false;
        }

        // 当前请求时间
        long currentTime = TimeUtil.currentTimeMillis();

        // Calculate the interval between every two requests.
        /*
         * 每个请求之间的间隔时长, 1秒/每秒通过的次数
         * 如每秒允许通过 5个,则 costTime = 1/ 5 * 1000 = 200 ms
         * 如每秒允许通过10个,则 costTime = 1/10 * 1000 = 100 ms
         */
        long costTime = Math.round(1.0 * (acquireCount) / count * 1000);

        // Expected pass time of this request.
        // 本次请求预计的通过时间 = 请求间隔 + 最后一次通过时间
        long expectedTime = costTime + latestPassedTime.get();

        /*
         * 如果预计通过时间 <= 当前请求时间, 则请求可以直接通过
         * 并将最后一次通过时间设置为当前请求时间
         */
        if (expectedTime <= currentTime) {
            // Contention may exist here, but it's okay.
            latestPassedTime.set(currentTime);
            return true;

        } else {

            // Calculate the time to wait.
            /*
             * 计算等待时间 [第一次]
             * 等待时间 = 下次通过时间 - 当前时间
             */
            long waitTime = costTime + latestPassedTime.get() - TimeUtil.currentTimeMillis();

            // 如果等待的时间大于排队允许的最大等待时间,则该请求不允许通过
            if (waitTime > maxQueueingTimeMs) {
                return false;
            } else {

                /*
                 * 获取 [本线程的放行时间]
                 * 可能存在多个线程竞争设置下次时间, 所以该线程所允许的下个放行时间可能并不是递延的下一个, 而是排队排到了很多个之后,那他
                 * 的等待时间可能会变长
                 */
                long oldTime = latestPassedTime.addAndGet(costTime);
                try {

                    /*
                     * 计算等待时间 [第二次]
                     * 等待时间 = 本线程的放行时间 - 当前时间
                     */
                    waitTime = oldTime - TimeUtil.currentTimeMillis();

                    /*
                     * 由于上述等待时间变长的原因, 所以仍然需要判断延长后的等待时间是否超过了所允许的最大等待时间, 如果超过, 则要重置掉
                     * 安排给本线程的放行时间, 已便将放行时间安排给其他线程
                     */
                    if (waitTime > maxQueueingTimeMs) {
                        latestPassedTime.addAndGet(-costTime);
                        return false;
                    }
                    // in race condition waitTime may <= 0
                    if (waitTime > 0) {
                        Thread.sleep(waitTime);
                    }
                    return true;
                }
                catch (InterruptedException e) {
                }
            }
        }
        return false;
    }
}
