package com.xzzz.B3_design_mode.A_策略_工厂_单例_反射.os;

/**
 * @author jasmineXz
 */
public class QiniuManager implements OsManager {

    private static volatile QiniuManager qiniuManager;

    /**
     * 构造方法私有化,防止外部再生成新的实例
     */
    private QiniuManager () {

    }

    public static QiniuManager getInstance () {
        if(qiniuManager == null){
            synchronized (AlibabaManager.class){
                if(qiniuManager == null) {
                    qiniuManager = new QiniuManager();
                }
            }
        }
        return qiniuManager;
    }

    @Override
    public void client() {

    }

    @Override
    public String put(String file) {
        return "七牛云上传文件 : " + file;
    }
}
