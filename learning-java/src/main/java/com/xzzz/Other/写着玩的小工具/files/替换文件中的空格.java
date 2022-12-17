package com.xzzz.Other.写着玩的小工具.files;

import cn.hutool.core.io.FileUtil;

import java.io.File;

public class 替换文件中的空格 {

    private static final String file_path =
    "F:\\存储\\收藏\\来自网络\\视频\\有这些够了\\A.5 国产-杂项\\麻辣王子-操 良 家 短 發 小 姑 娘 ， 黑 色 絲 襪 跪 地.mp4";

    public static void main(String[] args) {
        File file = new File(file_path);
        FileUtil.rename(file, file.getName().replaceAll(" ", ""), true);
    }
}
