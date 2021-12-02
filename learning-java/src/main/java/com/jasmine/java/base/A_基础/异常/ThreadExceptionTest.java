package com.jasmine.java.base.A_基础.异常;


public class ThreadExceptionTest implements Runnable
{
    public void run(){
        firstMethod();
    }

    public void firstMethod(){
        secondMethod();
    }

    public void secondMethod(){
        int a = 5;
        int b = 0;
        int c = a / b;
    }
    public static void main(String[] args)
    {
        try {
            new Thread(new ThreadExceptionTest()).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
