package com.xzzz.B3_design_mode.代理模式_Proxy.动态代理.例子二;

import java.lang.reflect.*;

public class MyInvokationHandler implements InvocationHandler {

	// 需要被代理的对象
	private Object target;
	public void setTarget(Object target) {
		this.target = target;
	}

	// 执行动态代理对象的所有方法时，都会被替换成执行如下的invoke方法
	public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
		DogUtil du = new DogUtil();
		// 执行DogUtil对象中的method1。
		du.method1();
		// 以target作为主调来执行method方法
		Object result = method.invoke(target , args);
		if(args != null){
			Class<?> jframeClazz = Class.forName(args[0].getClass().getName());
			Field[] f = jframeClazz.getDeclaredFields();
			for(int i = 0 ; i < f.length ; i ++){
				System.out.println();
			}
		}
		// 执行DogUtil对象中的method2。
		du.method2();
		return result;
	}
}


