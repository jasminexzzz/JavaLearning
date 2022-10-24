package com.jasmine.A1_java.base.A_基础.类.Transient关键字;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class TestTransient implements java.io.Serializable {

    public static void main(String[] args) {
        LoggingInfo logInfo = new LoggingInfo("MIKE", "MECHANICS");
        System.out.println(logInfo.toString());

        try {
            ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream("logInfo.out"));
            o.writeObject(logInfo);
            o.close();
        } catch(Exception e) {
            //deal with exception
        }
        //To read the object back, we can write
        try {
            ObjectInputStream in =new ObjectInputStream(new FileInputStream("logInfo.out"));
            LoggingInfo logInfoIn = (LoggingInfo)in.readObject();
            System.out.println(logInfoIn.toString());
        } catch(Exception e) {
            //deal with exception
        }
    }
}