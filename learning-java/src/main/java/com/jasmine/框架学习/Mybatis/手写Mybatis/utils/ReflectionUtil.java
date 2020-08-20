package com.jasmine.框架学习.Mybatis.手写Mybatis.utils;

import com.jasmine.框架学习.Mybatis.手写Mybatis.model.User;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReflectionUtil {


    public static void setPropToBean(Object bean,String propName,Object value){
        Field f;
        try {
            f = bean.getClass().getDeclaredField(propName);
            f.setAccessible(true);
            f.set(bean,value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void setPropToBeanFromResultSet(Object entity, ResultSet resultSet) throws SQLException {
        Field[] declaredFields = entity.getClass().getDeclaredFields();
        for(int i = 0 ; i < declaredFields.length; i++){
            if(declaredFields[i].getType().getSimpleName().equals("String")){
                setPropToBean(entity,declaredFields[i].getName(),resultSet.getString(declaredFields[i].getName()));
            }else if(declaredFields[i].getType().getSimpleName().equals("Integer")){
                setPropToBean(entity,declaredFields[i].getName(),resultSet.getInt(declaredFields[i].getName()));
            }else if(declaredFields[i].getType().getSimpleName().equals("Long")){
                setPropToBean(entity,declaredFields[i].getName(),resultSet.getLong(declaredFields[i].getName()));
            }
        }

    }

    public static void main(String[] args) {

        User user = new User();
        ReflectionUtil.setPropToBean(user,"userName", "WYF");
        System.out.println(user);
    }
}
