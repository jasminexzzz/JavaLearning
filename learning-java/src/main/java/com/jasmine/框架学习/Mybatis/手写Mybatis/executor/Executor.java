package com.jasmine.框架学习.Mybatis.手写Mybatis.executor;

import com.jasmine.框架学习.Mybatis.手写Mybatis.config.MapperStatement;

import java.util.List;

public interface Executor {


    <E> List<E> query(MapperStatement ms , Object paramter);
}
