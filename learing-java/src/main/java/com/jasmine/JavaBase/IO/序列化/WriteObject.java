package com.jasmine.JavaBase.IO.序列化;

import java.io.*;

public class WriteObject
{
    public static void main(String[] args)
    {
        try(
                // 创建一个ObjectOutputStream输出流
                ObjectOutputStream oos = new ObjectOutputStream(
                        new FileOutputStream("C:\\Users\\Jasmine\\Desktop\\新建文本文档.txt")))
        {
            Person per = new Person("孙悟空", 500);
            // 将per对象写入输出流
            oos.writeObject(per);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}

