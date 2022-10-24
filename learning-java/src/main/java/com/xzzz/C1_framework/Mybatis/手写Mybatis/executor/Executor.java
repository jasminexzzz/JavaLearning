package com.xzzz.C1_framework.Mybatis.手写Mybatis.executor;

import com.xzzz.C1_framework.Mybatis.手写Mybatis.config.MapperStatement;

import java.util.List;

public interface Executor {


    <E> List<E> query(MapperStatement ms , Object paramter);
}
