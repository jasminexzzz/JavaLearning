package com.jasmine.Other.写着玩的小工具.gamersky;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.jasmine.Other.写着玩的小工具.gamersky.common.GSUtil;
import com.jasmine.Other.写着玩的小工具.gamersky.dto.PicElement;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class GSDownloadQueue {
    private static final BlockingQueue<PicElement> queue = new LinkedBlockingQueue<>();
    private static final Map<String,PicElement> picMap = new ConcurrentHashMap<>();
//    private static volatile boolean folderExist = false;

    protected void put (PicElement pic) {
        try {
            if (picMap.containsKey(pic.getUrl())) {
                // print(GSUtil.fileNameResolver(url),"不放入队列");
                return;
            }
            queue.put(pic);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    protected void take () {
        try {
            PicElement pic = queue.take();
            download(pic);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件
     * @param pic 图片
     */
    private void download (PicElement pic) {

//        System.out.println(Thread.currentThread().getName() + "外部判断" + !folderExist);
//        if (!folderExist) {
//            System.out.println(Thread.currentThread().getName() + "内部判断" + !folderExist);
//            createFolder(pic.getPath());
//        }

        String fileName = GSUtil.fileNameResolver(pic.getUrl());

        /*
         记录中包含该文件名,则不下载
         该方法非线程安全,可能存在相同文件覆盖情况,不影响图片下载
         当文件名放入map中后便不会覆盖
         */
        if (!picMap.containsKey(pic.getUrl())) {
            File file = new File(pic.getPath() + fileName);
            // 文件存在,则不下载
            if (!FileUtil.exist(file)) {
                HttpResponse resp = HttpRequest.get(pic.getUrl()).execute();
                byte[] bytes = resp.bodyBytes();
                try (FileImageOutputStream imageOutput = new FileImageOutputStream(file)) {
                    imageOutput.write(bytes, 0, bytes.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                print(fileName,"保存成功");
                picMap.put(pic.getUrl(),pic); // 放入队列
            } else {
                //print(fileName,"本地已存在");
                picMap.put(pic.getUrl(),pic);
            }
        } else {
            //print(fileName,"SET已存在");
        }
    }

    protected void print (String fileName, String msg) {
        System.out.println(String.format("[%s] [%s] %s %s",Thread.currentThread().getName(),picMap.size(),fileName,msg));
    }

//    private static synchronized void createFolder (String path) {
//        File file = new File(path);
//        if (!folderExist && !file.exists()) {
//            file.mkdir();
//            folderExist = true;
//            System.out.println("===========" + folderExist);
//        }
//    }
}
