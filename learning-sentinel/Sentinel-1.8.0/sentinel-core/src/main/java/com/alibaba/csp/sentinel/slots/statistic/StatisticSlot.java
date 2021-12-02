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
package com.alibaba.csp.sentinel.slots.statistic;

import java.util.Collection;

import com.alibaba.csp.sentinel.node.Node;
import com.alibaba.csp.sentinel.slotchain.ProcessorSlotEntryCallback;
import com.alibaba.csp.sentinel.slotchain.ProcessorSlotExitCallback;
import com.alibaba.csp.sentinel.slots.block.flow.PriorityWaitException;
import com.alibaba.csp.sentinel.spi.SpiOrder;
import com.alibaba.csp.sentinel.util.TimeUtil;
import com.alibaba.csp.sentinel.Constants;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.node.ClusterNode;
import com.alibaba.csp.sentinel.node.DefaultNode;
import com.alibaba.csp.sentinel.slotchain.AbstractLinkedProcessorSlot;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import com.alibaba.csp.sentinel.slots.block.BlockException;

/**
 * <p>
 * A processor slot that dedicates to real time statistics.
 * When entering this slot, we need to separately count the following
 * information:
 * <ul>
 * <li>{@link ClusterNode}: total statistics of a cluster node of the resource ID.</li>
 * <li>Origin node: statistics of a cluster node from different callers/origins.</li>
 * <li>{@link DefaultNode}: statistics for specific resource name in the specific context.</li>
 * <li>Finally, the sum statistics of all entrances.</li>
 * </ul>
 * </p>
 *
 * @author jialiang.linjl
 * @author Eric Zhao
 */
@SpiOrder(-7000)
public class StatisticSlot extends AbstractLinkedProcessorSlot<DefaultNode> {

    @Override
    public void entry(Context context, ResourceWrapper resourceWrapper, DefaultNode node, int count,
                      boolean prioritized, Object... args) throws Throwable {
        try {
            // Do some checking.
            // 先执行后续节点
            fireEntry(context, resourceWrapper, node, count, prioritized, args);

            // 如果正常执行，则请求通过，执行通过请求的统计信息
            // 1. 添加线程数和请求通过数
            // Request passed, add thread count and pass count.
            node.increaseThreadNum();
            node.addPassRequest(count);

            // 2. 如果来源节点不为空，则说明用户指定了来源，则增加来源节点的线程数和请求通过数
            if (context.getCurEntry().getOriginNode() != null) {
                // Add count for origin node.
                context.getCurEntry().getOriginNode().increaseThreadNum();
                context.getCurEntry().getOriginNode().addPassRequest(count);
            }

            // 3. 如果是入口流量统计，则增加系统全局的线程数和请求通过数
            if (resourceWrapper.getEntryType() == EntryType.IN) {
                // Add count for global inbound entry node for global statistics.
                Constants.ENTRY_NODE.increaseThreadNum();
                Constants.ENTRY_NODE.addPassRequest(count);
            }

            /*
             * 执行通过请求的回调方法
             * 默认只有一个: {@link com.alibaba.csp.sentinel.metric.extension.callback.MetricEntryCallback}
             * Handle pass event with registered entry callback handlers.
             */
            for (ProcessorSlotEntryCallback<DefaultNode> handler : StatisticSlotCallbackRegistry.getEntryCallbacks()) {
                handler.onPass(context, resourceWrapper, node, count, args);
            }
        }
        /*
         * 该异常说明，请求是允许通过的，但占用了未来的流量，如果请求优先级很高，可以将 prioritized 参数设置为true
         * 但注意此异常是不增加请求通过数的，业务该请求会在下一个时间窗口中通过，所以只增加线程数，而不增加通过数，通过数是算在下一个时间窗口中的
         * 这部分逻辑可以看《流控 - 实现原理》
         */
        catch (PriorityWaitException ex) {
            // 1. 增加线程数
            node.increaseThreadNum();

            // 2. 如果来源节点不为空，则说明用户指定了来源，则增加来源节点的线程数
            if (context.getCurEntry().getOriginNode() != null) {
                // Add count for origin node.
                context.getCurEntry().getOriginNode().increaseThreadNum();
            }

            // 3. 如果是入口流量统计，则增加系统全局的线程数
            if (resourceWrapper.getEntryType() == EntryType.IN) {
                // Add count for global inbound entry node for global statistics.
                Constants.ENTRY_NODE.increaseThreadNum();
            }

            /*
             * 执行通过请求的回调方法
             * 默认只有一个: {@link com.alibaba.csp.sentinel.metric.extension.callback.MetricEntryCallback}
             * Handle pass event with registered entry callback handlers.
             */
            for (ProcessorSlotEntryCallback<DefaultNode> handler : StatisticSlotCallbackRegistry.getEntryCallbacks()) {
                handler.onPass(context, resourceWrapper, node, count, args);
            }
        }

        catch (BlockException e) {
            // 保存当前条目的阻塞异常信息
            // Blocked, set block exception to current entry.
            context.getCurEntry().setBlockError(e);
            // Add block count.CtSph
            // 1. 增加阻塞QPS
            node.increaseBlockQps(count);

            // 2. 如果来源节点不为空，则说明用户指定了来源，则增加来源节点的阻塞QPS
            if (context.getCurEntry().getOriginNode() != null) {
                context.getCurEntry().getOriginNode().increaseBlockQps(count);
            }

            if (resourceWrapper.getEntryType() == EntryType.IN) {
                // Add count for global inbound entry node for global statistics.
                // 3. 如果是入口流量统计，则增加系统全局的阻塞QPS
                Constants.ENTRY_NODE.increaseBlockQps(count);
            }

            /**
             * 执行阻塞请求的回调方法
             * 默认只有一个: {@link com.alibaba.csp.sentinel.metric.extension.callback.MetricEntryCallback}
             * Handle block event with registered entry callback handlers.
             */
            for (ProcessorSlotEntryCallback<DefaultNode> handler : StatisticSlotCallbackRegistry.getEntryCallbacks()) {
                handler.onBlocked(e, context, resourceWrapper, node, count, args);
            }

            throw e;
        } catch (Throwable e) {
            // Unexpected internal error, set error to current entry.
            // 抛出其他异常，则保存当前条目的异常信息
            context.getCurEntry().setError(e);

            throw e;
        }
    }

    @Override
    public void exit(Context context, ResourceWrapper resourceWrapper, int count, Object... args) {
        Node node = context.getCurNode();

        // 如果退出时, 没有发生阻塞异常
        if (context.getCurEntry().getBlockError() == null) {
            // Calculate response time (use completeStatTime as the time of completion).
            // 计算当前条目的结束时间
            long completeStatTime = TimeUtil.currentTimeMillis();
            context.getCurEntry().setCompleteTimestamp(completeStatTime);
            // 计算当前条目的响应时间
            long rt = completeStatTime - context.getCurEntry().getCreateTimestamp();

            Throwable error = context.getCurEntry().getError();

            // Record response time and success count.
            // 增加当前上下文节点的统计响应时间, 异常数(如果发生异常), 减少线程数
            recordCompleteFor(node, count, rt, error);
            // 增加当前来源节点的统计响应时间, 异常数(如果发生异常), 减少线程数
            recordCompleteFor(context.getCurEntry().getOriginNode(), count, rt, error);
            // 增加系统全局节点的统计响应时间, 异常数(如果发生异常), 减少线程数
            if (resourceWrapper.getEntryType() == EntryType.IN) {
                recordCompleteFor(Constants.ENTRY_NODE, count, rt, error);
            }
        }

        // Handle exit event with registered exit callback handlers.
        // 执行退出回调方法
        Collection<ProcessorSlotExitCallback> exitCallbacks = StatisticSlotCallbackRegistry.getExitCallbacks();
        for (ProcessorSlotExitCallback handler : exitCallbacks) {
            handler.onExit(context, resourceWrapper, count, args);
        }

        fireExit(context, resourceWrapper, count);
    }

    /**
     * 增加当前上下文节点的统计响应时间, 业务异常数(如果发生异常), 减少线程数
     * @param node 节点
     * @param batchCount 请求数
     * @param rt 本次的请求耗时
     * @param error 异常信息
     */
    private void recordCompleteFor(Node node, int batchCount, long rt, Throwable error) {
        if (node == null) {
            return;
        }
        // 记录平均响应时间
        node.addRtAndSuccess(rt, batchCount);
        // 减少线程数
        node.decreaseThreadNum();

        // 如果存在异常, 则增加异常QPS数, 阻塞的异常是不算在内, 算的是业务异常
        if (error != null && !(error instanceof BlockException)) {
            node.increaseExceptionQps(batchCount);
        }
    }
}
