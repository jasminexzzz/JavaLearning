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
package com.alibaba.csp.sentinel.slots.block.flow;

import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.node.DefaultNode;
import com.alibaba.csp.sentinel.slotchain.AbstractLinkedProcessorSlot;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.spi.SpiOrder;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.csp.sentinel.util.function.Function;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Combined the runtime statistics collected from the previous
 * slots (NodeSelectorSlot, ClusterNodeBuilderSlot, and StatisticSlot), FlowSlot
 * will use pre-set rules to decide whether the incoming requests should be
 * blocked.
 *
 * 结合从以前的插槽(NodeSelectorSlot, ClusterNodeBuilderSlot，和StatisticSlot)收集的运行时统计数据，FlowSlot将使用预先设置的规则来决定是否应该阻塞传入的请求。
 * </p>
 *
 * <p>
 * {@code SphU.entry(resourceName)} will throw {@code FlowException} if any rule is
 * triggered. Users can customize their own logic by catching {@code FlowException}.
 *
 * 如果触发任何规则，{@code spu .entry(resourceName)}将抛出{@code FlowException}。用户可以通过捕获{@code FlowException}来定制自己的逻辑。
 * </p>
 *
 * <p>
 * One resource can have multiple flow rules. FlowSlot traverses these rules
 * until one of them is triggered or all rules have been traversed.
 *
 * 一个资源可以有多个流规则。FlowSlot遍历这些规则，直到其中一个被触发或所有规则都被遍历。
 * </p>
 *
 * <p>
 * Each {@link FlowRule} is mainly composed of these factors: grade, strategy, path. We
 * can combine these factors to achieve different effects.
 *
 * 每个{@link FlowRule}主要由等级、策略、路径等因素组成。我们可以结合这些因素来达到不同的效果。
 * </p>
 *
 * <p>
 * The grade is defined by the {@code grade} field in {@link FlowRule}. Here, 0 for thread
 * isolation and 1 for request count shaping (QPS). Both thread count and request
 * count are collected in real runtime, and we can view these statistics by
 * following command:
 *
 * 等级由{@link FlowRule}中的{@code grade}字段定义。这里，0表示线程隔离，1表示请求计数整形(QPS)。线程数和请求数都是在实际运行时收集的，我们可以通过以下命令查看这些统计数据:
 * </p>
 *
 * <pre>
 * curl http://localhost:8719/tree
 *
 * idx id    thread pass  blocked   success total aRt   1m-pass   1m-block   1m-all   exception
 * 2   abc647 0      460    46          46   1    27      630       276        897      0
 * </pre>
 *
 * <ul>
 * <li>{@code thread}    用于当前正在处理该资源的线程数</li>
 * <li>{@code pass}      查询一秒内传入请求的计数</li>
 * <li>{@code blocked}   查询一秒钟内被阻塞的请求的计数</li>
 * <li>{@code success}   获取Sentinel在1秒内成功处理的请求数</li>
 * <li>{@code RT}        请求在一秒内的平均响应时间</li>
 * <li>{@code total}     查询一秒钟内收到的请求和被阻止的请求的总和</li>
 * <li>{@code 1m-pass}   是一分钟内传入请求的计数</li>
 * <li>{@code 1m-block}  是在一分钟内阻塞请求的计数</li>
 * <li>{@code 1m-all}    一分钟内传入和阻止的请求总数是多少</li>
 * <li>{@code exception} 用于一秒钟内业务(定制)异常的计数</li>
 * </ul>
 *
 * <ul>
 * <li>{@code thread} for the count of threads that is currently processing the resource</li>
 * <li>{@code pass} for the count of incoming request within one second</li>
 * <li>{@code blocked} for the count of requests blocked within one second</li>
 * <li>{@code success} for the count of the requests successfully handled by Sentinel within one second</li>
 * <li>{@code RT} for the average response time of the requests within a second</li>
 * <li>{@code total} for the sum of incoming requests and blocked requests within one second</li>
 * <li>{@code 1m-pass} is for the count of incoming requests within one minute</li>
 * <li>{@code 1m-block} is for the count of a request blocked within one minute</li>
 * <li>{@code 1m-all} is the total of incoming and blocked requests within one minute</li>
 * <li>{@code exception} is for the count of business (customized) exceptions in one second</li>
 * </ul>
 *
 * This stage is usually used to protect resources from occupying. If a resource
 * takes long time to finish, threads will begin to occupy. The longer the
 * response takes, the more threads occupy.
 *
 * Besides counter, thread pool or semaphore can also be used to achieve this.
 *
 * - Thread pool: Allocate a thread pool to handle these resource. When there is
 * no more idle thread in the pool, the request is rejected without affecting
 * other resources.
 *
 * - Semaphore: Use semaphore to control the concurrent count of the threads in
 * this resource.
 *
 * The benefit of using thread pool is that, it can walk away gracefully when
 * time out. But it also bring us the cost of context switch and additional
 * threads. If the incoming requests is already served in a separated thread,
 * for instance, a Servlet HTTP request, it will almost double the threads count if
 * using thread pool.
 *
 * 这个阶段通常用于保护资源不被占用。如果一个资源需要很长时间才能完成，线程就会开始占用。响应时间越长，占用的线程越多。
 * 除了计数器，线程池或信号量也可以用来实现这一点。
 * - 线程池:分配一个线程池来处理这些资源。当池中没有空闲线程时，请求将被拒绝，而不会影响其他资源。
 * - 信号量:使用信号量来控制该资源中线程的并发计数。
 *
 * 使用线程池的好处是，它可以在超时时优雅地离开。但是它也给我们带来了上下文切换和额外线程的成本。
 * 如果传入的请求已经在一个单独的线程中得到服务，例如Servlet HTTP请求，那么如果使用线程池，线程数几乎会增加一倍。
 *
 * <h3>Traffic Shaping</h3>
 * <p>
 * When QPS exceeds the threshold, Sentinel will take actions to control the incoming request,
 * and is configured by {@code controlBehavior} field in flow rules.
 *
 * 当QPS超过阈值时，Sentinel将采取措施来控制传入的请求，并通过流规则中的{@code controlBehavior}字段进行配置。
 * </p>
 * <ol>
 * <li>Immediately reject ({@code RuleConstant.CONTROL_BEHAVIOR_DEFAULT})</li>
 * 立即拒绝({@code RuleConstant.CONTROL_BEHAVIOR_DEFAULT})
 * <p>
 * This is the default behavior. The exceeded request is rejected immediately
 * and the FlowException is thrown
 * 这是默认行为。超出的请求立即被拒绝，并抛出FlowException
 * </p>
 *
 * <li>Warmup ({@code RuleConstant.CONTROL_BEHAVIOR_WARM_UP})</li>
 * <p>
 * If the load of system has been low for a while, and a large amount of
 * requests comes, the system might not be able to handle all these requests at
 * once. However if we steady increase the incoming request, the system can warm
 * up and finally be able to handle all the requests.
 * This warmup period can be configured by setting the field {@code warmUpPeriodSec} in flow rules.
 *
 * 如果系统的负载已经低了一段时间，并且出现了大量的请求，系统可能无法一次处理所有这些请求。
 * 然而，如果我们稳定地增加传入请求，系统就可以预热起来，最终能够处理所有请求。这个预热时间可以通过在流规则中设置字段{@code warmUpPeriodSec}来配置。
 * </p>
 *
 * <li>Uniform Rate Limiting ({@code RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER})</li>
 * 统一速率限制({@code ruleconstant . control_behavior_rate_limit})
 * <p>
 * This strategy strictly controls the interval between requests.
 * In other words, it allows requests to pass at a stable, uniform rate.
 * 该策略严格控制请求之间的间隔。换句话说，它允许请求以稳定、统一的速率传递。
 * </p>
 * <img src="https://raw.githubusercontent.com/wiki/alibaba/Sentinel/image/uniform-speed-queue.png" style="max-width:
 * 60%;"/>
 * <p>
 * This strategy is an implement of <a href="https://en.wikipedia.org/wiki/Leaky_bucket">leaky bucket</a>.
 * It is used to handle the request at a stable rate and is often used in burst traffic (e.g. message handling).
 * When a large number of requests beyond the system’s capacity arrive
 * at the same time, the system using this strategy will handle requests and its
 * fixed rate until all the requests have been processed or time out.
 *
 * 此策略是实现<a href="https://en.wikipedia.org/wiki/Leaky_bucket">漏水桶</a>。
 * 它用于以稳定的速率处理请求，经常用于突发流量(例如消息处理)。
 * 当超过系统容量的大量请求同时到达时，使用此策略的系统将以固定的速率处理请求，直到所有请求都被处理或超时。
 * </p>
 * </ol>
 *
 * @author jialiang.linjl
 * @author Eric Zhao
 */
@SpiOrder(-2000)
public class FlowSlot extends AbstractLinkedProcessorSlot<DefaultNode> {

    private final FlowRuleChecker checker;

    public FlowSlot() {
        this(new FlowRuleChecker());
    }

    /**
     * Package-private for test.
     *
     * @param checker flow rule checker
     * @since 1.6.1
     */
    FlowSlot(FlowRuleChecker checker) {
        AssertUtil.notNull(checker, "flow checker should not be null");
        this.checker = checker;
    }

    @Override
    public void entry(Context context, ResourceWrapper resourceWrapper, DefaultNode node, int count,
                      boolean prioritized, Object... args) throws Throwable {
        checkFlow(resourceWrapper, context, node, count, prioritized);

        fireEntry(context, resourceWrapper, node, count, prioritized, args);
    }

    void checkFlow(ResourceWrapper resource, Context context, DefaultNode node, int count, boolean prioritized)
        throws BlockException {
        checker.checkFlow(ruleProvider, resource, context, node, count, prioritized);
    }

    @Override
    public void exit(Context context, ResourceWrapper resourceWrapper, int count, Object... args) {
        fireExit(context, resourceWrapper, count, args);
    }

    private final Function<String, Collection<FlowRule>> ruleProvider = new Function<String, Collection<FlowRule>>() {
        @Override
        public Collection<FlowRule> apply(String resource) {
            // Flow rule map should not be null.
            Map<String, List<FlowRule>> flowRules = FlowRuleManager.getFlowRuleMap();
            return flowRules.get(resource);
        }
    };
}
