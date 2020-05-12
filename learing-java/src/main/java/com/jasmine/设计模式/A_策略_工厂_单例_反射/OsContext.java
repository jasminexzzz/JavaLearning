package com.jasmine.设计模式.A_策略_工厂_单例_反射;

import com.jasmine.设计模式.A_策略_工厂_单例_反射.os.AlibabaManager;
import com.jasmine.设计模式.A_策略_工厂_单例_反射.os.OsManager;
import com.jasmine.设计模式.A_策略_工厂_单例_反射.os.QiniuManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 文件上传上下文
 *
 * @author jasmineXz
 */
public class OsContext {
    private OsManager osManager;

    /**
     * 构造上下文
     *
     * @param type 文件类型
     */
    public OsContext (String type) throws Exception {
        Class clazz = OsType.valueOf(type).getClazz();
        Method method = clazz.getDeclaredMethod("getInstance", null);
        osManager = (OsManager) method.invoke(null,null);
    }

    String put (String file) {
        return osManager.put(file);
    }

    public OsManager getOsManager() {
        return osManager;
    }
}
