package com.xzzz.A1_java.base.D_IO.基准测试;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.UUID;

public class FileUtil {

    static File getRandomFile() {
        String fileName = UUID.randomUUID().toString();
        File file = new File(fileName);
        Runtime.getRuntime().addShutdownHook(new Thread(file::delete));
        return file;
    }

    static RandomAccessFile getRandomAccessFile() throws IOException {
        File file = FileUtil.getAlreadyFillFile();
        return new RandomAccessFile(file, "r");
    }

    static FileChannel getFileChannel() throws FileNotFoundException {
        File file = FileUtil.getRandomFile();
        return new RandomAccessFile(file, "rw").getChannel();
    }

    static MappedByteBuffer getMappedByteBuffer() throws IOException {
        File file = FileUtil.getRandomFile();
        return new RandomAccessFile(file, "rw").getChannel().
                map(FileChannel.MapMode.READ_WRITE, 0, 1024 * 1024 * 1024);
    }

    static File getAlreadyFillFile() throws IOException {
        File file = getRandomFile();
        FileOutputStream fo = new FileOutputStream(file);
        byte[] arr = new byte[1024 * 1024 * 1024];
        Arrays.fill(arr, (byte) 1);
        fo.write(arr);
        return file;
    }
}
