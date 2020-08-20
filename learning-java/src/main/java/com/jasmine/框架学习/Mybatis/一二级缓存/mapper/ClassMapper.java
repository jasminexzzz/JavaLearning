package com.jasmine.框架学习.Mybatis.一二级缓存.mapper;

import org.apache.ibatis.annotations.Param;

public interface ClassMapper {

    public int updateClassName(@Param("name") String className, @Param("id") int id);
}
