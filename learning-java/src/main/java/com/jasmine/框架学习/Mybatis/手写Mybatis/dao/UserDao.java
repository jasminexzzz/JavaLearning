package com.jasmine.框架学习.Mybatis.手写Mybatis.dao;

import com.jasmine.框架学习.Mybatis.手写Mybatis.model.User;

import java.util.List;

public interface UserDao {

    User selectByPrimaryKey(Object id);

    List<User> selectAll();
}
