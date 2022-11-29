package com.xzzz.A1_java.base.D_IO.accessfile_mmap;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * 随机文件读写, 可以读取指定位置
 * <p>
 * 示例文件中的内容: 1122334455
 * <p>
 * 1~5 的数字, 每个长2
 */
public class TestRandomAccessFile {

    private static final String filePath =
            "C:\\WangYunFei\\GitCode\\Learning\\learning-java\\src\\main\\java\\com\\xzzz\\A1_java\\base\\D_IO\\TestRandomAccessFile.txt";

    public static void main(String[] args) throws IOException {
        File source = new File(filePath);
        RandomAccessFile raFile = new RandomAccessFile(source, "rwd");
        FileChannel fileChannel = raFile.getChannel();

        // 使用 mmap 映射文件
        MappedByteBuffer mapBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 10240);

        mapBuffer.put(1, (byte) 0);
        mapBuffer.put(10, (byte) 0);
        mapBuffer.put(20, (byte) 0);
        mapBuffer.put(4096, (byte) 0);

//        byte[] bytes = new byte[]{'0', '0', '0', '0', '0', '0', '0', '0', '0', '0'};
//        mapBuffer.get(bytes, 5, 5);
//        System.out.println(new String(bytes, StandardCharsets.UTF_8));

//        print(mapBuffer, (int) raFile.length());
//
//        mapBuffer.put("#".getBytes(StandardCharsets.UTF_8));
//        print(mapBuffer, (int) raFile.length());
//
//        mapBuffer.put("#".getBytes(StandardCharsets.UTF_8));
//        print(mapBuffer, (int) raFile.length());
//
//        mapBuffer.put("#".getBytes(StandardCharsets.UTF_8));
//        print(mapBuffer, (int) raFile.length());
//
//        mapBuffer.put("#".getBytes(StandardCharsets.UTF_8));
//        print(mapBuffer, (int) raFile.length());
//
//        mapBuffer.put("#".getBytes(StandardCharsets.UTF_8));
//        print(mapBuffer, (int) raFile.length());
//
//        mapBuffer.put("%%".getBytes(StandardCharsets.UTF_8), 1, 2);
//        print(mapBuffer, (int) raFile.length());

        // 将变更刷入磁盘
        mapBuffer.force();
        raFile.close();
    }

    private static void print(MappedByteBuffer mapBuffer, int length) {
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = mapBuffer.get(i);
        }
        System.out.println(new String(bytes, StandardCharsets.UTF_8));
    }

}
