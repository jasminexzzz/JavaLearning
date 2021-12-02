package com.jasmine.java.base.A_基础.类.枚举;

public enum SingleEnum {
    A("1"),
    B("2"),
    C("3"),
    D("4");

    private String id;


    SingleEnum(String s) {
        this.id = s;
    }

    public String getId() {
        return id;
    }
}
