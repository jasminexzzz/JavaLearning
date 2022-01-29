package com.jasmine.Other.写着玩的小工具.mergets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MergeTask implements Runnable {

    private List<File> tss;
    private String hashName;
    private String fileName;

    MergeTask(List<File> tss, String hashName, String fileName) {
        this.tss = tss;
        this.hashName = hashName;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        mergeTo();
    }

    private void mergeTo() {

        File file = new File(MergeConfig.targetPath + "\\" + MergeConfig.clearTsName(fileName) + ".mp4");
        try {
            if (file.exists()) {
//                return;
                file.delete();
            } else {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] b = new byte[4096];
            for (File f : tss) {
                FileInputStream fileInputStream = new FileInputStream(f);
                int len;
                while ((len = fileInputStream.read(b)) != -1) {
                    fileOutputStream.write(b, 0, len);
                }
                fileInputStream.close();
                fileOutputStream.flush();
            }
            fileOutputStream.close();

            MergeConfig.setSuccTsName(fileName);
            MergeConfig.setSuccTsName(hashName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
