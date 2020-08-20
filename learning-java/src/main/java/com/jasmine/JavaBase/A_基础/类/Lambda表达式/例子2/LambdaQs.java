package com.jasmine.JavaBase.A_基础.类.Lambda表达式.例子2;

public class LambdaQs {
    public void eat(Eat e){
        e.taste();
    }


    public void drive(Fly f){
        f.fly("晴空");
    }



    public void add(Add a){
        System.out.println("和为:" + a.add(5,3));
    }



    public static void main(String[] args) {
        LambdaQs l = new LambdaQs();

        l.eat(() -> System.out.println("eat的taste方法"));

        l.drive(weather -> {
            System.out.println("今天的天气是" + weather);
            System.out.println("飞行平稳");
        });

        l.add((a,b) -> a + b);

    }
}
