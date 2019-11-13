package com.jasmine.中间件.elasticsearch;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.jasmine.中间件.ElasticSearch.SpringData用法.Book;
import com.jasmine.中间件.ElasticSearch.SpringData用法.ESDao;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * @author : jasmineXz
 */
@RunWith(SpringRunner.class)
@EnableAutoConfiguration(exclude={DruidDataSourceAutoConfigure.class})
@SpringBootTest
public class EsBySpringDataTest implements Serializable {

    List<Book> documentList = new ArrayList<>();

    // 初始化数据
    {
        Book book1 = new Book();
        book1.setId("1");
        book1.setBookName("Java基础");
        book1.setAuthor("作者A");
        documentList.add(book1);

        Book book2 = new Book();
        book2.setId("2");
        book2.setBookName("Java高级");
        book2.setAuthor("作者A");
        documentList.add(book2);

        Book book3 = new Book();
        book3.setId("3");
        book3.setBookName("Java分布式");
        book3.setAuthor("作者B");
        documentList.add(book3);

        Book book4 = new Book();
        book4.setId("4");
        book4.setBookName("Java高并发");
        book4.setAuthor("作者B");
        documentList.add(book4);


        Book book5 = new Book();
        book5.setId("5");
        book5.setBookName("算法");
        book5.setAuthor("作者B");
        documentList.add(book5);
    }



    @Autowired
    ESDao esDao;

    // ===================================================== 查询 ===============================================================
    /**
     * 根据ID查询
     */
    @Test
    public void findOne(){
        Book book = esDao.findById("1").get();
//        System.err.println(new Gson().toJson(book));
    }

    /**
     * 根据ID查询
     */
    @Test
    public void queryEmployeeById(){
        Book book = esDao.queryEmployeeById("1");
//        System.err.println(new Gson().toJson(book));
    }


    /**
     * 查询全部
     */
    @Test
    public void findAll(){
        Iterable<Book> searchResult = esDao.findAll();
        Iterator<Book> iterator = searchResult.iterator();
        while (iterator.hasNext()) {
            Book book = iterator.next();
            System.out.println(book.toString());
        }
    }

    /**
     * 条件查询
     */
    @Test
    public void queryAll(){
        String str = "吴承";
        QueryStringQueryBuilder builder = new QueryStringQueryBuilder(str);
        Iterable<Book> searchResult = esDao.search(builder);
        Iterator<Book> iterator = searchResult.iterator();
        List<Book> list = new ArrayList<>();
        while (iterator.hasNext()) {
            Book book = iterator.next();
            System.out.println(book.toString());
        }
    }

    @Test
    public void test11() throws UnknownHostException {
        Settings setting = Settings.builder().put("cluster.name",
                "my-application").build();

        //创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(
                        new TransportAddress(
                                InetAddress.getByName("localhost"),32780));

        //get方式数据查询 ,参数为Index,type和id
        GetResponse response = client.prepareGet("book","itbook","5").get();

        System.out.println(response.getSourceAsString());
    }

    // ===================================================== 新增 ===============================================================


    /**
     * 新增
     */
    @Test
    public void save(){
        Book book = new Book();
        book.setId("1");
        book.setBookName("Java");
        book.setAuthor("Java");
        esDao.save(book);
    }

    /**
     * 新增
     */
    @Test
    public void saveAll(){
        this.deleteAll();
        if(!documentList.isEmpty()){
            System.out.println("=====> 准备插入数据");
            esDao.saveAll(documentList);
            System.out.println("=====> 插入数据完成");
        }
    }


    // ===================================================== 修改 ===============================================================


    // ===================================================== 删除 ===============================================================

    /**
     * 删除一个
     */
    @Test
    public void deleteById(){
        esDao.deleteById("1");
    }

    /**
     * 删除全部
     */
    @Test
    public void  deleteAll(){
        esDao.deleteAll();
    }
}