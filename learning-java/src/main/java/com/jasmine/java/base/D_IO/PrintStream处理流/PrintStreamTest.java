package com.jasmine.java.base.D_IO.PrintStream处理流;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class PrintStreamTest
{
    public static void main(String[] args)
    {
        try(
                FileOutputStream fos = new FileOutputStream("C:\\Users\\Jasmine\\Desktop\\new.txt");
                PrintStream ps = new PrintStream(fos))
        {
            // 使用PrintStream执行输出
            ps.println("普通字符串123123213ABCabc");
            // 直接使用PrintStream输出对象
            ps.println(new PrintStreamTest());

        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
}

