package com.jasmine.Other.写着玩的小工具.mergets;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 可以合并TS文件
 * <p>从一个.m3u8文件中读取.xxxxxxxxxxxx文件夹，然后找到对应文件夹合并其中的ts文件，合并结果的名称就是.m3u8的名称
 * <p>例如：
 * <p>一个文件名：abcdefg.m3u8，会通过内容找到对应文件夹
 * <pre>
 * #EXTM3U
 * #EXT-X-TARGETDURATION:18
 * #EXTINF:10.000000,
 * file:///xx/xx/.00b27f86370ec15798703299dd38a53f/0.ts
 * ... 其他省略
 * </pre>
 * <p>对应文件夹：.00b27f86370ec15798703299dd38a53f
 * <p>然后查询文件夹中的.ts文件合并成一个 abcdefg.mp4 文件
 *
 * <p>具体的路径配置在 {@link MergeConfig}
 *
 */
public class MergeTsUtil {


    public static void main(String[] args) {

        new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(String.format("总数：%s, 成功数：%s, 失败数：%s, 运行线程数：%s",
                    MergeConfig.tsNameMap.size(),
                    MergeConfig.succTsNames.size(),
                    MergeConfig.failTsNames.size(),
                    MergeConfig.mergePool.getActiveCount()
                ));
            }
        }).start();
        getAllTsName();
        getTsByTsName();
        delSuccFile();
    }

    /**
     * 获取文件名
     */
    private static void getAllTsName() {
        List<String> tsNames = FileUtil.listFileNames(MergeConfig.sourcePath);

        for (String tsName : tsNames) {
            File file = FileUtil.file(MergeConfig.sourcePath + "\\" + tsName);
            String content = FileUtil.readUtf8String(file);
            if (StrUtil.isBlank(content)) {
                MergeConfig.setFailTsName(tsName);
            }
            String[] lines = content.split("\n");
            if (lines.length < 4) {
                MergeConfig.setFailTsName(tsName);
            }
            String targetLine = lines[3];
            if (!targetLine.contains("0.ts")) {
                MergeConfig.setFailTsName(tsName);
            }
            // 最后的斜线出现的位置
            String replaceTs = targetLine.replace("/0.ts", "");
            int lastslash = StrUtil.lastIndexOfIgnoreCase(replaceTs, "/");
            String tsHashName = replaceTs.substring(lastslash + 1);
            MergeConfig.tsNameMap.put(tsHashName.replace("\r",""), tsName);
        }

        System.out.println(String.format("共找到 [%s] 个文件", tsNames.size()));
    }

    /**
     * 获取文件名中的
     */
    private static void getTsByTsName() {
        MergeConfig.tsNameMap.forEach((hashName, fileName) -> {
            String folderName = MergeConfig.sourcePath + "\\" + hashName;
            if (FileUtil.isDirectory(folderName)) {
                File[] fileArrays = FileUtil.ls(folderName);
                List<File> files = new ArrayList<>();

                for (File file : fileArrays) {
                    // 必须是ts文件
                    if (FileUtil.isFile(file) && FileUtil.pathEndsWith(file, ".ts")) {
                        files.add(file);
                    }
                }

                List<File> sortedFile = files.stream().sorted((f1, f2) -> {
                    int n1 = Integer.parseInt(f1.getName().replace(".ts",""));
                    int n2 = Integer.parseInt(f2.getName().replace(".ts",""));
                    return Integer.compare(n1, n2);
                }).collect(Collectors.toList());

                MergeConfig.mergePool.execute(new MergeTask(sortedFile, hashName, fileName));
            } else {
                MergeConfig.setFailTsName(fileName);
            }
        });
    }

    private static void delSuccFile() {
        for (String succTsName : MergeConfig.succTsNames) {
            String path = MergeConfig.sourcePath + "\\" + succTsName;
            FileUtil.del(path);
        }
    }

    private static void delFailFile() {
        for (String failTsName : MergeConfig.failTsNames) {
            String path = MergeConfig.sourcePath + "\\" + failTsName;
            FileUtil.del(path);
        }
    }

}
