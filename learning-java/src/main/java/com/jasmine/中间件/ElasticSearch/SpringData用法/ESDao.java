package com.jasmine.中间件.ElasticSearch.SpringData用法;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

/**
 * @author : jasmineXz
 */
@Component
public interface ESDao extends ElasticsearchRepository<Book, String> {
    Book queryEmployeeById(String id);
}
