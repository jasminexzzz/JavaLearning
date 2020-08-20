package com.jasmine.Other.MyUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 按一定规则输出一个String到文件中
 */
public class OutputToTxt {

    public static StringBuilder createStr(int check) throws Exception {
        StringBuilder str = new StringBuilder("");
        switch (check){
            case 1:{
                for(int i = 1 ; i <=26 ; i ++){
                    //直接强转输出的是ASCII码，大写字母A在65位，小写字母a在97位
                    for(int j = 0 ; j < 10 ; j ++ ){
                        str.append((char)(64+i));
                    }
                    str.append("\r\n");
                }
                return str;
            }
            default:
                throw new Exception("输入的标识无效!");
        }
    }

    public static void main(String[] args) throws Exception {
        StringBuilder str = createStr(1);

        System.out.println(str);

        File file1 = new File("C:\\Users\\Jasmine\\Desktop\\ABC等.txt");
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file1));

            long s= System.currentTimeMillis();
            //防止大于1万条时出错,循环处理
            bw.write(str.toString());
            bw.flush();
            //buffer = new StringBuffer();
            long e= System.currentTimeMillis();
            System.out.println(e-s);
        } catch (IOException e1) {
            e1.printStackTrace();
        }finally {
            if(bw != null){
                bw.close();
            }
        }
    }
}
