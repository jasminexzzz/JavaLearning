package com.xzzz.B3_design_mode.A_策略_工厂_单例_反射.os;

/**
 * OS 对象
 *
 * @author jasmineXz
 */
public interface OsManager {

    /**
     * 构造对象
     */
    void client();

    /**
     * 上传文件
     * @param file
     */
    String put(String file);
}
