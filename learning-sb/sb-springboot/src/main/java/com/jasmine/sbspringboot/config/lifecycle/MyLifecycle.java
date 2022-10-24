package com.xzzz.sbspringboot.config.lifecycle;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

/**
 * @author wangyf
 */
@Slf4j
@SpringBootTest(
    classes = {MyLifecycle.class},
    webEnvironment = SpringBootTest.WebEnvironment.NONE // 不启动WEB容器
)
@Component
public class MyLifecycle implements SmartLifecycle {

    private volatile boolean isRunning = false;

    /**
     * 如果该 Lifecycle 类所在的上下文在调用 refresh 时,希望能够自己自动进行回调，则返回 true
     * false 的值表明组件打算通过显式的 start() 调用来启动，类似于普通的 Lifecycle 实现。
     * @return 默认为true
     */
    @Override
    public boolean isAutoStartup() {
        return true;
    }



    /**
     * 返回调用顺序
     * @return 调用顺序, 越小越优先
     */
    @Override
    public int getPhase() {
        return -1;
    }



    /**
     * 开始生命周期
     */
    @Override
    public void start() {
        log.warn("start");
        this.isRunning = true;
    }



    /**
     * 注意, 如果你重写了该方法, 那么需要关注是否需要在该方法中调用 {@link SmartLifecycle#stop()}, 以便执行某些关闭逻辑, 并且
     * callback.run() 是必须执行的, 否则代码不会继续进行
     * @param callback 一个关闭线程
     */
    @Override
    public void stop(Runnable callback) {
        log.warn("stop runnable");
        stop();
        callback.run();
    }



    /**
     * 注意, 该方法是不会被生命周期调用的, 实际生命周期调用的是 {@link SmartLifecycle#stop(Runnable)},
     * 在 {@link SmartLifecycle#stop(Runnable)} 的默认实现中会调用 {@link SmartLifecycle#stop()}, 所以如果重写了
     * {@link SmartLifecycle#stop(Runnable)}, 那么你可能需要关注你是否需要在重写中调用 {@link SmartLifecycle#stop()}
     */
    @Override
    public void stop() {
        log.warn("stop");
        this.isRunning = false;
    }



    @Override
    public boolean isRunning() {
        log.warn("判断 isRunning: {}", this.isRunning);
        return isRunning;
    }



    @Test
    public void test() {

    }
}
