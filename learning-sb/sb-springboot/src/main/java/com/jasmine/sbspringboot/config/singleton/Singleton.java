package com.jasmine.sbspringboot.config.singleton;

/**
 * @author jasmineXz
 */
public class Singleton {
    private String value = "123";

    private static volatile Singleton single = null;

    public static Singleton getInstance() {
        if (single == null) {
            synchronized (Singleton.class) {
                if (single == null) {
                    single = new Singleton();
                }
            }
        }
        return single;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
