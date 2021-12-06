/*
 * Copyright (C) 2012 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.common.util.concurrent;

import static java.lang.Math.min;
import static java.util.concurrent.TimeUnit.SECONDS;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.math.LongMath;
import java.util.concurrent.TimeUnit;

@GwtIncompatible
@ElementTypesAreNonnullByDefault
abstract class SmoothRateLimiter extends RateLimiter {
  /*
   * How is the RateLimiter designed, and why?
   * RateLimiter 是如何设计的，为什么?
   *
   * The primary feature of a RateLimiter is its "stable rate", the maximum rate that it should
   * allow in normal conditions. This is enforced by "throttling" incoming requests as needed. For
   * example, we could compute the appropriate throttle time for an incoming request, and make the
   * calling thread wait for that time.
   * RateLimiter 的主要特点是它的“稳定速率 - stable rate”，即在正常情况下允许的最大速率。这是通过根据需要 "限制 - throttling" 传入请求来实现的。
   * 例如，我们可以计算传入请求的适当节流时间，并让调用线程等待该时间。
   *
   * The simplest way to maintain a rate of QPS is to keep the timestamp of the last granted
   * request, and ensure that (1/QPS) seconds have elapsed since then. For example, for a rate of
   * QPS=5 (5 tokens per second), if we ensure that a request isn't granted earlier than 200ms after
   * the last one, then we achieve the intended rate. If a request comes and the last request was
   * granted only 100ms ago, then we wait for another 100ms. At this rate, serving 15 fresh permits
   * (i.e. for an acquire(15) request) naturally takes 3 seconds.
   * 维护 QPS 速率的最简单方法是保存最后一个被授予请求的时间戳，并确保从那时起已经经过了(1/QPS)秒。
   * 例如，对于QPS=5(每秒5个令牌)的速率，如果我们确保一个请求在最后一个请求之后200毫秒之前没有被授予，那么我们就达到了预期的速率。
   * 如果一个请求来了，而最后一个请求在100毫秒之前才被授予，那么我们再等待100毫秒。按照这个速度，服务15个新的许可(即获取15个)自然需要3秒。
   *
   * It is important to realize that such a RateLimiter has a very superficial memory of the past:
   * it only remembers the last request. What if the RateLimiter was unused for a long period of
   * time, then a request arrived and was immediately granted? This RateLimiter would immediately
   * forget about that past underutilization. This may result in either underutilization or
   * overflow, depending on the real world consequences of not using the expected rate.
   * 重要的是要意识到这样的情况，一个 RateLimiter 对过去一段时间的系统情况的记忆是粗浅的:
   * 这句话可以理解为，它只记得最后一个请求。如果限流器在很长一段时间内未使用，然后一个请求到达并立即被批准后，会怎么样?
   * 这个限流器会立即忘记过去的这段时间系统是很长时间不被使用的。这可能导致资源未充分利用或溢出，具体取决于没有使用预期速率的实际结果。
   *
   * Past underutilization could mean that excess resources are available. Then, the RateLimiter
   * should speed up for a while, to take advantage of these resources. This is important when the
   * rate is applied to networking (limiting bandwidth), where past underutilization typically
   * translates to "almost empty buffers", which can be filled immediately.
   * 过去的资源未充分利用可能意味着有多余的资源可用。那么，限流器应该加速一段时间，以便利用这些资源。
   * 当速率应用于网络(限制带宽)时，这一点很重要，因为过去的未充分利用通常会导致“几乎为空的缓冲区”，这些缓冲区可以立即被填充。
   * ===================================================== 批注 =========================================================
   * 情况一：系统在一段时间不使用后，突然来请求时，系统应该允许更大的流量通过，随后逐渐将流量降低。
   * ===================================================================================================================
   *
   *
   * On the other hand, past underutilization could mean that "the server responsible for handling
   * the request has become less ready for future requests", i.e. its caches become stale, and
   * requests become more likely to trigger expensive operations (a more extreme case of this
   * example is when a server has just booted, and it is mostly busy with getting itself up to
   * speed).
   * 另一方面,过去未充分利用可能意味着 “服务器负责处理请求已经变得不那么准备未来的请求”, 即其缓存变得陈旧, 请求变得更容易触发昂贵操作
   * (一个更极端的例子,这个例子是当一个服务器刚刚启动,而且它主要忙于让自己跟上速度)。
   * ===================================================== 批注 =========================================================
   * 情况二：系统在一段时间不使用后，突然来请求时，系统应该允许较少的流量通过，随后逐渐将流量提高。
   * ===================================================================================================================
   *
   * To deal with such scenarios, we add an extra dimension, that of "past underutilization",
   * modeled by "storedPermits" variable. This variable is zero when there is no underutilization,
   * and it can grow up to maxStoredPermits, for sufficiently large underutilization. So, the
   * requested permits, by an invocation acquire(permits), are served from:
   * - stored permits (if available)
   * - fresh permits (for any remaining permits)
   * 为了处理这样的场景，我们添加了一个额外的维度，即由 “storedPermits” 变量建模的 “过去未充分利用” 维度。
   * 当过去没有未充分利用的资源时，该变量为零，当过去有未充分利用的资源时，它可以增长到 maxStoredPermits 令牌桶的最大容量。
   * 因此，请求的许可证，通过从以下两个来调用 acquire(permits) 获取:
   * - 存储的令牌, 即令牌桶(如有)
   * - 新发的令牌(任何剩余许可证)
   *
   * How this works is best explained with an example:
   * 最好有一个例子解释它时如何工作的:
   *
   * For a RateLimiter that produces 1 token per second, every second that goes by with the
   * RateLimiter being unused, we increase storedPermits by 1. Say we leave the RateLimiter unused
   * for 10 seconds (i.e., we expected a request at time X, but we are at time X + 10 seconds before
   * a request actually arrives; this is also related to the point made in the last paragraph), thus
   * storedPermits becomes 10.0 (assuming maxStoredPermits >= 10.0). At that point, a request of
   * acquire(3) arrives. We serve this request out of storedPermits, and reduce that to 7.0 (how
   * this is translated to throttling time is discussed later). Immediately after, assume that an
   * acquire(10) request arriving. We serve the request partly from storedPermits, using all the
   * remaining 7.0 permits, and the remaining 3.0, we serve them by fresh permits produced by the
   * rate limiter.
   * 对于每秒产生1个令牌的 RateLimiter，在 RateLimiter 未使用的情况下每过一秒，我们将 storedPermit 增加1。
   * 假设我们让 RateLimiter 闲置10秒(即，我们期望在X时间有一个请求，但我们在X + 10秒的时间请求实际到达;
   * 这也与上一段中提到的一点有关)，因此 storedPermit 变成了10.0(假设 maxStoredPermits >= 10.0)。此时，获取请求(3)到达。
   * 我们从 storedPermit 提供此请求，并将其减少到7.0(稍后将讨论如何将其转换为节流时间)。
   * 紧接着，假设一个获取(10)请求到达。我们服务的请求部分来自 storedPermit，使用所有剩余的7.0许可证，和剩余的3.0许可证，
   * 我们提供他们由速率限制器产生的新鲜许可证。
   *
   * We already know how much time it takes to serve 3 fresh permits: if the rate is
   * "1 token per second", then this will take 3 seconds. But what does it mean to serve 7 stored
   * permits? As explained above, there is no unique answer. If we are primarily interested to deal
   * with underutilization, then we want stored permits to be given out /faster/ than fresh ones,
   * because underutilization = free resources for the taking. If we are primarily interested to
   * deal with overflow, then stored permits could be given out /slower/ than fresh ones. Thus, we
   * require a (different in each case) function that translates storedPermits to throttling time.
   * 我们已经知道提供 3 个 fresh permits 需要多少时间: 如果速率是 "每秒1个令牌" ，那么这将需要3秒。
   * 但是服务 7 个 stored permits 证意味着什么?
   * 如上所述，没有唯一的答案。
   * - 如果我们主要关心的是未充分利用的的资源，那么我们希望 stored permits 比 fresh permits 发放得更快, 因为未充分利用的资源 = 免费资源。
   * - 如果我们主要想处理溢出的问题，那么 stored permits 的发放速度可能会比 fresh permits 慢。
   * 因此，我们需要一个(在每种情况下不同)函数将 storedPermit 属性转换为节流时间。
   * ===================================================== 批注 =========================================================
   * 需要一个函数, 能够控制存储的令牌的发放速度
   * ===================================================================================================================
   *
   * This role is played by storedPermitsToWaitTime(double storedPermits, double permitsToTake). The
   * underlying model is a continuous function mapping storedPermits (from 0.0 to maxStoredPermits)
   * onto the 1/rate (i.e. intervals) that is effective at the given storedPermits. "storedPermits"
   * essentially measure unused time; we spend unused time buying/storing permits. Rate is
   * "permits / time", thus "1 / rate = time / permits". Thus, "1/rate" (time / permits) times
   * "permits" gives time, i.e., integrals on this function (which is what storedPermitsToWaitTime()
   * computes) correspond to minimum intervals between subsequent requests, for the specified number
   * of requested permits.
   * 这个角色由 storedPermitsToWaitTime(double storedpermit, double permitsToTake)方法来扮演。
   * 底层模型是一个连续的函数，将 storedPermit (从 0.0 到 maxStoredPermits ) 映射到1/请求速率(即每个令牌的间隔)，这在给定的 storedPermit 中是有效的。
   * "storedPermit" 本质上度量的时未使用的时间; 我们把未使用的时间花在 购买或储存 permits(令牌) 上。
   * Rate 是 [permits / time](令牌/时间, 例如令牌为5,时间为1s,则请求的速率就是0.2s) ，因此 [1 / Rate = time / permits]。
   * 因此，[1/rate](time/permits)乘以 permits 给出时间，即这个函数(storedPermitsToWaitTime()) 计算的结果就是在指定请求
   * 令牌 permits 的数量时, 后续请求之间的最小间隔.
   *
   * Here is an example of storedPermitsToWaitTime: If storedPermits == 10.0, and we want 3 permits,
   * we take them from storedPermits, reducing them to 7.0, and compute the throttling for these as
   * a call to storedPermitsToWaitTime(storedPermits = 10.0, permitsToTake = 3.0), which will
   * evaluate the integral of the function from 7.0 to 10.0.
   *
   * Using integrals guarantees that the effect of a single acquire(3) is equivalent to {
   * acquire(1); acquire(1); acquire(1); }, or { acquire(2); acquire(1); }, etc, since the integral
   * of the function in [7.0, 10.0] is equivalent to the sum of the integrals of [7.0, 8.0], [8.0,
   * 9.0], [9.0, 10.0] (and so on), no matter what the function is. This guarantees that we handle
   * correctly requests of varying weight (permits), /no matter/ what the actual function is - so we
   * can tweak the latter freely. (The only requirement, obviously, is that we can compute its
   * integrals).
   *
   * Note well that if, for this function, we chose a horizontal line, at height of exactly (1/QPS),
   * then the effect of the function is non-existent: we serve storedPermits at exactly the same
   * cost as fresh ones (1/QPS is the cost for each). We use this trick later.
   *
   * If we pick a function that goes /below/ that horizontal line, it means that we reduce the area
   * of the function, thus time. Thus, the RateLimiter becomes /faster/ after a period of
   * underutilization. If, on the other hand, we pick a function that goes /above/ that horizontal
   * line, then it means that the area (time) is increased, thus storedPermits are more costly than
   * fresh permits, thus the RateLimiter becomes /slower/ after a period of underutilization.
   *
   * Last, but not least: consider a RateLimiter with rate of 1 permit per second, currently
   * completely unused, and an expensive acquire(100) request comes. It would be nonsensical to just
   * wait for 100 seconds, and /then/ start the actual task. Why wait without doing anything? A much
   * better approach is to /allow/ the request right away (as if it was an acquire(1) request
   * instead), and postpone /subsequent/ requests as needed. In this version, we allow starting the
   * task immediately, and postpone by 100 seconds future requests, thus we allow for work to get
   * done in the meantime instead of waiting idly.
   *
   * This has important consequences: it means that the RateLimiter doesn't remember the time of the
   * _last_ request, but it remembers the (expected) time of the _next_ request. This also enables
   * us to tell immediately (see tryAcquire(timeout)) whether a particular timeout is enough to get
   * us to the point of the next scheduling time, since we always maintain that. And what we mean by
   * "an unused RateLimiter" is also defined by that notion: when we observe that the
   * "expected arrival time of the next request" is actually in the past, then the difference (now -
   * past) is the amount of time that the RateLimiter was formally unused, and it is that amount of
   * time which we translate to storedPermits. (We increase storedPermits with the amount of permits
   * that would have been produced in that idle time). So, if rate == 1 permit per second, and
   * arrivals come exactly one second after the previous, then storedPermits is _never_ increased --
   * we would only increase it for arrivals _later_ than the expected one second.
   */

  /**
   * This implements the following function where coldInterval = coldFactor * stableInterval.
   * 这实现了以下函数，其中coldInterval = coldFactor * stableInterval。
   * coldInterval     ：令牌生成的最慢速度，此时系统允许的QPS是最低的
   * stableInterval   ：令牌生成的最快速度，此时系统允许的QPS是最高的
   * thresholdPermits ：令牌最少的个数
   * maxPermits       ：令牌最大的个数
   * warmupPeriod     ：系统预热的时间，也就是令牌数从 0 -> maxPermits 所需的时间
   *                  　　　　　　　　　也是令牌生成速 0 —> coldInterval 所需的时间
   *
   * <pre>
   *          ^ throttling
   *          |
   *    cold  +                  /
   * interval |                 /.
   *          |                / .
   *          |               /  .   ← "warmup period" is the area of the trapezoid between
   *          |              /   .     thresholdPermits and maxPermits
   *          |             /    .
   *          |            /     .
   *          |           /      .
   *   stable +----------/  WARM .
   * interval |          .   UP  .
   *          |          . PERIOD.
   *          |          .       .
   *        0 +----------+-------+--------------→ storedPermits
   *          0 thresholdPermits maxPermits
   * </pre>
   *
   * Before going into the details of this particular function, let's keep in mind the basics:
   * 在深入了解这个特定函数的细节之前，让我们先记住一些基础知识:
   * <ol>
   *   <li>The state of the RateLimiter (storedPermits) is a vertical line in this figure.
   *       RateLimiter (storedPermits) 令牌个数在图中是一条垂直线。
   *   <li>When the RateLimiter is not used, this goes right (up to maxPermits)
   *       当 RateLimiter 不使用时，令牌数会一直向右增长(直到最大许可停止增长)
   *   <li>When the RateLimiter is used, this goes left (down to zero), since if we have
   *       storedPermits, we serve from those first
   *       当 RateLimiter 在使用时，这将向左下降(最终下降到零)，因为如果我们有存储的令牌，我们将首先这里获取令牌
   *   <li>When _unused_, we go right at a constant rate! The rate at which we move to the right is
   *       chosen as maxPermits / warmupPeriod. This ensures that the time it takes to go from 0 to
   *       maxPermits is equal to warmupPeriod.
   *       当未被使用时，我们以恒定的速度前进！向右移动的速率选择为 maxPermit / warmupPeriod。
   *       这确保从 0 到 maxPermit 所花费的时间等于 warmupPeriod(预热时间)。
   *   <li>When _used_, the time it takes, as explained in the introductory class note, is equal to
   *       the integral of our function, between X permits and X-K permits, assuming we want to
   *       spend K saved permits.
   *       当使用是，它所花费的时间，正如在导论笔记中解释的，等于我们的函数的积分，在X许可和X-K许可之间，假设我们要花费K个节省下来的许可证。
   * </ol>
   *
   * <p>In summary, the time it takes to move to the left (spend K permits), is equal to the area of
   * the function of width == K.
   * 总之，向左移动的时间(也就是使用掉K个许可)，等于宽度为K的函数的面积。
   *
   * <p>Assuming we have saturated demand, the time to go from maxPermits to thresholdPermits is
   * equal to warmupPeriod. And the time to go from thresholdPermits to 0 is warmupPeriod/2. (The
   * reason that this is warmupPeriod/2 is to maintain the behavior of the original implementation
   * where coldFactor was hard coded as 3.)
   * 假设我们有饱和的需求，从 maxPermit 到 thresholdPermits 的时间等于 warmupPeriod。
   * 从 thresholdPermits 到 0 的时间是 warmupPeriod / 2 。
   * (这是 warmupPeriod/2 的原因是为了维护原始实现的行为，其中 coldFactor 被硬编码为3。)
   *
   * <p>It remains to calculate thresholdsPermits and maxPermits.
   * 接下来还需要计算 thresholdsPermits 和 maxPermit。
   *
   * <ul>
   *   <li>The time to go from thresholdPermits to 0 is equal to the integral of the function
   *       between 0 and thresholdPermits. This is thresholdPermits * stableIntervals. By (5) it is
   *       also equal to warmupPeriod/2. Therefore
   *       从 thresholdsPermits 到 0 的时间等于函数在 0 和 thresholdsPermits 之间的积分。
   *       这是 thresholdsPermits * stableIntervals。(5)它也等于 warmupPeriod/2 。因此
   *       <blockquote>
   *       thresholdPermits = 0.5 * warmupPeriod / stableInterval
   *       </blockquote>
   *   <li>The time to go from maxPermits to thresholdPermits is equal to the integral of the
   *       function between thresholdPermits and maxPermits. This is the area of the pictured
   *       trapezoid, and it is equal to 0.5 * (stableInterval + coldInterval) * (maxPermits -
   *       thresholdPermits). It is also equal to warmupPeriod, so
   *       从 maxPermits 到 thresholdPermits 的时间等于 thresholdPermits 和 maxPermits 之间函数的积分。
   *       这是图中梯形的面积，它等于0.5 * (stableInterval + coldInterval) * (maxpermit - thresholdpermit)。它也等于warmupPeriod，所以
   *       <blockquote>
   *       maxPermits = thresholdPermits + 2 * warmupPeriod / (stableInterval + coldInterval)
   *       </blockquote>
   * </ul>
   */
  static final class SmoothWarmingUp extends SmoothRateLimiter {
    /**
     * 预热所需的时间
     */
    private final long warmupPeriodMicros;
    /**
     * 从稳定区间到冷区间的直线斜率
     * The slope of the line from the stable interval (when permits == 0), to the cold interval
     * (when permits == maxPermits)
     */
    private double slope;
    /**
     * 稳定的令牌数，令牌数 <= 该值时，说明系统进入稳定期
     */
    private double thresholdPermits;
    /**
     * 固定为：3.0
     */
    private double coldFactor;

    /**
     * 系统预热构造方法
     * @param stopwatch
     * @param warmupPeriod 预热时间
     * @param timeUnit 时间单位
     * @param coldFactor coldFactor
     */
    SmoothWarmingUp(
        SleepingStopwatch stopwatch, long warmupPeriod, TimeUnit timeUnit, double coldFactor) {
      super(stopwatch);
      this.warmupPeriodMicros = timeUnit.toMicros(warmupPeriod);// 转换为微秒
      this.coldFactor = coldFactor;
    }

    /**
     * 设置速率
     * @param permitsPerSecond 每秒允许通过的请求个数，也就是每秒生成的令牌的个数
     * @param stableIntervalMicros 系统稳定时生成1个令牌的时间(y1)
     */
    @Override
    void doSetRate(double permitsPerSecond, double stableIntervalMicros) {
      // 获取当前最大令牌数，作为旧的令牌数，如果限流器运行到一半重新设置了速率，就需要重新设置令牌桶的大小了
      double oldMaxPermits = maxPermits;
      // 系统冷却时生成1个令牌的时间(y2) = 稳定生成令牌的时间 * 冷启动因子(这是一个常量)
      double coldIntervalMicros = stableIntervalMicros * coldFactor;
      // 系统稳定时的令牌数(x1) = 预热时间的一半 / 稳定速率
      thresholdPermits = 0.5 * warmupPeriodMicros / stableIntervalMicros;
      // 系统能存储的最大令牌数(x2) = 稳定的令牌数 + (2倍的预热时间) / (系统稳定时生成1个令牌的时间 + 系统冷却时生成1个令牌的时间)
      maxPermits = thresholdPermits + 2.0 * warmupPeriodMicros / (stableIntervalMicros + coldIntervalMicros);
      // 斜率 = (y2-y1)/(x2-x1) = (系统冷却时生成1个令牌的时间-系统稳定时生成1个令牌的时间)/(系统能存储的最大令牌数-系统稳定时的令牌数)
      slope = (coldIntervalMicros - stableIntervalMicros) / (maxPermits - thresholdPermits);// ()
      if (oldMaxPermits == Double.POSITIVE_INFINITY) {
        // if we don't special-case this, we would get storedPermits == NaN, below
        storedPermits = 0.0;
      } else {
        // 当前存储的令牌数 = 系统能存储的最大令牌数，也就是初始化时，令牌桶是满的。
        storedPermits =
            (oldMaxPermits == 0.0)
                ? maxPermits // initial state is cold
                : storedPermits * maxPermits / oldMaxPermits;
      }
    }

    /**
     * 根据存储的令牌数算出的系统冷却时下发令牌的时间
     * @param storedPermits 存储的令牌数
     * @param permitsToTake 需要扣减的令牌数, 如果一次性将存储的令牌数全部扣减了, 那这个数 = 存储的令牌数
     * @return 需要等待的时间
     */
    @Override
    long storedPermitsToWaitTime(double storedPermits, double permitsToTake) {
      // 存储的令牌数比稳定的令牌数大的那部分
      double availablePermitsAboveThreshold = storedPermits - thresholdPermits;
      // 等待的时间
      long micros = 0;
      // measuring the integral on the right part of the function (the climbing line)
      // 如果存储的令牌数 > 稳定的令牌数, 则说明在梯形区域
      if (availablePermitsAboveThreshold > 0.0) {
        // 超过稳定令牌数的部分, 如果比扣减的部分多, 则使用扣减的部分, 否则是存储的部分
        // 也就是扣减的令牌数里, 有多少是需要按冷却速率下发令牌的
        double permitsAboveThresholdToTake = min(availablePermitsAboveThreshold, permitsToTake);
        // TODO(cpovirk): Figure out a good name for this variable.
        double length = permitsToTime(availablePermitsAboveThreshold) + permitsToTime(availablePermitsAboveThreshold - permitsAboveThresholdToTake);
        System.out.print(" = " + length);
        //
        micros = (long) (permitsAboveThresholdToTake * length / 2.0);
        permitsToTake -= permitsAboveThresholdToTake;
      }
      // measuring the integral on the left part of the function (the horizontal line)
      micros += (long) (stableIntervalMicros * permitsToTake);
      return micros;
    }

    /**
     *
     * @param permits
     * @return
     */
    private double permitsToTime(double permits) {
      System.out.print(":" + (stableIntervalMicros + permits * slope));
      return stableIntervalMicros + permits * slope;
    }

    /**
     * 获取系统冷却时, 每个令牌生成所需要的时间, 也就是(y2)
     * @return
     */
    @Override
    double coolDownIntervalMicros() {
      return warmupPeriodMicros / maxPermits;
    }
  }

  /**
   * This implements a "bursty" RateLimiter, where storedPermits are translated to zero throttling.
   * The maximum number of permits that can be saved (when the RateLimiter is unused) is defined in
   * terms of time, in this sense: if a RateLimiter is 2qps, and this time is specified as 10
   * seconds, we can save up to 2 * 10 = 20 permits.
   *
   * 这实现了一个 "突发的" RateLimiter，其中 storedPermits 被转换为零节流。可以保存的最大许可数量(当RateLimiter未使用时)
   * 是根据时间来定义的:如果一个 RateLimiter 是 2qps，并且这个时间被指定为10秒，我们最多可以保存2 * 10 = 20个许可。
   */
  static final class SmoothBursty extends SmoothRateLimiter {
    /** The work (permits) of how many seconds can be saved up if this RateLimiter is unused? */
    final double maxBurstSeconds;

    SmoothBursty(SleepingStopwatch stopwatch, double maxBurstSeconds) {
      super(stopwatch);
      this.maxBurstSeconds = maxBurstSeconds;
    }

    @Override
    void doSetRate(double permitsPerSecond, double stableIntervalMicros) {
      double oldMaxPermits = this.maxPermits;
      maxPermits = maxBurstSeconds * permitsPerSecond;
      if (oldMaxPermits == Double.POSITIVE_INFINITY) {
        // if we don't special-case this, we would get storedPermits == NaN, below
        storedPermits = maxPermits;
      } else {
        storedPermits =
            (oldMaxPermits == 0.0)
                ? 0.0 // initial state
                : storedPermits * maxPermits / oldMaxPermits;
      }
    }

    @Override
    long storedPermitsToWaitTime(double storedPermits, double permitsToTake) {
      return 0L;
    }

    @Override
    double coolDownIntervalMicros() {
      return stableIntervalMicros;
    }
  }

  /**
   * The currently stored permits.
   * 令牌桶
   */
  double storedPermits;

  /**
   * The maximum number of stored permits.
   * 最大令牌数
   */
  double maxPermits;

  /**
   * The interval between two unit requests, at our stable rate. E.g., a stable rate of 5 permits
   * per second has a stable interval of 200ms.
   * 稳定速率下的间隔
   */
  double stableIntervalMicros;

  /**
   * The time when the next request (no matter its size) will be granted. After granting a request,
   * this is pushed further in the future. Large requests push this further than small requests.
   * 下一个令牌的发放时间
   */
  private long nextFreeTicketMicros = 0L; // could be either in the past or future

  private SmoothRateLimiter(SleepingStopwatch stopwatch) {
    super(stopwatch);
  }

  /**
   * 设置速率
   * @param permitsPerSecond 最大速率
   * @param nowMicros 当前时间
   */
  @Override
  final void doSetRate(double permitsPerSecond, long nowMicros) {
    resync(nowMicros);
    double stableIntervalMicros = SECONDS.toMicros(1L) / permitsPerSecond;// 每个令牌产生所需的时间(微秒)
    this.stableIntervalMicros = stableIntervalMicros;
    doSetRate(permitsPerSecond, stableIntervalMicros);
  }

  abstract void doSetRate(double permitsPerSecond, double stableIntervalMicros);

  @Override
  final double doGetRate() {
    return SECONDS.toMicros(1L) / stableIntervalMicros;
  }

  @Override
  final long queryEarliestAvailable(long nowMicros) {
    return nextFreeTicketMicros;
  }

  /**
   * 保留所要求的许可证数量，并返回这些许可证可以使用的时间(有一个警告)。
   * @param requiredPermits 请求的令牌数
   * @param nowMicros 当前时间
   * @return 请求允许通过的时间
   */
  @Override
  final long reserveEarliestAvailable(int requiredPermits, long nowMicros) {
    resync(nowMicros);
    // 如果当前时间比下次通过时间晚, 返回的就是当前时候, 否则返回下次通过时间
    long returnValue = nextFreeTicketMicros;
    // 令牌桶需要扣减的个数, 不能大于存储的令牌数，请求数如果大于存储的令牌数, 就用存储的令牌数, 否则就是请求数
    double storedPermitsToSpend = min(requiredPermits, this.storedPermits);
    // 新的令牌
    // 如果请求数比令牌桶的令牌数小, 则新的令牌 = 0,                  例如桶中有10个, 要拿5个, 则需要新生成的令牌肯定是0个
    // 如果请求书比令牌桶的令牌数大, 则新的令牌 = 请求数 - 令牌桶的令牌. 例如桶中有10个, 要拿15个, 则需要新生成5个
    double freshPermits = requiredPermits - storedPermitsToSpend;
    // 本次请求需要等待的时间 = 根据存储的令牌数算出的系统冷却时下发令牌的时间 + 新生成的令牌所需要的时间.
    long waitMicros =
            // 根据存储的令牌数算出的系统冷却时下发令牌的时间
            storedPermitsToWaitTime(this.storedPermits, storedPermitsToSpend)
            +
            // 新生成的令牌的时间 = 令牌数 * 最快生成令牌的时间
            (long) (freshPermits * stableIntervalMicros);

    this.nextFreeTicketMicros = LongMath.saturatedAdd(nextFreeTicketMicros, waitMicros);
    this.storedPermits -= storedPermitsToSpend;
    return returnValue;
  }

  /**
   * Translates a specified portion of our currently stored permits which we want to spend/acquire,
   * into a throttling time. Conceptually, this evaluates the integral of the underlying function we
   * use, for the range of [(storedPermits - permitsToTake), storedPermits].
   *
   * <p>This always holds: {@code 0 <= permitsToTake <= storedPermits}
   */
  abstract long storedPermitsToWaitTime(double storedPermits, double permitsToTake);

  /**
   * Returns the number of microseconds during cool down that we have to wait to get a new permit.
   */
  abstract double coolDownIntervalMicros();

  /**
   * 重新同步令牌桶
   * 基于当前时间 修改存储的的令牌数 & 下一个令牌的时间
   * 存储的令牌数
   * Updates {@code storedPermits} and {@code nextFreeTicketMicros} based on the current time.
   */
  void resync(long nowMicros) {
    // if nextFreeTicket is in the past, resync to now
    // 如果当前时间 > 下次允许通过的时间, 则当前就是下次请求允许通过的时间
    if (nowMicros > nextFreeTicketMicros) {
      // 距离上次添加令牌的时间 / 冷却时每个令牌的生成时间 = 新生成的令牌数
      double newPermits = (nowMicros - nextFreeTicketMicros) / coolDownIntervalMicros();
      storedPermits = min(maxPermits, storedPermits + newPermits);
      nextFreeTicketMicros = nowMicros;
    }
  }
}
