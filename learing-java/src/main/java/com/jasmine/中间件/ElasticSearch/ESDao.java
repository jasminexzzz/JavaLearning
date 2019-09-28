package com.jasmine.中间件.ElasticSearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

/**
 * @author : jasmineXz
 */
@Component
public interface ESDao extends ElasticsearchRepository<BookTest, String> {
    BookTest queryEmployeeById(String id);
}
