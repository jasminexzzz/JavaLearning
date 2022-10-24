package com.jasmine.B3_design_mode.A_策略_工厂_单例_反射;

/**
 * 实现客户上传文件,根据配置,选择不同的IAAS服务提供商,不同的提供商需要获取不同的字典来生成上传对象
 *
 * @author jasmineXz
 */
public class ZTest {
    public static void main(String[] args) throws Exception {

        // 模拟批量上传多张图片,对象单例后每次生成的连接对象都是同一个
        for (int i = 0 ; i < 5 ; i++) {
            OsContext os = new OsContext("ALIBABA");
            System.out.println("==================== 第" + i + "次 ====================");
            System.out.println(os.getOsManager().toString());
            System.out.println(os.put("头像.jpg"));
        }
    }
}
