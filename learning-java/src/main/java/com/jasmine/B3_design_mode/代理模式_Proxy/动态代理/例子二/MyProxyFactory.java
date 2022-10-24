package com.jasmine.B3_design_mode.代理模式_Proxy.动态代理.例子二;

import java.lang.reflect.*;

public class MyProxyFactory {

	// 为指定target生成动态代理对象
	public static Object getProxy(Object target) throws Exception {
		// 创建一个MyInvokationHandler对象
		MyInvokationHandler handler = new MyInvokationHandler();
		// 为MyInvokationHandler设置target对象
		handler.setTarget(target);
		// 创建、并返回一个动态代理
		return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces() , handler);
	}
}

