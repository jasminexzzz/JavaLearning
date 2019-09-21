package com.jasmine.设计模式.装饰器模式_Decorator.演变例子;

/**
 * @author : jasmineXz
 */
public class LogImpl2 implements Log {

    @Override
    public void printLog() {
        System.out.println("打印到控制台");
    }

    public void printToFile(){
        this.printLog();
        this.saveFile();
    }


    public void printToDatabase(){
        this.printLog();
        this.saveDataBase();
    }


    public void printToFileAndDataBase(){
        this.printLog();
        this.saveFile();
        this.saveDataBase();
    }

    public void saveFile(){
        System.out.println("输入到文件");
    }

    public void saveDataBase(){
        System.out.println("保存到数据库");
    }


    public static void main(String[] args) {
        LogImpl2 a = new LogImpl2();
        a.printToFileAndDataBase();
    }

}
