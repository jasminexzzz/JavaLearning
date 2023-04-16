package com.xzzz.Other.写着玩的小工具.files;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class 文件中的数字到最前 {

    /**
     * 文件目录, 注意替换
     */
    private static final String file_path = "Q:\\下载\\FellatioJapan";


    public static void main(String[] args) {

        // 递归遍历目录以及子目录中的所有文件
        List<File> files = FileUtil.loopFiles(file_path);
        for (File file : files) {
            System.out.println(file.getName());

            // 去掉文件后缀
            String suffix = FileUtil.getSuffix(file);

            String[] names = file.getName().replaceAll("\\." + suffix, "").split("-");
            LinkedList<String> linkName = new LinkedList<>();
            for (String name : names) {
                try {
                    Integer.valueOf(name);
                    linkName.addFirst("【" + name + "】");
                } catch (NumberFormatException e) {
                    linkName.addLast(name);

                }
            }
            StringBuilder finalName = new StringBuilder();
            for (String s : linkName) {
                finalName.append(s);
            }
            finalName.append(".mp4");
            FileUtil.rename(file, finalName.toString(), false);

        }
    }
}
