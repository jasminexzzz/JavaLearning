package com.xzzz.sentinelzerolearning.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author wangyf
 * @since 2.0.1
 */
@Component
public class RUtil {

    private static Environment environment;

    @Autowired
    public void setEnv(Environment env) {
        RUtil.environment = env;
    }

    public static R succ(Object data) {
        R r = new R();
        r.setCode("ok");
        r.setData(data);
        r.setPort(environment.getProperty("server.port"));
        return r;
    }

}
