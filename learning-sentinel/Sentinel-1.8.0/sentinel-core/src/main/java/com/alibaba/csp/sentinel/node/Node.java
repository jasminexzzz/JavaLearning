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
package com.alibaba.csp.sentinel.node;

import java.util.List;
import java.util.Map;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.node.metric.MetricNode;
import com.alibaba.csp.sentinel.slots.statistic.metric.DebugSupport;
import com.alibaba.csp.sentinel.util.function.Predicate;

/**
 * Holds real-time statistics for resources.
 *
 * @author qinan.qn
 * @author leyou
 * @author Eric Zhao
 */
public interface Node extends OccupySupport, DebugSupport {

    /**
     * 每分钟的请求书, 通过 + 阻塞的
     * Get incoming request per minute ({@code pass + block}).
     *
     * @return total request count per minute
     */
    long totalRequest();

    /**
     * 每分钟通过数
     * Get pass count per minute.
     *
     * @return total passed request count per minute
     * @since 1.5.0
     */
    long totalPass();

    /**
     * 每分钟完成的请求数
     * Get {@link Entry#exit()} count per minute.
     *
     * @return total completed request count per minute
     */
    long totalSuccess();

    /**
     * 每分钟被阻塞的请求总数
     * Get blocked request count per minute (totalBlockRequest).
     *
     * @return total blocked request count per minute
     */
    long blockRequest();

    /**
     * 每分钟业务异常数
     * Get exception count per minute.
     *
     * @return total business exception count per minute
     */
    long totalException();

    /**
     * 通过的请求的QPS
     * Get pass request per second.
     *
     * @return QPS of passed requests
     */
    double passQps();

    /**
     * 被阻止请求的QPS
     * Get block request per second.
     *
     * @return QPS of blocked requests
     */
    double blockQps();

    /**
     * 通过的和被阻止的请求的QPS
     * Get {@link #passQps()} + {@link #blockQps()} request per second.
     *
     * @return QPS of passed and blocked requests
     */
    double totalQps();

    /**
     * 已完成要求的QPS
     * Get {@link Entry#exit()} request per second.
     *
     * @return QPS of completed requests
     */
    double successQps();

    /**
     * Get estimated max success QPS till now.
     *
     * @return max completed QPS
     */
    double maxSuccessQps();

    /**
     * Get exception count per second.
     *
     * @return QPS of exception occurs
     */
    double exceptionQps();

    /**
     * 每秒平均响应时间
     * Get average rt per second.
     *
     * @return average response time per second
     */
    double avgRt();

    /**
     * 记录的最小响应时间
     * Get minimal response time.
     *
     * @return recorded minimal response time
     */
    double minRt();

    /**
     * 当前活动线程数
     * Get current active thread count.
     *
     * @return current active thread count
     */
    int curThreadNum();

    /**
     * Get last second block QPS.
     */
    double previousBlockQps();

    /**
     * 最后时间窗口的QPS
     * Last window QPS.
     */
    double previousPassQps();

    /**
     * Fetch all valid metric nodes of resources.
     * Fetch all valid metric nodes of resources.
     *
     * @return valid metric nodes of resources
     */
    Map<Long, MetricNode> metrics();

    /**
     * 获取满足时间谓词的所有原始度量项。
     * Fetch all raw metric items that satisfies the time predicate.
     *
     * @param timePredicate time predicate
     * @return raw metric items that satisfies the time predicate
     * @since 1.7.0
     */
    List<MetricNode> rawMetricsInMin(Predicate<Long> timePredicate);

    /**
     * 添加通过计数。
     * Add pass count.
     *
     * @param count count to add pass
     */
    void addPassRequest(int count);

    /**
     * 添加rt和成功计数。
     * Add rt and success count.
     *
     * @param rt      response time
     * @param success success count to add
     */
    void addRtAndSuccess(long rt, int success);

    /**
     * 增加阻塞QPS
     * Increase the block count.
     *
     * @param count count to add
     */
    void increaseBlockQps(int count);

    /**
     * 增加异常QPS
     * Add the biz exception count.
     *
     * @param count count to add
     */
    void increaseExceptionQps(int count);

    /**
     * 增加线程数
     * Increase current thread count.
     */
    void increaseThreadNum();

    /**
     * 减少线程数
     * Decrease current thread count.
     */
    void decreaseThreadNum();

    /**
     * Reset the internal counter. Reset is needed when {@link IntervalProperty#INTERVAL} or
     * {@link SampleCountProperty#SAMPLE_COUNT} is changed.
     */
    void reset();
}
