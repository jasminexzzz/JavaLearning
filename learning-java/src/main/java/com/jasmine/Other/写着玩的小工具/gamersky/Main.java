package com.jasmine.Other.写着玩的小工具.gamersky;

import com.jasmine.Other.写着玩的小工具.gamersky.common.GSProperties;
import com.jasmine.Other.写着玩的小工具.gamersky.common.GSUtil;
import com.jasmine.Other.写着玩的小工具.gamersky.dto.GSRequest;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {

    static final int THREAD_NUM = Runtime.getRuntime().availableProcessors(); // CPU核心数
    static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            THREAD_NUM,
            THREAD_NUM * 2,
            200,
            TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(3)
    );


    public static void main(String[] args) {
        // 1. 启动下载线程
        for (int i = 0 ; i < THREAD_NUM - 1 ; i++) {
            executor.execute(new GSDownloadTask());
        }

        // 2. 路径解析
        String url = GSUtil.urlResolver();
        System.out.println("请求地址: " + url);
        // 3. 请求体解析
        GSRequest request = GSUtil.requestResolver();
        System.out.println("请求参数: " + request);
        // 4. 创建文件夹
        System.out.print("校验路径: ");
        GSUtil.createFolder(GSProperties.DOWNLOAD_PATH + "\\" + request.getArticleId() + "\\");
        // 5. 开始读取文件
        new GSComment(url,request).read();
    }
}