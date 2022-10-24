package com.jasmine.C1_framework.Mybatis.手写Mybatis.executor;

import com.jasmine.C1_framework.Mybatis.手写Mybatis.config.Configuration;
import com.jasmine.C1_framework.Mybatis.手写Mybatis.config.MapperStatement;
import com.jasmine.C1_framework.Mybatis.手写Mybatis.utils.ReflectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DefaultExecutor implements Executor {

    private Configuration conf;

    public DefaultExecutor(Configuration conf){
        super();
        this.conf = conf;
    }

    @Override
    public <E> List<E> query(MapperStatement ms, Object parameter) {


        List<E> ret = new ArrayList<E>();

        try {
            Class.forName(conf.getJdbcDriver());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(conf.getJdbcUrl(),conf.getJdbcUsername(),conf.getJdbcPassword());
            System.out.println(ms.getSql());
            preparedStatement = connection.prepareStatement(ms.getSql());
//            preparedStatement = connection.prepareStatement("prepareStatementVGSELECT * FROM base_User WHERE userid = ?");
            parameterize(preparedStatement,parameter);
            resultSet = preparedStatement.executeQuery();

            handlerResultSet(resultSet,ret,ms.getResultType());
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                resultSet.close();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }


    private void parameterize(PreparedStatement preparedStatement,Object parameter) throws SQLException {
        if(parameter instanceof Integer){
            preparedStatement.setInt(1, (int)parameter);
        }else if(parameter instanceof Long){
            preparedStatement.setLong(1, (long)parameter);
        }else if(parameter instanceof String){
            preparedStatement.setString(1, (String)parameter);
        }
    }

    private <E> void handlerResultSet(ResultSet resultSet,List<E> ret, String className){
        Class<E> clazz = null;
        try {
            clazz = (Class<E>)Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            while(resultSet.next()){
                Object entity = clazz.newInstance();
                ReflectionUtil.setPropToBeanFromResultSet(entity,resultSet);
                ret.add((E) entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }
}
