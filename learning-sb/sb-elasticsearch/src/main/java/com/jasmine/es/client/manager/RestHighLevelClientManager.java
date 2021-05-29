package com.jasmine.es.client.manager;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.List;
import java.util.Map;

/**
 * @author wangyf
 * @since 0.0.1
 */
public interface RestHighLevelClientManager {

    /**
     * 字段后缀
     */
    String KEYWORD = ".keyword";

    /**
     * 获取单个
     * @param index index
     * @param id id
     * @return 结果
     */
    Map<String,Object> get(String index, String id);

    /**
     * 查询条数
     * @param index index
     * @param queryBuilder 查询条件
     * @return 条数
     */
    long count(String index, QueryBuilder queryBuilder);

    /**
     * 查询数据
     * @param index index
     * @param searchSourceBuilder 查询条件
     * @return 返回结果
     */
    List<String> search(String index, SearchSourceBuilder searchSourceBuilder);
}
