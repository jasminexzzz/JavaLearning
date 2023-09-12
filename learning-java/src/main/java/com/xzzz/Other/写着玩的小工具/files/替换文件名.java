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
    private static final String file_path = "Q:\\下载\\日本";

    /**
     * 追踪名称是否转为大写，适用于全是番号的路径
     */
    private static final Boolean FINAL_TO_UPPER_CASE = true;

    /**
     * 非法名称会从文件名中剔除
     */
    private static final String[] invalidNames = new String[]{
            "\\[44x.me\\]",
            "\\[88k.me\\]",
            "\\[88q.me\\]",
            "\\(1pondo\\)",
            "\\[99u.me\\]",
            "\\[456k.me\\]",
            "\\[ThZu.Cc\\]",
            "fellatiojapan-",
            "hhd800.com@",
            "1024核工厂",
            "2048社区 - big2048.com@",
            "@18p2p",
            "sifangbt.com ",
            "tsbt5.com ",
            "tsbt6.com ",
            "www.ds75.xyz ",
            "x3f3.com ",
            "d5e5.com ",
            "d4b4.com ",
            "e3i3.com ",
            "f3j3.com ",
            "f5e5.com ",
            "j3d3.com ",
            "sfbt5.com ",
            "thbt5.com ",
            "thbt8.com ",
            "tsbt9.com ",
            "mfgc1.com ",
            "mfgc7.com ",
            "p5a5.com ",
            "sfbt2.com ",
            "taosebt.com ",
            "i7a7.com ",
            "mfgc5.com ",
            "tsbt5.com ",
            "mfgc5.com ",
            "hhd800.com@",
            "guochan2048.com -",
            "guochan2048.com-",
            "guochan2048.com",
            "bbs2048.org出品",
            "d5e5.com 91",
            "gc2048.com-",
            "fhdの",
            "mfgc2.com ",
            "kpxvs.com_",
            "j4f4.com ",
            "sfbt7.com ",
            "1024核工厂-",

            "-720P",
            "720P",
            "-1080P",
            "1080P",
            "高清1080p原版首发",
            "高清1080p完整版",
            "高清1080p原版",
            "高清1080p版",
            "高清1080p",
            "1080P原版",
            "1080P高清",
            "高清720P原版无水印",
            "高清720p原版首发",
            "高清720P完整版",
            "高清720p原版",
            "高清720P版",
            "720p高清版",
            "720p高清",
            "无水印",

            "w4e4.com ",
            "y7k7.com ",
            "tsbt7.com ",
            "汤不热流出",
            "2048社区 - big2048.com@",
            "2048社区 -",
            "2048社区",
            "jav20s8.com ",
            "       ",
            "hjd2048.com_",
            "hjd2048.com"
    };


    public static void main(String[] args) {
        // 递归遍历目录以及子目录中的所有文件
        List<File> files = FileUtil.loopFiles(file_path);

        for (File file : files) {

            if (rejectFileName(file.getName())) {
                continue;
            }
            //
            String newName = file.getName();
            System.out.println("文件原始名： " + newName);

            // 1. 先获取主文件名, 不包含后缀, 防止后缀被替换, 被转大写等.
            newName = FileUtil.getPrefix(newName);

            // 2. 替换名字中的非法字符
            newName = replaceInvalidNames(newName);

            // 3. 为文件增加后缀等
            newName = addSuffix(newName);

            // 4. 文件名最前面增加一个空格
            newName = " " + newName;

            // 5. 转为大写, 并增加后缀
            if (FINAL_TO_UPPER_CASE) {
                newName = newName.toUpperCase() + "." + FileUtil.getSuffix(file);
            } else {
                newName = newName + "." + FileUtil.getSuffix(file);
            }

            // 第一次重命名, 头部包含空格, 这是为了将小写转为大写
            System.out.println("文件最终名：" + newName);
            FileUtil.rename(file, newName, true);

            System.out.println("===================================================");
        }

        // 第二次重命名, 去除头部空格, 这是为了将小写转为大写
        List<File> fileTwos = FileUtil.loopFiles(file_path);
        for (File file : fileTwos) {

            if (rejectFileName(file.getName())) {
                continue;
            }
            String newName = file.getName();
            // 第二次重命名, 去除头部空格, 这是为了将小写转为大写
            newName = newName.replaceFirst(" ", "");
            FileUtil.rename(file, newName, true);
        }

    }

    /**
     * 必须是这几种文件才处理
     *
     * @param name
     * @return
     */
    private static boolean rejectFileName(String name) {
        return !StrUtil.endWith(name, ".mp4") &&
                !StrUtil.endWith(name, ".avi") &&
                !StrUtil.endWith(name, ".ts") &&
                !StrUtil.endWith(name, ".mov")
                ;
    }

    /**
     * 将名字里的非法内容替换掉
     */
    private static String replaceInvalidNames(String name) {
        for (String invalidName : invalidNames) {
            name = StrUtil.replace(name, invalidName.toLowerCase(), "");
            name = StrUtil.replace(name, invalidName.toUpperCase(), "");
        }
        // 替换头部的-
        if (StrUtil.startWith(name, "-") ||
                StrUtil.startWith(name, "@") ||
                StrUtil.startWith(name, "_")
        ) {
            name = name.substring(1);
        }
        // 替换尾部的空格
        if (StrUtil.endWith(name, " ")) {
            name = name.substring(0, name.length() - 1);
        }
        return name;
    }

    /**
     * 给文件增加后缀
     *
     * @param name
     * @return
     */
    private static String addSuffix(String name) {
        name = StrUtil.replace(name, "-C", "【中文字幕】");
        name = StrUtil.replace(name, "-c", "【中文字幕】");
        name = StrUtil.replace(name, "-4K", "【4K】");
        name = StrUtil.replace(name, "-4k", "【4K】");
        name = StrUtil.replace(name, "-不要下", "【不要下载】");
        name = StrUtil.replace(name, "_TRIM", "【已截部分】");
        name = StrUtil.replace(name, "_trim", "【已截部分】");
        name = StrUtil.replace(name, "_Trim", "【已截部分】");
        return name;
    }

    private static String addPrefix(String name) {
        return "【前缀】" + name;
    }

}
