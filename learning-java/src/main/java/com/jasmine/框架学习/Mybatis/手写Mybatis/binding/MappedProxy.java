package com.jasmine.框架学习.Mybatis.手写Mybatis.binding;

import com.jasmine.框架学习.Mybatis.手写Mybatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;

public class MappedProxy implements InvocationHandler {

    private SqlSession session;


    public MappedProxy(SqlSession session){
        super();
        this.session = session;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class returnType = method.getReturnType();

        //方法返回为list
        if(Collection.class.isAssignableFrom(returnType)){
            return session.selectList(
                    method.getDeclaringClass().getName()+"."+method.getName(),
                    args == null?null:args[0]);
        }
        return session.selectOne(
                method.getDeclaringClass().getName()+"."+method.getName(),
                args == null?null:args[0]);
    }
}
