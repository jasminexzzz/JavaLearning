package com.jasmine.A1_java.base.A_基础.注解.自定义注解.注解练习.例子1;


@AllAnnotation
@SuppressWarnings(value = "unchecked")
@OtherAnno
public class Test {
    @NoNotString String a = "1";
    @NoNotString String b = "2";
    @NoNotString String c = "3";

    public Test(){
        try {
            NoNotStringProcess.process(this);
            AllAnnotationProcess.process(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Test();
    }
}
