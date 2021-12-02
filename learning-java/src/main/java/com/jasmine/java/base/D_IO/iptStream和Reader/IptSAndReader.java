package com.jasmine.java.base.D_IO.iptStream和Reader;

import java.io.*;


/**
 * InputStream是表示字节输入流的所有类的超类
 * Reader是用于读取字符流的抽象类
 * InputStream提供的是字节流的读取，而非文本读取，这是和Reader类的根本区别。
 * 即用Reader读取出来的是char数组或者String ，使用InputStream读取出来的是byte数组。
 * 弄清了两个超类的根本区别，再来看他们底下子类的使用，这里只对最常用的几个说明
 */
public class IptSAndReader {

    /**
     * 字节流
     * InputStream
     * |__FileInputStream
     *
     */

    /*
     * 读取中文乱码
     * @throws IOException
     */
    public static void IptsNotReadChinese() throws IOException {

        System.out.println("InputStream");
        // 创建字节输入流
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("C:\\Users\\Jasmine\\Desktop\\新建文本文档.txt");
            // 创建一个长度为1024的“竹筒”
            byte[] bbuf = new byte[3];
            // 用于保存实际读取的字节数
            int hasRead = 0;
            // 使用循环来重复“取水”过程
            int num = 0;
            while ((hasRead = fis.read(bbuf)) > 0 )
            {
                // 取出“竹筒”中水滴（字节），将字节数组转换成字符串输入！
                System.out.println(new String(bbuf , 0 , hasRead ));
                num++;
            }
            System.out.println(num);
            // 关闭文件输入流，放在finally块里更安全
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fis.close();
        }
    }


    /**
     * 字符流
     * Reader
     * |——BufferedReader
     * |__InputStreamReader
     *   |__FileReader
     */


    /**
     * BufferedReader
     * 构造方法摘要（需要传入字符流对象）
     *   |__BufferedReader (Reader  in)
     *       创建一个使用默认大小输入缓冲区的缓冲字符输入流。
     *   |__BufferedReader (Reader  in, int sz)
     *       创建一个使用指定大小输入缓冲区的缓冲字符输入流。
     *
     * @throws IOException
     */
    public static void bufferedReaderReadChinese() throws IOException {
        FileReader rf = null;
        InputStreamReader fir = null;
        BufferedReader bfr = null;
        try{
            //rf = new FileReader("C:\\Users\\Jasmine\\Desktop\\新建文本文档 (2).txt");
            fir = new InputStreamReader(new FileInputStream("C:\\Users\\Jasmine\\Desktop\\新建文本文档.txt"),"GBK");
            bfr = new BufferedReader(fir);
            char[] ch = new char[10];

            int num = 0;
            // 用于保存实际读取的字符数
            int hasRead = 0;
            // 使用循环来重复“取水”过程
            while ((hasRead = bfr.read(ch)) > 0 ){
                // 取出“竹筒”中水滴（字符），将字符数组转换成字符串输入！
                System.out.print(new String(ch , 0 , hasRead));
                num++;
            }
            System.out.println("\n"+num);
        } catch (Exception e){
        } finally {
            if(rf != null)
                rf.close();
            if(bfr != null)
                bfr.close();
            if(fir != null)
                fir.close();

        }
    }

    /**
     * InputStreamReader读取中文
     * 是字节流通向字符流的桥梁：它使用指定的 charset 读取字节并将其解码为字符。
     * 它使用的字符集可以由名称指定或显式给定，或者可以接受平台默认的字符集。
     */
    public static void inputStreamReaderReadChinese(){
        //最大特点是可以指定字符编码,这是其他类所不能的
        try(
            InputStreamReader fr = new InputStreamReader(
                    new FileInputStream("C:\\Users\\Jasmine\\Desktop\\新建文本文档.txt"),
                    "GBK")){

            //
            char[] cbuf = new char[10];
            int num = 0;
            // 用于保存实际读取的字符数
            int hasRead = 0;
            // 使用循环来重复“取水”过程
            while ((hasRead = fr.read(cbuf)) > 0 ){
                // 取出“竹筒”中水滴（字符），将字符数组转换成字符串输入！
                System.out.print(new String(cbuf , 0 , hasRead));
                num++;
            }
            System.out.println("\n"+num);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }


    /**
     * FileReader
     * FileReader会使用系统默认的字符集，若字符集不符时读取会乱码
     * @throws IOException
     */
    public static void FileReaderReadChinese() throws IOException {
        FileReader fr = null;


        try {
            //该文件为GBK格式，而系统默认是UTF-8，所以读取会乱码
            //fr = new FileReader("C:\\Users\\Jasmine\\Desktop\\新建文本文档.txt");

            //该文件为UTF-8格式，不会乱码
            fr = new FileReader("C:\\Users\\Jasmine\\Desktop\\新建文本文档 (2).txt");

            char[] ch = new char[10];

            int num = 0;
            // 用于保存实际读取的字符数
            int hasRead = 0;
            // 使用循环来重复“取水”过程
            while ((hasRead = fr.read(ch)) > 0 ){
                // 取出“竹筒”中水滴（字符），将字符数组转换成字符串输入！
                System.out.print(new String(ch , 0 , hasRead));
                num++;
            }
            System.out.println("\n"+num);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fr != null)
                fr.close();
        }

    }


    public static void main(String[] args) throws IOException {
        FileReaderReadChinese();
    }
}
