package com.jasmine.java.base.A_基础.类.Transient关键字;

import java.util.Date;

public class LoggingInfo implements java.io.Serializable {

    private Date loggingDate = new Date();

    private String uid;

    private transient String pwd;

    LoggingInfo(String user, String password) {
        uid = user;
        pwd = password;
    }

    public String toString() {
        String password=null;
        if(pwd == null) {
            password = "NOT SET";
        }
        else {
            password = pwd;
        }
        return"logon info: \n   " + "user: " + uid +
                "\n   logging date : " + loggingDate.toString() +
                "\n   password: " + password;
    }
}