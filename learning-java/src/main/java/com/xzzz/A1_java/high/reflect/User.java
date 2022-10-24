package com.xzzz.A1_java.high.reflect;

public class User {
    private String name;

    public User(String name) {
        this.name = name;
    }

    public String printName(int age) {
        return "名字叫:" + this.name + ", 年龄:" + age;
    }
}
