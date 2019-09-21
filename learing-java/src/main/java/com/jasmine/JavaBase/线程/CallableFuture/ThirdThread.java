package com.jasmine.JavaBase.线程.CallableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class ThirdThread {

    public static void main(String[] args){

        // 创建Callable对象
        ThirdThread rt = new ThirdThread();
        // 先使用Lambda表达式创建Callable<Integer>引用和对象
        // 使用FutureTask来包装Callable对象
        FutureTask<Integer> task = new FutureTask<Integer>((Callable<Integer>)() -> {
            int i = 0;
//            throw new Exception("call方法可以抛出异常");

            /**/
            for ( ; i < 20 ; i++ ){
                System.out.println(Thread.currentThread().getName() + " 的循环变量i的值：" + i);
            }
            // call()方法可以有返回值
            return i;
            /**/

        });

        for (int i = 0 ; i < 20 ; i++){
            System.out.println(Thread.currentThread().getName() + " 的循环变量i的值：" + i);
            if (i == 5){
                // 实质还是以Callable对象来创建、并启动线程
                new Thread(task , "有返回值的线程").start();
            }
        }

        try{
            // 获取线程返回值
            System.out.println("子线程的返回值：" + task.get());
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

}

