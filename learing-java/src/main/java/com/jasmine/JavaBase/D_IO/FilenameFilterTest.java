package com.jasmine.JavaBase.D_IO;


import java.io.File;

public class FilenameFilterTest
{
    public static void main(String[] args)
    {
        File file = new File("E:\\Work\\WorkSpace\\IdeaProjects\\Study\\src\\com\\jasmine\\JavaBase\\IO");
        // 使用Lambda表达式（目标类型为FilenameFilter）实现文件过滤器。
        // 如果文件名以.java结尾，或者文件对应一个路径，返回true
        //FilenameFilter -- boolean accept(File dir, String name);
        String[] nameList = file.list((dir, name) -> name.endsWith(".txt") || new File(name).isDirectory());
        for(String name : nameList)
        {
            System.out.println(name);
        }
    }
}