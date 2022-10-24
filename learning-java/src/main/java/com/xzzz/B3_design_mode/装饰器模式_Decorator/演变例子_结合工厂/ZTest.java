package com.xzzz.B3_design_mode.装饰器模式_Decorator.演变例子_结合工厂;

/**
 * @author : jasmineXz
 */
public class ZTest {

    public static void main(String[] args) {
        ZTest.factory();
    }


    private static void normal () {

        // 1. 我要输出控制台
        Log console = new LogConsole();
        console.print();

        // 2. 我要输出控制台和文件
        System.out.println("========== 输出控制台/文件 ================");

        LogDecorator file = new LogDecoratorFile();
        file.setLog(console);
        file.print();

        // 3. 输出控制台,文件,数据库
        System.out.println("========== 输出控制台/文件/数据库 ==========");

        LogDecorator data = new LogDecoratorDataBase();
        data.setLog(file);
        data.print();
    }

    /**
     * 工厂制造
     * 工厂类中有装饰配置
     */
    private static void factory () {
        Log log = LogFactory.createLog();
        log.print();
    }
}
