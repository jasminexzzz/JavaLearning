package com.jasmine.Java高级.JVM.内存模型_JMM.堆栈溢出测试;

/**
 * 堆溢出
 * 注意 :
 *      由于在Windows 平台的虚拟机中,Java 的线程是映射到操作系统的内核线程上的,所以多线程代码执行时有较大的风险,可能会导致操作系统崩溃.
 *      执行下列方法前,请保存自己的重要文件
 * @author : jasmineXz
 */
public class OutOfMemory {
    private void dontStop() {
        while (true) {
        }
    }

    //通过不断的创建新的线程使Stack内存耗尽
    public void stackLeakByThread() {
        System.out.println(123123);
//        while (true) {
//            Thread thread = new Thread(() -> dontStop());
//            thread.start();
//        }
    }

    public static void main(String[] args) {
        OutOfMemory oom = new OutOfMemory();
        oom.stackLeakByThread();
    }
}
