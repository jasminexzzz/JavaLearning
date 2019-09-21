package com.jasmine.设计模式.代理模式_Proxy.动态代理.例子二;

public class GunDog implements Dog {

	// 实现info()方法，仅仅打印一个字符串
	public void info() {
		System.out.println("我是一只猎狗");
	}

	// 实现run()方法，仅仅打印一个字符串
	public void run() {
		System.out.println("我奔跑迅速");
	}

    // 实现run()方法，仅仅打印一个字符串
    public String getName(DogBean dogb) {
        return "狗的名字叫:" + dogb.getName();
    }
}


