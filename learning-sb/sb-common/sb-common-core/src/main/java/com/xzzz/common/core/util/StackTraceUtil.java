package com.xzzz.common.core.util;

/**
 * @author wangyf
 */
public class StackTraceUtil {

    public static String lastMethodName() {
        Throwable ex = new Throwable();
        StackTraceElement[] stackElements = ex.getStackTrace();
        if (stackElements.length >= 3) {
            return stackElements[2].getMethodName();
        }
        return "无上级方法";
    }
}
