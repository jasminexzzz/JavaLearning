package com.xzzz.B3_design_mode.A_策略_工厂_单例_反射.os;

/**
 * @author jasmineXz
 */
public class AlibabaManager implements OsManager {

    private static volatile AlibabaManager alibabaManager;

    /**
     * 构造方法私有化,防止外部再生成新的实例
     */
    private AlibabaManager () {

    }

    public static AlibabaManager getInstance () {
        if(alibabaManager == null){
            synchronized (AlibabaManager.class){
                if(alibabaManager == null) {
                    alibabaManager = new AlibabaManager();
                }
            }
        }
        return alibabaManager;
    }

    @Override
    public void client() {

    }

    @Override
    public String put(String file) {
        return "阿里云上传文件 : " + file;
    }
}
