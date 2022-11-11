package com.xzzz.A1_java.base.D_IO.OptStream和Writer;


import cn.hutool.core.thread.ThreadUtil;

import java.io.*;

/**
 * 字节输出流, 将字节输出到流中
 */
public class OutputStreamTest {

    static final String filename = "C:\\WangYunFei\\GitCode\\Learning\\learning-java\\src\\main\\java\\com\\xzzz\\A1_java\\base\\D_IO\\OptStream和Writer\\target.txt";

    public static void main(String[] args) {
        writeTargetBuffered();
    }


    /**
     * 将内容直接输出到文件
     */
    private static void writeTarget() {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            // write 是个 native 方法, 直接写入到文件中的
            for (int i = 0; i < 10; i++) {
                fos.write(("输出内容" + i + "\r\n").getBytes());
            }

            // 因为文件输出流是直接输出到文件, 所以没有 flush() 方法, 效率较低
            // 该方法未实现, 无方法体
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用缓冲流包装输出流, 内容将先输出到缓冲区, 在输出到最终输出,
     */
    private static void writeTargetBuffered() {
        try {
            // append 为true将内容追加在文件后, 否则清空重写
            FileOutputStream fos = new FileOutputStream(filename, true);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            for (int i = 0; i < 10; i++) {
                bos.write(("输出内容" + i + "\r\n").getBytes());
            }
            System.out.println("未调用 flush, 不写入");
            ThreadUtil.safeSleep(3000);
            System.out.println("调用 flush, 写入");
            // flush 是写入到最终的输出终端, 不调用可能会无输出
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        try (
//                // 创建字节输入流
//                FileInputStream fis = new FileInputStream("C:\\Users\\Jasmine\\Desktop\\新建文本文档.txt");
//                // 创建字节输出流
//                FileOutputStream fos = new FileOutputStream("C:\\Users\\Jasmine\\Desktop\\newFile.txt")) ;
//        byte[] bbuf = new byte[2];
//        int hasRead = 0;
//        // 循环从输入流中取出数据
//        while ((hasRead = fis.read(bbuf)) > 0) {
//            // 每读取一次，即写入文件输出流，读了多少，就写多少。
//            fos.write(bbuf, 0, hasRead);
//            fos.flush();
//        }
//    } catch(
//    IOException ioe)
//
//    {
//        ioe.printStackTrace();
//    }
//}
}
