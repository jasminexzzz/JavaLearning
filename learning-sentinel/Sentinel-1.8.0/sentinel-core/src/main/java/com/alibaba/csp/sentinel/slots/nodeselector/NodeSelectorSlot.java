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
package com.alibaba.csp.sentinel.slots.nodeselector;

import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.node.ClusterNode;
import com.alibaba.csp.sentinel.node.DefaultNode;
import com.alibaba.csp.sentinel.node.EntranceNode;
import com.alibaba.csp.sentinel.slotchain.AbstractLinkedProcessorSlot;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import com.alibaba.csp.sentinel.spi.SpiOrder;

import java.util.HashMap;
import java.util.Map;

/**
 * </p>
 * This class will try to build the calling traces via
 * <ol>
 * <li>adding a new {@link DefaultNode} if needed as the last child in the context.
 * The context's last node is the current node or the parent node of the context. </li>
 * <li>setting itself to the context current node.</li>
 * </ol>
 * </p>
 *
 * <p>It works as follow:</p>
 * <pre>
 * ContextUtil.enter("entrance1", "appA");
 * Entry nodeA = SphU.entry("nodeA");
 * if (nodeA != null) {
 *     nodeA.exit();
 * }
 * ContextUtil.exit();
 * </pre>
 *
 * Above code will generate the following invocation structure in memory:
 *
 * <pre>
 *
 *              machine-root
 *                  /
 *                 /
 *           EntranceNode1
 *               /
 *              /
 *        DefaultNode(nodeA)- - - - - -> ClusterNode(nodeA);
 * </pre>
 *
 * <p>
 * Here the {@link EntranceNode} represents "entrance1" given by
 * {@code ContextUtil.enter("entrance1", "appA")}.
 * </p>
 * <p>
 * Both DefaultNode(nodeA) and ClusterNode(nodeA) holds statistics of "nodeA", which is given
 * by {@code SphU.entry("nodeA")}
 * </p>
 * <p>
 * The {@link ClusterNode} is uniquely identified by the ResourceId; the {@link DefaultNode}
 * is identified by both the resource id and {@link Context}. In other words, one resource
 * id will generate multiple {@link DefaultNode} for each distinct context, but only one
 * {@link ClusterNode}.
 * </p>
 * <p>
 * the following code shows one resource id in two different context:
 * </p>
 *
 * <pre>
 *    ContextUtil.enter("entrance1", "appA");
 *    Entry nodeA = SphU.entry("nodeA");
 *    if (nodeA != null) {
 *        nodeA.exit();
 *    }
 *    ContextUtil.exit();
 *
 *    ContextUtil.enter("entrance2", "appA");
 *    nodeA = SphU.entry("nodeA");
 *    if (nodeA != null) {
 *        nodeA.exit();
 *    }
 *    ContextUtil.exit();
 * </pre>
 *
 * Above code will generate the following invocation structure in memory:
 *
 * <pre>
 *
 *                  machine-root
 *                  /         \
 *                 /           \
 *         EntranceNode1   EntranceNode2
 *               /               \
 *              /                 \
 *      DefaultNode(nodeA)   DefaultNode(nodeA)
 *             |                    |
 *             +- - - - - - - - - - +- - - - - - -> ClusterNode(nodeA);
 * </pre>
 *
 * <p>
 * As we can see, two {@link DefaultNode} are created for "nodeA" in two context, but only one
 * {@link ClusterNode} is created.
 * </p>
 *
 * <p>
 * We can also check this structure by calling: <br/>
 * {@code curl http://localhost:8719/tree?type=root}
 * </p>
 *
 * @author jialiang.linjl
 * @see EntranceNode
 * @see ContextUtil
 */
@SpiOrder(-10000)
public class NodeSelectorSlot extends AbstractLinkedProcessorSlot<Object> {

    /**
     * {@link DefaultNode}s of the same resource in different context.
     */
    private volatile Map<String, DefaultNode> map = new HashMap<String, DefaultNode>(10);

    @Override
    public void entry(Context context, ResourceWrapper resourceWrapper, Object obj, int count, boolean prioritized, Object... args)
        throws Throwable {
        /**
         * It's interesting that we use context name rather resource name as the map key.
         *
         * Remember that same resource({@link ResourceWrapper#equals(Object)}) will share
         * the same {@link ProcessorSlotChain} globally, no matter in which context. So if
         * code goes into {@link #entry(Context, ResourceWrapper, DefaultNode, int, Object...)},
         * the resource name must be same but context name may not.
         *
         * If we use {@link com.alibaba.csp.sentinel.SphU#entry(String resource)} to
         * enter same resource in different context, using context name as map key can
         * distinguish the same resource. In this case, multiple {@link DefaultNode}s will be created
         * of the same resource name, for every distinct context (different context name) each.
         *
         * Consider another question. One resource may have multiple {@link DefaultNode},
         * so what is the fastest way to get total statistics of the same resource?
         * The answer is all {@link DefaultNode}s with same resource name share one
         * {@link ClusterNode}. See {@link ClusterBuilderSlot} for detail.
         *
         * ─────────────────────────────────────────────────────────────────────────────────────────────────────────────
         *
         * 有趣的是，我们使用上下文名而不是资源名作为映射键。
         *
         * 记住，同一个资源 ResourceWrapper 将全局共享同一个 ProcessorSlotChain ，无论在哪个上下文中。
         * 因此，如果代码进入 entry(Context, ResourceWrapper, DefaultNode, int, Object…) 则资源名必须相同，但上下文名可能不同。
         *
         * 如果我们使用 SphU#entry 在不同的上下文中输入相同的资源，使用上下文名作为映射键可以区分相同的资源。
         * 在本例中，将使用相同的资源名创建多个 DefaultNod，用于每个不同的上下文(不同的上下文名)。
         * 考虑另一个问题。一个资源可能有多个 DefaultNode 那么获取同一资源的总体统计信息的最快方法是什么?
         * 答案是所有具有相同资源名的 DefaultNode 共享一个 ClusterNode 详情请参见{@link ClusterBuilderSlot}。
         */
        DefaultNode node = map.get(context.getName());
        if (node == null) {
            synchronized (this) {
                node = map.get(context.getName());
                if (node == null) {
                    node = new DefaultNode(resourceWrapper, null);
                    HashMap<String, DefaultNode> cacheMap = new HashMap<String, DefaultNode>(map.size());
                    cacheMap.putAll(map);
                    cacheMap.put(context.getName(), node);
                    map = cacheMap;
                    // Build invocation tree
                    ((DefaultNode) context.getLastNode()).addChild(node);
                }

            }
        }

        context.setCurNode(node);
        fireEntry(context, resourceWrapper, node, count, prioritized, args);
    }

    @Override
    public void exit(Context context, ResourceWrapper resourceWrapper, int count, Object... args) {
        fireExit(context, resourceWrapper, count, args);
    }
}
