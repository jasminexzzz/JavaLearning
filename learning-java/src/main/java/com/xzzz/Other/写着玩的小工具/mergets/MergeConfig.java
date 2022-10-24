package com.xzzz.Other.写着玩的小工具.mergets;


import cn.hutool.core.collection.ConcurrentHashSet;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MergeConfig {
    static final String typeByM3U8   = "M3U8";
    static final String typeByFOLDER = "FOLDER";
    static final String type = typeByFOLDER;
    // 保存.m3u8的和文件夹的路径
    static final String sourcePath = "G:\\tsmerge\\m3u8";
    // 合并结果保存的路径
    static final String targetPath = "G:\\tsmerge\\m3u8target";




    // ts hash名称:ts 文件中文名称
    static final Map<String, String> tsNameMap = new HashMap<>();
    static final Set<String> failTsNames = new ConcurrentHashSet<>();
    static final Set<String> succTsNames = new ConcurrentHashSet<>();
    static final String TS = ".ts";
    // 合并文件的线程池
    static final ThreadPoolExecutor mergePool = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
    // 需要过滤的文件名
    static final List<String> clearContent = new ArrayList<String>() {{
        this.add("Chinese homemade video");
        this.add(".m3u8");
        this.add(" ");
    }};


    static void setFailTsName(String tsName) {
        System.out.println(String.format("\033[31;1m%s\033[0m", tsName));
        failTsNames.add(tsName);
    }

    static void setSuccTsName(String tsName) {
        succTsNames.add(tsName);
    }

    static String clearTsName(String tsName) {
        for (String s : clearContent) {
            tsName = tsName.trim().replace(s, "");
        }
        return tsName;
    }
}
