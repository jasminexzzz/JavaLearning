package com.jasmine.JavaBase.A_基础.类.变量;

public class 局部变量 {
    public String name = "全局";

    public void getName() {
        String name = "局部";
        System.out.println(this.name);
    }
}
