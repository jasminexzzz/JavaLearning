package com.jasmine.java.base.B_集合.collection.set.hashset.HashCode的计算方法;


import java.util.HashSet;
import java.util.Objects;

public class New {
    private String name;
    private int age;
    private String id;


    public New(String name,int age,String id){
        this.name = name;
        this.age = age;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof New)) {
            return false;
        }
        New New = (New) o;
        return age == New.age &&
                Objects.equals(name, New.name) &&
                Objects.equals(id, New.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, id);
    }

    public static void main(String[] args) {
        New o1 = new New("一",11,"111");
        New o2 = new New("一", 11,"111");
        //==比较的是地址，对于未重写equals的类，equals比较的也是地址
        //但有些时候我们希望能比较内容,所以String类重写了equals
        //比较地址
        System.out.println(o1==o2);
        //比较值，因为已经重写equals方法
        System.out.println(o1.equals(o2));
        HashSet<New> o = new HashSet();
        o.add(o1);
        o.add(o2);
        System.out.println(o);
    }


}