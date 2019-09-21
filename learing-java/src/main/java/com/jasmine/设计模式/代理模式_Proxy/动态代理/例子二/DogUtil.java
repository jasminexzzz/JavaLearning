package com.jasmine.设计模式.代理模式_Proxy.动态代理.例子二;

public class DogUtil {

	// 第一个拦截器方法
	public void method1() {
		System.out.println("===== 插入方法 1 =====");
	}

	// 第二个拦截器方法
	public void method2() {
		System.out.println("===== 插入方法 2 =====");
	}
}