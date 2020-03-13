package com.jasmine.Java高级.JVM.内存模型_JMM.堆栈溢出测试;


import java.util.ArrayList;

public class 栈溢出 {

    public static void main(String[] args) {
        栈溢出 a = new 栈溢出();
        a.stack();
    }

    private void stack() {
        while (true) {
            ArrayList list = new ArrayList (2000);
        }
    }
}
