package com.jasmine.JavaBase.A_基础.类.ToString方法;

class Test {

    private String color;
    private double weight;
    public Test(){}

    public Test(String color,double weight){
        this.color = color;
        this.weight = weight;
    }

    //一般out.println(Object) 和 System.out.println(Object)，其中输出的都是Object.toString()方法。重写toString()方法，可以输出自己想要的文字信息
    /*public String toString(){
        return "color:" + color + "weight" + weight;
    }*/
}

