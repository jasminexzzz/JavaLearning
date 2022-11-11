package com.xzzz.A1_java.base.D_IO.NIO;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.channels.FileChannel;

@Slf4j
public class TestSendFile {

    /**
     * 把文件1复制到文件2
     */
    public static void copyByTransferTo(File source, File target) {
        try (FileChannel inChannel = new FileInputStream(source).getChannel();
             FileChannel outChannel = new FileOutputStream(target).getChannel()) {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copy(File source, File target) {
        FileUtil.copy(source, target, false);
    }


    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        File source = new File("D:\\WangYunFei\\TestFile\\VIDEO-1590MB.mp4");
        File target = new File("D:\\WangYunFei\\TestFile\\VIDEO-1590MB_COPY.mp4");
        copyByTransferTo(source, target);
        System.out.println(System.currentTimeMillis() - start);

        System.out.println("=============");

        long start1 = System.currentTimeMillis();
        File source1 = new File("D:\\WangYunFei\\TestFile\\VIDEO-1590MB.mp4");
        File target1 = new File("D:\\WangYunFei\\TestFile\\VIDEO-1590MB_COPY1.mp4");
        copy(source1, target1);
        System.out.println(System.currentTimeMillis() - start1);
    }

}
