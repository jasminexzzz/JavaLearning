package com.jasmine.中间件.elasticsearch;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.google.gson.Gson;
import com.jasmine.中间件.ElasticSearch.BookTest;
import com.jasmine.中间件.ElasticSearch.ESDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.Serializable;

/**
 * @author : jasmineXz
 */
@RunWith(SpringRunner.class)
@EnableAutoConfiguration(exclude={DruidDataSourceAutoConfigure.class})
@SpringBootTest
public class ElasticSearchTest implements Serializable {

    @Autowired
    ESDao esDao;

    @Test
    public void add(){
        BookTest employee = new BookTest();
        employee.setId("1");
        employee.setBookName("西游记");
        employee.setAuthor("吴承恩");
        esDao.save(employee);
        System.err.println("add a obj");
    }

    //查询
    @Test
    public void query(){
        BookTest accountInfo = esDao.queryEmployeeById("1");
        System.err.println(new Gson().toJson(accountInfo));
    }
}