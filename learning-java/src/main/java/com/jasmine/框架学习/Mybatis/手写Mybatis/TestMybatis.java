package com.jasmine.框架学习.Mybatis.手写Mybatis;

import com.jasmine.框架学习.Mybatis.手写Mybatis.dao.UserDao;
import com.jasmine.框架学习.Mybatis.手写Mybatis.model.User;
import com.jasmine.框架学习.Mybatis.手写Mybatis.session.SqlSession;
import com.jasmine.框架学习.Mybatis.手写Mybatis.session.SqlSessionFactory;

public class TestMybatis {

    public static void main(String[] args) {

        //1.实例化SqlSessionFactory,加载数据库配置文件以及mapper.xml文件到configuration对象
        SqlSessionFactory factory = new SqlSessionFactory();
        //2.获取SqlSession对象
        SqlSession session = factory.OpenSession();
        //3.通过动态代理跨越面向接口编程和iBatis编程模型的鸿沟
        UserDao userMapper = session.getMapper(UserDao.class);
        //4.
        User user = userMapper.selectByPrimaryKey("00102");
        System.out.println(user.getUserName());
    }
}
