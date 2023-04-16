package com.xzzz.Other.写着玩的小工具;

import cn.hutool.core.io.FileUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class 本地文件扫描 {

    public static void main(String[] args) {
        List<File> files = FileUtil.loopFiles("G:\\存储\\收藏\\来自网络\\视频\\M.1 欧美");
        System.out.println(files.size());


        try {
            Desktop desktop = Desktop.getDesktop();
            File file = new File("E:\\weishaixuan\\TheAwakeningP01_FullSound_2K.mp4");
            desktop.open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        for (File file : files) {
//            System.out.println(file.getAbsolutePath());
//        }
    }
}
