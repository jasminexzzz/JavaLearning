package com.xzzz.A1_java.high.数据库.MySql.idb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class IDBReader {

    public static void main(String[] args) {
        try {
            // create a reader for data file
            FileInputStream read = new FileInputStream(new File("D:\\WangYunFei\\Docker\\properties\\mysql\\data\\learning@002dmysql\\users.ibd"));

            // the variable will be used to read one byte at a time
            int byt;
            while ((byt = read.read()) != -1) {
                System.out.print((char) byt);
            }

            read.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
