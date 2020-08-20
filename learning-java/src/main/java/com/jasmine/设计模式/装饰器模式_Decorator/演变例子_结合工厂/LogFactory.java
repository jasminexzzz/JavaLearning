package com.jasmine.设计模式.装饰器模式_Decorator.演变例子_结合工厂;

/**
 * @author jasmineXz
 */
public class LogFactory {
    private static final boolean CONSOLE  = true; // 控制台配置
    private static final boolean FILE     = false;// 文件配置
    private static final boolean DATABASE = false;// 数据库配置
    private static final boolean SMS      = true;// 发送短信

    private static Log log;
    private static LogDecorator logDecorator;

    public static Log createLog () {
        if (CONSOLE) {
            log = new LogConsole();
        }

        if (FILE) {
            if (logDecorator == null) {
                logDecorator = new LogDecoratorFile();
                logDecorator.setLog(log);
            }
        }

        if (DATABASE) {
            if (logDecorator == null) {
                logDecorator = new LogDecoratorDataBase();
                logDecorator.setLog(log);
            } else {
                LogDecorator decorator = logDecorator;
                logDecorator = new LogDecoratorDataBase();
                logDecorator.setLog(decorator);
            }
        }

        if (SMS) {
            if (logDecorator == null) {
                logDecorator = new LogDecoratorSms();
                logDecorator.setLog(log);
            } else {
                LogDecorator decorator = logDecorator;
                logDecorator = new LogDecoratorSms();
                logDecorator.setLog(decorator);
            }
        }

        return logDecorator != null ? logDecorator : log;
    }

}
