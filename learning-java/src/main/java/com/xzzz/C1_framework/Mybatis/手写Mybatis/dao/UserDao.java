package com.xzzz.C1_framework.Mybatis.手写Mybatis.dao;

import com.xzzz.C1_framework.Mybatis.手写Mybatis.model.User;

import java.util.List;

public interface UserDao {

    User selectByPrimaryKey(Object id);

    List<User> selectAll();
}
