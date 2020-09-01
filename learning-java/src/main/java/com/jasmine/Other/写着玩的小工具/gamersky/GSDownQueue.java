package com.jasmine.Other.写着玩的小工具.gamersky;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;

public class GSDownQueue {
    private static final BlockingQueue<PicElement> queue = new LinkedBlockingQueue<>();

    private static final Set<String> pictureSet = new CopyOnWriteArraySet<>();
    private static final Set<String> faultSet = new CopyOnWriteArraySet<>();

    private boolean folderExist = false;

    protected void put (PicElement pic) {
        try {
            if (pictureSet.contains(pic.getUrl())) {
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
        String fileName = GSUtil.fileNameResolver(pic.getUrl());
        createFolder(pic.getPath());
        File file = new File(pic.getPath() + fileName);

        // 记录中包含该文件名,则不下载
        if (!pictureSet.contains(pic.getUrl())) {
            // 文件存在,则不下载
            if (!FileUtil.exist(file)) {
                HttpResponse resp = HttpRequest.get(pic.getUrl()).execute();
                byte[] bytes = resp.bodyBytes();
                try (FileImageOutputStream imageOutput = new FileImageOutputStream(file)) {
                    imageOutput.write(bytes, 0, bytes.length);//将byte写入硬盘
                } catch (IOException e) {
                    e.printStackTrace();
                }
                print(fileName,"保存成功");
                pictureSet.add(pic.getUrl()); // 放入队列
            } else {
                //print(fileName,"本地已存在");
                pictureSet.add(pic.getUrl());
            }
        } else {
            //print(fileName,"SET已存在");
        }
    }

    protected void print (String fileName, String msg) {
        System.out.println(String.format("[%s] [%s] %s %s",Thread.currentThread().getName(),pictureSet.size(),fileName,msg));
    }

    private void createFolder (String path) {
        File file = new File(path);
        if (!folderExist && !file.exists()) {
            file.mkdir();
            folderExist = true;
        }
    }
}
