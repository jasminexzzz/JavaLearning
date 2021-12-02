package com.jasmine.java.base.D_IO.缓存流;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BuffReaderTest {
    public static void main(String[] args)
    {
        try(
            // 将Sytem.in对象转换成Reader对象
            InputStreamReader reader = new InputStreamReader(System.in);
            // 将普通Reader包装成BufferedReader
            BufferedReader br = new BufferedReader(reader))
        {
            String line = null;
            // 采用循环方式来一行一行的读取
            while ((line = br.readLine()) != null)
            {
                // 如果读取的字符串为"exit"，程序退出
                if (line.equals("exit"))
                {
                    System.exit(1);
                }
                // 打印读取的内容
                System.out.println("输入内容为:" + line);
            }
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
}
