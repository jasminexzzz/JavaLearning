package com.jasmine.es.client.anotations;


import java.lang.annotation.*;

/**
 * 标识文档信息
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Document {

	String indexName();

}
