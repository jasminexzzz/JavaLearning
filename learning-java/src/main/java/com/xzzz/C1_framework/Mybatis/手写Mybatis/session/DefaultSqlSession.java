package com.xzzz.C1_framework.Mybatis.手写Mybatis.session;

import com.xzzz.C1_framework.Mybatis.手写Mybatis.binding.MappedProxy;
import com.xzzz.C1_framework.Mybatis.手写Mybatis.config.Configuration;
import com.xzzz.C1_framework.Mybatis.手写Mybatis.config.MapperStatement;
import com.xzzz.C1_framework.Mybatis.手写Mybatis.executor.DefaultExecutor;
import com.xzzz.C1_framework.Mybatis.手写Mybatis.executor.Executor;

import java.lang.reflect.Proxy;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration conf;

    private Executor executor;

    public DefaultSqlSession(Configuration conf){
        super();
        this.conf = conf;
        executor = new DefaultExecutor(conf);
    }


    @Override
    public <T> T selectOne(String statement, Object paramenter) {
        List<T> selectList = this.selectList(statement, paramenter);
        if(selectList == null || selectList.size() == 0){
            return null;
        }

        if(selectList.size() == 1){
            return selectList.get(0);
        }

        throw new RuntimeException("too many results");
    }

    @Override
    public <E> List<E> selectList(String statement, Object paramenter) {
        //com.jasmine.框架学习.Mybatis.手写Mybatis.dao.UserDao.selectByPrimaryKey
        /*
        conf.getMapperStatements()是扫描到的mapper.xml中的dao路径+方法名 也就是com.jasmine.dao.UserDaogetUserName
         */
        MapperStatement ms = conf.getMapperStatements().get(statement);
        return executor.query(ms, paramenter);
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        MappedProxy mp = new MappedProxy(this);
        return (T)Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, mp);
    }
}
