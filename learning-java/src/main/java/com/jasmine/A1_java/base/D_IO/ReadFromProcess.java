package com.jasmine.A1_java.base.D_IO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadFromProcess
{
    public static void main(String[] args)throws IOException
    {
        // 运行javac命令，返回运行该命令的子进程
        Process p = Runtime.getRuntime().exec("java -version");

        //获取可用内存
        long value = Runtime.getRuntime().freeMemory();
        System.out.println("可用内存为:"+value/1024/1024+"mb");
        //获取jvm的总数量，该值会不断的变化
        long  totalMemory = Runtime.getRuntime().totalMemory();
        System.out.println("全部内存为:"+totalMemory/1024/1024+"mb");
        //获取jvm 可以最大使用的内存数量，如果没有被限制 返回 Long.MAX_VALUE;
        long maxMemory = Runtime.getRuntime().maxMemory();
        System.out.println("可用最大内存为:"+maxMemory/1024/1024+"mb");

        int valueCpuCore = Runtime.getRuntime().availableProcessors();
        System.out.println(valueCpuCore);

        try(
                // 以p进程的错误流创建BufferedReader对象
                // 这个错误流对本程序是输入流，对p进程则是输出流
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream(),"GBK")))
        {
            String buff = null;
            // 采取循环方式来读取p进程的错误输出
            while((buff = br.readLine()) != null)
            {
                System.out.println(buff);
            }
        }
    }
}
