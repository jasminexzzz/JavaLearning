package com.jasmine.A1_java.base.B_集合.collection.set.hashset;

import java.util.HashSet;

class A{
    public boolean equals(Object obj) {
        return true;
    }
}
// 类B的hashCode()方法总是返回1，但没有重写其equals()方法
class B{
    public int hashCode() {
        return 1;
    }
}
// 类C的hashCode()方法总是返回2，且重写其equals()方法总是返回true
class C{
    public int hashCode() {
        return 2;
    }
    public boolean equals(Object obj) {
        return true;
    }
}
public class HashSetTest{
    public static void main(String[] args) {
        HashSet books = new HashSet();
        // 分别向books集合中添加两个A对象，两个B对象，两个C对象
        books.add(new A());
        books.add(new A());
        books.add(new B());
        books.add(new B());
        books.add(new C());
        books.add(new C());
        System.out.println(books.toString());
    }
}
