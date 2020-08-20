package com.jasmine.Other.写着玩的小工具;

import java.io.File;

/**
 * @author : jasmineXz
 */
public class GetAllFils {
    static String path = "F:\\02_收藏\\01_图片\\999_ProjectFile\\Images\\picture\\test";
    static String path1 = "picture/test/";


    public static void main(String[] args) {
        //调用方法
        String PATH = path;
        getFiles(PATH,-2);
    }

    /**
     * 递归获取某路径下的所有文件，文件夹，并输出
     */
    private static void getFiles(String path,int retractNum) {
        File file = new File(path);
        // 如果这个路径是文件夹
        if (file.isDirectory()) {
            // 获取路径下的所有文件
            File[] files = file.listFiles();
            if(files == null){
                return;
            }
            for(File f : files){
                System.out.println("'http://localhost:8080/"+path1+f.getName()+"',");
            }
        }
    }
}
