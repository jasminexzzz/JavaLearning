package com.jasmine.设计模式.A_策略_工厂_单例_反射;

import com.jasmine.设计模式.A_策略_工厂_单例_反射.os.AlibabaManager;
import com.jasmine.设计模式.A_策略_工厂_单例_反射.os.QiniuManager;

/**
 * 对象存储类型
 *
 * @author jasmineXz
 */
public enum OsType {
    /**
     * 阿里巴巴
     */
    ALIBABA(AlibabaManager.class),

    /**
     * 七牛
     */
    QINIU(QiniuManager.class);

    Class clazz;

    OsType(Class clazz) {
        this.clazz = clazz;
    }


    public Class getClazz() {
        return clazz;
    }
}
