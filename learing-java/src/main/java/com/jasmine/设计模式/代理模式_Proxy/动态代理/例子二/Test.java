package com.jasmine.设计模式.代理模式_Proxy.动态代理.例子二;


public class Test {
	public static void main(String[] args) throws Exception {
		// 创建一个原始的GunDog对象，作为target
		Dog target = new GunDog();
		DogBean dogBean = new DogBean();
		dogBean.setName("小白");
		dogBean.setBreed("哈士奇");
		// 以指定的target来创建动态代理
		Dog dog = (Dog)MyProxyFactory.getProxy(target);
		dog.info();
		dog.run();
		System.out.println(dog.getName(dogBean));
	}
}

