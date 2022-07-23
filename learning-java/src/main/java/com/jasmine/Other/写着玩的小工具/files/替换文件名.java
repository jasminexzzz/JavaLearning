package com.jasmine.Other.写着玩的小工具.files;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class 替换文件名 {

    // 文件目录
    private static final String file_path = "G:\\存储\\收藏\\来自网络\\视频\\有这些够了";
    private static final Set<String> set = new HashSet<>();

    public static void main(String[] args) {
        // 递归遍历目录以及子目录中的所有文件
        List<File> files = FileUtil.loopFiles(file_path);
        for (File file : files) {
//            // 替换主名字
            String newName = replaceName(FileUtil.getPrefix(file));
            if (set.contains(newName)) {
                newName = newName + "#" + UUID.fastUUID().toString(true).toUpperCase();
            } else {
                set.add(newName);
            }
            // 为文件补充后缀
            newName = newName + "." + FileUtil.getSuffix(file);
            System.out.println(newName);
            FileUtil.rename(file, newName, false);
        }
    }

    // 替换名称
    private static String replaceName(String name) {
        return name.replace("【精华版】-高清原版无水","");
    }

}
