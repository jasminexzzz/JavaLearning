package com.jasmine.java.base.A_基础.类.引用和对象;


import java.util.HashMap;
import java.util.Map;

/**
 * @author jasmineXz
 */
public class StringQuote {
    private static final Map<String,Test> map = new HashMap<>();

    public static class Test {
        String a;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }
    }

    public static void main(String[] args) {
        Test t = new Test();
        map.put(t.getA(),t);

        map.forEach((k,v) -> {
            System.out.println("k:" + k + ", v:" + v);
        });

        t.setA("123");

        map.forEach((k,v) -> {
            System.out.println("k:" + k + ", v:" + v);
        });

    }
}
