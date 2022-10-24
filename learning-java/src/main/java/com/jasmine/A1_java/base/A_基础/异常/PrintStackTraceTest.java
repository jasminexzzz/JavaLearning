package com.jasmine.A1_java.base.A_基础.异常;


class SelfException extends RuntimeException
{
    SelfException(){}
    SelfException(String msg){
        super(msg);
    }
}
public class PrintStackTraceTest
{
    public static void main(String[] args){
        firstMethod();
    }

    public static void firstMethod(){
        secondMethod();
    }

    public static void secondMethod(){
        thirdMethod();
    }

    public static void thirdMethod(){
        throw new SelfException("自定义异常信息");
    }
}
