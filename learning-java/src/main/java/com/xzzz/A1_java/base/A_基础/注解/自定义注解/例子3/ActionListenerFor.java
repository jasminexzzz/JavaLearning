package com.xzzz.A1_java.base.A_基础.注解.自定义注解.例子3;

import java.lang.annotation.*;
import java.awt.event.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionListenerFor
{
	// 定义一个成员变量，用于设置元数据
	// 该listener成员变量用于保存监听器实现类
	Class<? extends ActionListener> listener();
}
