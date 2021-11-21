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
package com.alibaba.csp.sentinel.slots.block.flow.param;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.csp.sentinel.cluster.ClusterStateManager;
import com.alibaba.csp.sentinel.cluster.TokenResult;
import com.alibaba.csp.sentinel.cluster.TokenResultStatus;
import com.alibaba.csp.sentinel.cluster.TokenService;
import com.alibaba.csp.sentinel.cluster.client.TokenClientProvider;
import com.alibaba.csp.sentinel.cluster.server.EmbeddedClusterTokenServerProvider;
import com.alibaba.csp.sentinel.log.RecordLog;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.statistic.cache.CacheMap;
import com.alibaba.csp.sentinel.util.TimeUtil;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;

/**
 * Rule checker for parameter flow control.
 *
 * @author Eric Zhao
 * @since 0.2.0
 */
public final class ParamFlowChecker {

    public static boolean passCheck(ResourceWrapper resourceWrapper, /*@Valid*/ ParamFlowRule rule, /*@Valid*/ int count,
                             Object... args) {
        if (args == null) {
            return true;
        }

        // 下标位置不能大于等于数组长度，否则会越界
        int paramIdx = rule.getParamIdx();
        if (args.length <= paramIdx) {
            return true;
        }

        // Get parameter value.
        Object value = args[paramIdx];

        // Assign value with the result of paramFlowKey method
        if (value instanceof ParamFlowArgument) {
            value = ((ParamFlowArgument) value).paramFlowKey();
        }
        // If value is null, then pass
        if (value == null) {
            return true;
        }
        // 集群和单机不同校验
        if (rule.isClusterMode() && rule.getGrade() == RuleConstant.FLOW_GRADE_QPS) {
            return passClusterCheck(resourceWrapper, rule, count, value);
        }

        return passLocalCheck(resourceWrapper, rule, count, value);
    }

    /**
     * 本地校验
     * 1. 如果参数是集合, 则校验集合中的每一个元素， 只要有一个不通过，本次请求就不允许通过
     * 2. 如果参数是数组, 则校验数组中的每一个元素， 只要有一个不通过，本次请求就不允许通过
     * 3. 如果参数单独配置了阈值，则使用单独配置的，否则使用全局的阈值
     * 4. 全局限流中，只要参数的个数大于等于下标，就对该参数限流，无论这个参数值是什么
     *      比如说下标1的参数，全局限流是10qps，那么不管下标1中的参数是[1,"A","1","任何值",UserDTO],等等任何类型的对象，只要1秒超过
     *      10个，则这个参数的请求就会被限流,但由于参数太多了，如果允许无限保存，那么内存就被用完了，所以保存参数、参数的请求时间等数值
     *      的对象是{@link ConcurrentLinkedHashMap}，这个是谷歌实现的线程安全的带有LRU淘汰规则的Map，根据名字也可以看出他是淘汰最近
     *      最少使用的对象的，这样防止了占用太多内存。
     *
     * @param resourceWrapper
     * @param rule
     * @param count
     * @param value
     * @return
     */
    private static boolean passLocalCheck(ResourceWrapper resourceWrapper, ParamFlowRule rule, int count,
                                          Object value) {
        try {
            if (Collection.class.isAssignableFrom(value.getClass())) {
                for (Object param : ((Collection)value)) {
                    if (!passSingleValueCheck(resourceWrapper, rule, count, param)) {
                        return false;
                    }
                }
            }

            else if (value.getClass().isArray()) {
                int length = Array.getLength(value);
                for (int i = 0; i < length; i++) {
                    Object param = Array.get(value, i);
                    if (!passSingleValueCheck(resourceWrapper, rule, count, param)) {
                        return false;
                    }
                }
            } else {
                return passSingleValueCheck(resourceWrapper, rule, count, value);
            }
        } catch (Throwable e) {
            RecordLog.warn("[ParamFlowChecker] Unexpected error", e);
        }

        return true;
    }

    /**
     * 校验单个参数
     * 两种校验方式
     * 1. qps,qps也分为两种
     *      1.1 快速拒绝 {@link this#passDefaultLocalCheck}, 令牌桶
     *      1.2 匀速排队 {@link this#passThrottleLocalCheck}， 漏桶
     * 2. 线程数
     *
     * @param resourceWrapper 资源名称
     * @param rule 规则
     * @param acquireCount 通过数
     * @param value 请求的参数
     * @return 是否通过
     */
    static boolean passSingleValueCheck(ResourceWrapper resourceWrapper, ParamFlowRule rule, int acquireCount,
                                        Object value) {
        // QPS方式校验
        if (rule.getGrade() == RuleConstant.FLOW_GRADE_QPS) {
            // 如果是匀速排队
            if (rule.getControlBehavior() == RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER) {
                return passThrottleLocalCheck(resourceWrapper, rule, acquireCount, value);
            } else {
                return passDefaultLocalCheck(resourceWrapper, rule, acquireCount, value);
            }
        }

        // 线程数校验
        // 线程数就是简单校验线程数即可
        else if (rule.getGrade() == RuleConstant.FLOW_GRADE_THREAD) {
            Set<Object> exclusionItems = rule.getParsedHotItems().keySet();
            long threadCount = getParameterMetric(resourceWrapper).getThreadCount(rule.getParamIdx(), value);
            if (exclusionItems.contains(value)) {
                int itemThreshold = rule.getParsedHotItems().get(value);
                return ++threadCount <= itemThreshold;
            }
            long threshold = (long)rule.getCount();
            return ++threadCount <= threshold;
        }

        return true;
    }

    /**
     * 一个简单的令牌桶实现, 作为快速拒绝限流方式
     * 通过 {@link ParameterMetric#getRuleTokenCounter } 记录当前规则中每个参数的剩余令牌数
     * 通过 {@link ParameterMetric#getRuleTimeCounter }  记录当前规则中每个参数的上次访问时间
     *
     * @param resourceWrapper 资源
     * @param rule 规则
     * @param acquireCount 请求数
     * @param value 参数
     * @return 是否通过
     */
    static boolean passDefaultLocalCheck(ResourceWrapper resourceWrapper, ParamFlowRule rule, int acquireCount,
                                         Object value) {
        // 获取该资源的热点参数度量
        ParameterMetric metric = getParameterMetric(resourceWrapper);
        CacheMap<Object, AtomicLong> tokenCounters = metric == null ? null : metric.getRuleTokenCounter(rule);
        CacheMap<Object, AtomicLong> timeCounters = metric == null ? null : metric.getRuleTimeCounter(rule);

        if (tokenCounters == null || timeCounters == null) {
            return true;
        }

        // Calculate max token count (threshold)
        Set<Object> exclusionItems = rule.getParsedHotItems().keySet();
        // tokenCount 限流的阈值, 如果参数有特别指定, 则使用特别指定的阈值
        long tokenCount = (long)rule.getCount();
        if (exclusionItems.contains(value)) {
            tokenCount = rule.getParsedHotItems().get(value);
        }
        // 阈值为0则不允许任何请求通过
        if (tokenCount == 0) {
            return false;
        }

        long maxCount = tokenCount + rule.getBurstCount();
        if (acquireCount > maxCount) {
            return false;
        }

        // 开始自旋设置各项值
        while (true) {
            long currentTime = TimeUtil.currentTimeMillis();
            // 如果这个参数没有上次添加时间, 则添加, 否则返回上次添加时间
            AtomicLong lastAddTokenTime = timeCounters.putIfAbsent(value, new AtomicLong(currentTime));
            if (lastAddTokenTime == null) {
                // Token never added, just replenish the tokens and consume {@code acquireCount} immediately.
                // 如果这个热点参数没有传入过, 则说明是第一此传入此参数, 那么直接记录该参数的可用剩余次数即可
                tokenCounters.putIfAbsent(value, new AtomicLong(maxCount - acquireCount));
                return true;
            }

            // Calculate the time duration since last token was added.
            // 计算自上次添加令牌以来的持续时间。
            long passTime = currentTime - lastAddTokenTime.get();
            // A simplified token bucket algorithm that will replenish the tokens only when statistic window has passed.
            // 一个简化的令牌桶算法，只在统计窗口通过时才补充令牌。
            // 距离上次通过过了多久, 如果超过一个窗口时间
            if (passTime > rule.getDurationInSec() * 1000) {
                // 返回令牌桶
                AtomicLong oldQps = tokenCounters.putIfAbsent(value, new AtomicLong(maxCount - acquireCount));
                // 如果令牌桶为null, 相当于该参数没有被访问过, 则记录本次访问时间, 并且本次允许通过
                if (oldQps == null) {
                    // Might not be accurate here.
                    lastAddTokenTime.set(currentTime);
                    return true;
                }
                // 之前访问过
                else {
                    // 令牌数
                    long restQps = oldQps.get();
                    // 上次通过时间 -> 本次通过时间这段时间内应该添加的令牌数
                    long toAddCount = (passTime * tokenCount) / (rule.getDurationInSec() * 1000);
                    // 如果应该添加的令牌数 + 剩余令牌数 >  最大阈值, 则新的令牌数 - 最大令牌数 - 1
                    // 如果应该添加的令牌数 + 剩余令牌数 <= 最大阈值, 则新的令牌数 = 剩余令牌数 + 本次应该添加的令牌数
                    long newQps = toAddCount + restQps > maxCount?
                                        (maxCount - acquireCount):
                            (restQps + toAddCount - acquireCount);

                    if (newQps < 0) {
                        return false;
                    }
                    // 设置令牌数
                    if (oldQps.compareAndSet(restQps, newQps)) {
                        lastAddTokenTime.set(currentTime);
                        return true;
                    }
                    Thread.yield();
                }
            }
            // 如果距离上次请求是在同一个窗口内
            else {
                // 当前令牌数
                AtomicLong oldQps = tokenCounters.get(value);
                // 令牌数不为null
                if (oldQps != null) {
                    long oldQpsValue = oldQps.get();
                    // 如果令牌桶 - 1 后大于0, 则-1
                    if (oldQpsValue - acquireCount >= 0) {
                        // 令牌桶-1
                        if (oldQps.compareAndSet(oldQpsValue, oldQpsValue - acquireCount)) {
                            return true;
                        }
                    } else {
                        return false;
                    }
                }
                Thread.yield();
            }
        }
    }

    /**
     * 作为匀速排队限流控制， 一个简单的漏桶实现
     * 通过 {@link ParameterMetric#getRuleTimeCounter }  记录当前规则中每个参数的上次访问时间
     *
     *
     * @param resourceWrapper 资源
     * @param rule 规则
     * @param acquireCount 请求数
     * @param value 参数
     * @return 是否通过
     */
    static boolean passThrottleLocalCheck(ResourceWrapper resourceWrapper, ParamFlowRule rule, int acquireCount,
                                          Object value) {
        // 获取参数度量指标
        ParameterMetric metric = getParameterMetric(resourceWrapper);
        // 请求时间map
        CacheMap<Object, AtomicLong> timeRecorderMap = metric == null ? null : metric.getRuleTimeCounter(rule);
        if (timeRecorderMap == null) {
            return true;
        }

        // Calculate max token count (threshold)
        // 获取配置的热点参数
        Set<Object> exclusionItems = rule.getParsedHotItems().keySet();
        // 参数的阈值
        long tokenCount = (long)rule.getCount();
        if (exclusionItems.contains(value)) {
            // 如果特别指定了热点参数的阈值，则使用
            tokenCount = rule.getParsedHotItems().get(value);
        }

        if (tokenCount == 0) {
            return false;
        }

        // 每次请求的间隔
        long costTime = Math.round(1.0 * 1000 * acquireCount * rule.getDurationInSec() / tokenCount);
        while (true) {
            long currentTime = TimeUtil.currentTimeMillis();
            AtomicLong timeRecorder = timeRecorderMap.putIfAbsent(value, new AtomicLong(currentTime));
            if (timeRecorder == null) {
                return true;
            }
            //AtomicLong timeRecorder = timeRecorderMap.get(value);
            // 上次请求时间
            long lastPassTime = timeRecorder.get();
            // 预期能通过的时间
            long expectedTime = lastPassTime + costTime;

            // 逾期时间小于等于当前时间，或者逾期时间 - 当前时间 < 能等待的最大时间
            if (expectedTime <= currentTime || expectedTime - currentTime < rule.getMaxQueueingTimeMs()) {
                // 从map中获取上次通过的时间
                AtomicLong lastPastTimeRef = timeRecorderMap.get(value);
                // 设置上次通过时间为当前
                if (lastPastTimeRef.compareAndSet(lastPassTime, currentTime)) {
                    // 如果设置成功，则说明该线程可以进入等待了，等待时间等于预期的下次通过时间 - 当前时间
                    long waitTime = expectedTime - currentTime;
                    if (waitTime > 0) {
                        // 设置上次通过时间为预期时间，其他线程将以此为基准判断预期时间
                        lastPastTimeRef.set(expectedTime);
                        try {
                            // 本线程休眠
                            TimeUnit.MILLISECONDS.sleep(waitTime);
                        } catch (InterruptedException e) {
                            RecordLog.warn("passThrottleLocalCheck: wait interrupted", e);
                        }
                    }
                    return true;
                } else {
                    Thread.yield();
                }
            } else {
                return false;
            }
        }
    }

    private static ParameterMetric getParameterMetric(ResourceWrapper resourceWrapper) {
        // Should not be null.
        return ParameterMetricStorage.getParamMetric(resourceWrapper);
    }

    @SuppressWarnings("unchecked")
    private static Collection<Object> toCollection(Object value) {
        if (value instanceof Collection) {
            return (Collection<Object>)value;
        } else if (value.getClass().isArray()) {
            List<Object> params = new ArrayList<Object>();
            int length = Array.getLength(value);
            for (int i = 0; i < length; i++) {
                Object param = Array.get(value, i);
                params.add(param);
            }
            return params;
        } else {
            return Collections.singletonList(value);
        }
    }

    private static boolean passClusterCheck(ResourceWrapper resourceWrapper, ParamFlowRule rule, int count,
                                            Object value) {
        try {
            // 将单个对象放入集合
            Collection<Object> params = toCollection(value);

            TokenService clusterService = pickClusterService();
            if (clusterService == null) {
                // No available cluster client or server, fallback to local or
                // pass in need.
                return fallbackToLocalOrPass(resourceWrapper, rule, count, params);
            }

            TokenResult result = clusterService.requestParamToken(rule.getClusterConfig().getFlowId(), count, params);
            switch (result.getStatus()) {
                case TokenResultStatus.OK:
                    return true;
                case TokenResultStatus.BLOCKED:
                    return false;
                default:
                    return fallbackToLocalOrPass(resourceWrapper, rule, count, params);
            }
        } catch (Throwable ex) {
            RecordLog.warn("[ParamFlowChecker] Request cluster token for parameter unexpected failed", ex);
            return fallbackToLocalOrPass(resourceWrapper, rule, count, value);
        }
    }

    private static boolean fallbackToLocalOrPass(ResourceWrapper resourceWrapper, ParamFlowRule rule, int count,
                                                 Object value) {
        if (rule.getClusterConfig().isFallbackToLocalWhenFail()) {
            return passLocalCheck(resourceWrapper, rule, count, value);
        } else {
            // The rule won't be activated, just pass.
            return true;
        }
    }

    private static TokenService pickClusterService() {
        if (ClusterStateManager.isClient()) {
            return TokenClientProvider.getClient();
        }
        if (ClusterStateManager.isServer()) {
            return EmbeddedClusterTokenServerProvider.getServer();
        }
        return null;
    }

    private ParamFlowChecker() {
    }
}
