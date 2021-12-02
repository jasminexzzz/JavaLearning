package com.jasmine.java.base.D_IO.OptStream和Writer;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileOutputStreamTest
{
    public static void main(String[] args)
    {
        try(
                // 创建字节输入流
                FileInputStream fis = new FileInputStream("C:\\Users\\Jasmine\\Desktop\\新建文本文档.txt");
                // 创建字节输出流
                FileOutputStream fos = new FileOutputStream("C:\\Users\\Jasmine\\Desktop\\newFile.txt"))
        {
            byte[] bbuf = new byte[2];
            int hasRead = 0;
            // 循环从输入流中取出数据
            while ((hasRead = fis.read(bbuf)) > 0 )
            {
                // 每读取一次，即写入文件输出流，读了多少，就写多少。
                fos.write(bbuf , 0 , hasRead);
            }
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
}
