package com.xzzz.C1_framework.Mybatis.手写Mybatis.session;

import java.util.List;


/**
 * 1.对外提供数据访问
 * 2.
 */
public interface SqlSession {

    /**
     *
     * @param <T>
     * @param statement
     * @param paramenter
     * @return
     */
    <T> Object selectOne(String statement, Object paramenter);



    <E> List<E> selectList(String statement,Object paramenter);

    <T> T getMapper(Class<T> Type);
}
