package com.xzzz.Other.写着玩的小工具.files;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class 替换文件名 {

    /**
     * 文件目录, 注意替换
     */
    private static final String file_path = "G:\\存储\\收藏\\视频\\H\\I.2 日本NOPQRSTUVWXYZ\\ZZZ 不要下";

    private static final Set<String> set = new HashSet<>();

    /**
     * 非法名称会从文件名中剔除
     */
    private static final String[] invalidNames = new String[]{
            "\\[44x.me\\]",
            "\\[88k.me\\]",
            "\\[88q.me\\]",
            "\\(1pondo\\)",
            "hhd800.com@",
            "1024核工厂",
            "2048社区 - big2048.com@",
            "@18P2P"
    };

    public static void main(String[] args) {
        // 递归遍历目录以及子目录中的所有文件
        List<File> files = FileUtil.loopFiles(file_path);

        for (File file : files) {

            if (!StrUtil.endWith(file.getName(), ".mp4")) {
                continue;
            }
            //
            String newName = file.getName();
            System.out.println("文件原始名：" + newName);

            // 1. 先获取主文件名, 不包含后缀, 防止后缀被替换, 被转大写等.
            newName = FileUtil.getPrefix(newName);

            // 2. 替换名字中的非法字符
            newName = replaceInvalidNames(newName);

            // 3. 为文件增加后缀等
            newName = addSuffix(newName);

            // 4. 文件名最前面增加一个空格
            newName = " " + newName;

            // 增加后缀
            newName = newName.toUpperCase() + "." + FileUtil.getSuffix(file);

            // 第一次重命名, 头部包含空格, 这是为了将小写转为大写
            System.out.println("文件最终名：" + newName);
            FileUtil.rename(file, newName, true);

            System.out.println("===================================================");
        }

        List<File> fileTwos = FileUtil.loopFiles(file_path);

        for (File file : fileTwos) {

            if (!StrUtil.endWith(file.getName(), ".mp4")) {
                continue;
            }
            String newName = file.getName();
            // 第二次重命名, 去除头部空格, 这是为了将小写转为大写
            newName = newName.replaceFirst(" ", "");
            FileUtil.rename(file, newName, true);
        }


//        List<File> fileTxt = FileUtil.loopFiles(file_path);
//        for (File file : fileTxt) {
//            if (!StrUtil.endWith(file.getName(), ".txt") && !StrUtil.endWith(file.getName(), ".TXT")) {
//                continue;
//            }
//
//            if (!file.getName().contains("不要下")) {
//                continue;
//            }
//
//            String newName = file.getName();
//            System.out.println("文件原始名：" + newName);
//
//            // 1. 先获取主文件名, 不包含后缀, 防止后缀被替换, 被转大写等.
//            newName = FileUtil.getPrefix(newName);
//            newName = newName.replaceAll("不要下-", "");
//            newName = newName + "【不要下载】";
//            newName = newName.toUpperCase() + "." + FileUtil.getSuffix(file);
//            FileUtil.rename(file, newName, true);
//        }

    }

    /**
     * 将名字里的非法内容替换掉
     */
    private static String replaceInvalidNames(String name) {
        for (String invalidName : invalidNames) {
            name = name.replaceAll(invalidName, "");
        }
        return name;
    }

    private static String addSuffix(String name) {
        name = name.toUpperCase();
        name = name.replaceAll("-C","【中文字幕】");
        name = name.replaceAll("-4K", "【4K】");
        name = name.replaceAll("-不要下", "【不要下载】");
        name = name.replaceAll("_TRIM", "【已截部分】");
        return name;
    }

    private static String addPrefix(String name) {
        return "【前缀】" + name;
    }



}
