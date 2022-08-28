package com.jasmine.java.base.D_IO.NIO;

import java.nio.IntBuffer;

public class TestBuffer {

    public static void main(String[] args) {

        // buffer 写数据
        IntBuffer intBuffer = IntBuffer.allocate(5);
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i + 1);
        }

        // 读写切换
        intBuffer.flip();

        // buffer 读数据
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }




    }
}
