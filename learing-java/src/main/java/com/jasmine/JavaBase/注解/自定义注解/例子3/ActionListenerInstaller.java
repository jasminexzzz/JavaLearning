package com.jasmine.JavaBase.注解.自定义注解.例子3;
import java.lang.reflect.*;
import java.awt.event.*;
import javax.swing.*;

public class ActionListenerInstaller
{
	// 处理Annotation的方法，其中obj是包含Annotation的对象
	public static void processAnnotations(Object obj)
	{
		try
		{
			// 获取obj对象的类
			Class cl = obj.getClass();
			// 获取指定obj对象的所有成员变量，并遍历每个成员变量
			for (Field f : cl.getDeclaredFields())
			{
				System.out.println(f.getName());
				// 将该成员变量设置成可自由访问。
				f.setAccessible(true);
				// 获取该成员变量上ActionListenerFor类型的Annotation
				ActionListenerFor a = f.getAnnotation(ActionListenerFor.class);
				// 获取成员变量f的值
				Object fObj  = f.get(obj);
				// 如果f是AbstractButton的实例，且a不为null
				if (a != null && fObj != null && fObj instanceof AbstractButton)
				{
					// 获取a注解里的listner元数据（它是一个监听器类）
					Class<? extends ActionListener> listenerClazz = a.listener();
					// 使用反射来创建listner类的对象
					ActionListener al = listenerClazz.newInstance();
					// 此处重新赋值也是这个对象，因为对象是同一个
					AbstractButton ab = (AbstractButton)fObj;
					// 为ab按钮添加事件监听器
					ab.addActionListener(al);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
