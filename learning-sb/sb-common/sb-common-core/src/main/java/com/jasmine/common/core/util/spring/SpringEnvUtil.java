package com.jasmine.common.core.util.spring;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Spring 环境配置工具类
 *
 * @author wangyf
 * @since 1.2.2
 */
@Component
@Lazy(false)
public class SpringEnvUtil implements EnvironmentAware {

    private static Environment environment;

    /** 激活的配置文件配置 */
    public static final String ENV_SPRING_PROFILE_ACTION = "spring.profiles.active";
    /** JAR包版本 */
    public static final String ENV_JAR_VERSION = "jar.version";
    /** 端口 */
    public static final String ENV_PORT = "server.port";
    /**
     * 所有的模块都要在 POM 文件中添加 description 标签, 来表明该模块的功能
     */
    public static final String ENV_LEARNING_DESCRIPTION = "learning.description";
    /**
     * 所有的模块都要在 POM 文件中添加 frameworkVersion 标签, 来表明该模块使用的组件的版本
     */
    public static final String ENV_LEARNING_FRAMEWORK_VERSION = "learning.frameworkVersion";

    @Override
    public void setEnvironment(Environment environment) {
        SpringEnvUtil.environment = environment;
    }

    public static String get (String key) {
        return environment.getProperty(key);
    }

    public static String getPort() {
        return environment.getProperty(ENV_PORT);
    }

    public static String getDesc() {
        return environment.getProperty(ENV_LEARNING_DESCRIPTION);
    }

    public static String getFrameworkVersion() {
        return environment.getProperty(ENV_LEARNING_FRAMEWORK_VERSION);
    }
}
