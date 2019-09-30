package com.jasmine.中间件.elasticsearch;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.google.gson.Gson;
import com.jasmine.中间件.ElasticSearch.BookTest;
import com.jasmine.中间件.ElasticSearch.ESDao;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author : jasmineXz
 */
@RunWith(SpringRunner.class)
@EnableAutoConfiguration(exclude={DruidDataSourceAutoConfigure.class})
@SpringBootTest
public class ElasticSearchTest implements Serializable {

    @Autowired
    ESDao esDao;

    /**
     * 新增一个
     */
    @Test
    public void add(){
        BookTest book = new BookTest();
        for(int i = 3 ; i < 20 ; i++){
            book.setId(String.valueOf(i));
            book.setBookName("蓝茉莉"+i);
            book.setAuthor("王云飞");
            System.out.println("插入i :"+i);
            esDao.save(book);
        }
    }


    /**
     * 根据ID查询
     */
    @Test
    public void findOne(){
        BookTest book = esDao.findById("1").get();
        System.err.println(new Gson().toJson(book));
    }

    /**
     * 根据ID查询
     */
    @Test
    public void queryEmployeeById(){
        BookTest book = esDao.queryEmployeeById("1");
        System.err.println(new Gson().toJson(book));
    }

    /**
     * 查询全部
     */
    @Test
    public void queryAll(){
//        String str = "吴承";
        String str = "王云";
        QueryStringQueryBuilder builder = new QueryStringQueryBuilder(str);
        Iterable<BookTest> searchResult = esDao.search(builder);
        Iterator<BookTest> iterator = searchResult.iterator();
        List<BookTest> list = new ArrayList<>();
        while (iterator.hasNext()) {
            BookTest book = iterator.next();
            System.out.println(book.toString());
        }
    }
}