package com.jasmine.java.high.JVM.内存模型_JMM.堆栈溢出测试;

/**
 * 栈溢出
 * 递归调用自己,调用太深了所以栈帧太多溢出了
 * @author : jasmineXz
 */
public class StackOverFlow {
    public static void main(String[] args){
        new StackOverFlow().test(1);
    }

    private void test(int i) {
        System.out.println("run..." + i);
        test(++i);
    }
}
