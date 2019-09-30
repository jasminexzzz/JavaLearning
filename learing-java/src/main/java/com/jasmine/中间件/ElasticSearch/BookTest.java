package com.jasmine.中间件.ElasticSearch;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

/**
 * 标注存储地址
 * indexName : 索引名称 : 可以理解为数据库名 必须为小写 不然会报org.elasticsearch.indices.InvalidIndexNameException异常
 * type      : 类型 : 可以理解为表名
 *
 *
 * @author : jasmineXz
 */
@Document(indexName = "mytest",type="book")
public class BookTest {
    @Id
    private String id;
    @Field
    private String bookName;
    @Field
    private String author;

    @Override
    public String toString() {
        return "BookTest{" +
                "id=" + id +
                ", bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
