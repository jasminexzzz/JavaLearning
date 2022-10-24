package com.xzzz.Other.写着玩的小工具;

import java.io.File;
import java.util.HashSet;

/**
 * 输出项目路径下的全部包
 * 去重名,去例子文件夹
 * @author : jasmineXz
 */
public class GetAllFilder {

    private static HashSet<String> EXCLUDE_PATH_MAP = new HashSet<>();

    static {
        EXCLUDE_PATH_MAP.add("E:\\WorkSpace\\Idea\\MyJava\\src\\J2EE\\java\\com\\.svn");
        EXCLUDE_PATH_MAP.add("E:\\WorkSpace\\Idea\\MyJava\\src\\J2EE\\java\\com\\jasmine\\.svn");
        EXCLUDE_PATH_MAP.add("E:\\WorkSpace\\Idea\\MyJava\\src\\J2EE\\java\\com\\jasmine\\JavaBase\\.svn");
        EXCLUDE_PATH_MAP.add(".svn");
        EXCLUDE_PATH_MAP.add("webapp");
    }

    public static void main(String[] args) {
        //调用方法
        String PATH = "E:\\WorkSpace\\Idea\\MyJava\\src";
        getFiles(PATH,-2);
    }

    /**
     * 递归获取某路径下的所有文件，文件夹，并输出
     */
    private static void getFiles(String path,int retractNum) {
        retractNum += 2;
        File file = new File(path);
        // 如果这个路径是文件夹
        if (file.isDirectory()) {
            // 获取路径下的所有文件
            File[] files = file.listFiles();
            if(files == null){
                return;
            }
            for(File f : files){
                // 如果还是文件夹 递归获取里面的文件 文件夹
                if (f.isDirectory()) {
                    if(EXCLUDE_PATH_MAP.contains(f.getPath()) || EXCLUDE_PATH_MAP.contains(f.getName()) || f.getName().contains("例"))
                        continue;
                    retract(retractNum);
                    System.out.println(f.getName());
                    getFiles(f.getPath(),retractNum);
                }
            }

        }
    }

    /**
     * 构造缩进
     * @param retractNum 缩进次数
     */
    private static void retract(int retractNum){
        for(int i = 0 ; i < retractNum ; i ++){
            System.out.print(" ");
        }
        System.out.print("- ");
    }
}
