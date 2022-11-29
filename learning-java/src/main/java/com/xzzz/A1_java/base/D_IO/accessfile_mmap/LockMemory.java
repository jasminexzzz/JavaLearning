package com.xzzz.A1_java.base.D_IO.accessfile_mmap;

import com.sun.jna.*;
import lombok.extern.slf4j.Slf4j;
import sun.nio.ch.DirectBuffer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 需要引入：
 * <pre>{@code
 * <dependency>
 *     <groupId>net.java.dev.jna</groupId>
 *     <artifactId>jna</artifactId>
 *     <version>4.2.2</version>
 * </dependency>
 * }</pre>
 */
@Slf4j
public class LockMemory {

    private static final String filePath =
            "C:\\WangYunFei\\GitCode\\Learning\\learning-java\\src\\main\\java\\com\\xzzz\\A1_java\\base\\D_IO\\TestRandomAccessFile.txt";


    public static void main(String[] args) throws IOException {
        File source = new File(filePath);
        RandomAccessFile raFile = new RandomAccessFile(source, "rwd");
        FileChannel fileChannel = raFile.getChannel();

        // 使用 mmap 映射文件
        MappedByteBuffer mapBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, raFile.length());

        final long address = ((DirectBuffer) (mapBuffer)).address();

        Pointer pointer = new Pointer(address);
        {
            // 实现是将锁住指定的内存区域避免被操作系统调到 swap 空间中。
            int ret = LibC.INSTANCE.mlock(pointer, new NativeLong(raFile.length()));
            log.info("mlock {} {} {} ret = {}", address, source.getName(), raFile.length(), ret);
        }


    }


}
