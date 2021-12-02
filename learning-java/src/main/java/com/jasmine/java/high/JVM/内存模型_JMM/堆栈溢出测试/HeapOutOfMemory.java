package com.jasmine.java.high.JVM.内存模型_JMM.堆栈溢出测试;

import java.util.ArrayList;
import java.util.List;

/**
 * 堆溢出
 * @author : jasmineXz
 */
public class HeapOutOfMemory {
    public static void main(String[] args) {
        List list = new ArrayList();
        while (true) {
            list.add(new HeapOutOfMemory());
        }
    }
}
