package com.jasmine.es.client.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.jasmine.common.core.util.json.JsonUtil;
import com.jasmine.es.client.dto.EsBaseDTO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author wangyf
 * @since 0.0.1
 */
@Slf4j
@Component
public class EsHighLevelClientManager extends AbstractRestHighLevelClientManager {

    public EsHighLevelClientManager(RestHighLevelClient restHighLevelClient) {
        super(restHighLevelClient);
    }



    // region 查询数据


    /**
     * 根据 index 和 id 查询
     * @param index index
     * @param id id
     * @param clazz 指定结果对象
     * @return 对象数据
     */
    public <T> T get(String index, String id, Class<T> clazz) {
        GetRequest request = getRequest(index, id);
        GetResponse response;
        try {
            response = client.get(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("查询数据失败:" + e.getMessage());
        }

        if (!response.isSourceEmpty() && response.isExists()) {
            printResponseResourceJson(response.getSource());
            return sourceToClazz(response.getSource(), clazz, response);
        }

        return null;
    }


    /**
     * 查询index的总条数
     * @param index index
     * @return 查询条数
     */
    public long count (String index) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        return count(index, boolQueryBuilder);
    }


    /**
     * 查询符合指定字段的条数
     * @param index  index
     * @param term   是否全字段匹配
     * @param value  字段值
     * @param fields 字段名
     * @return       条数
     */
    public long count (String index,boolean term,String value,String... fields) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 如果字段有一个,不使用多字段查询
        if (fields.length == 1) {
            if (term) {
                boolQueryBuilder.must(QueryBuilders.termQuery(fields[0] + KEYWORD,value));// 精确匹配
            } else {
                boolQueryBuilder.must(QueryBuilders.matchQuery(fields[0],value));// 模糊匹配
            }
        }

        if (fields.length > 1) {
            if (term) {
                boolQueryBuilder.must(QueryBuilders.multiMatchQuery(value,fields));// 精确匹配
            } else {
                boolQueryBuilder.must(QueryBuilders.multiMatchQuery(value,fields));// 模糊匹配
            }
        }

        return count(index, boolQueryBuilder);
    }


    /**
     * 查询条数
     * @param index index
     * @param queryBuilder 查询条件
     * @return 查询条数
     */
    @Override
    public long count (String index, QueryBuilder queryBuilder) {
        CountRequest countRequest = new CountRequest(index);
        countRequest.query(queryBuilder);
        try {
            CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);
            return countResponse.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }


    // endregion 查询数据






    // region 搜索


    /**
     * 搜索
     *
     * @param index index
     * @param clazz 结果对象
     * @param value 查询内容
     * @param field 查询字段,一个或多个
     *
     * @return 结果对象集合
     */
    public <T> List<T> search (String index, Class<T> clazz, String value, String... field) {

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder(); // 查询基类
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();      // 真假匹配

        if (field.length > 1) {
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(value,field);
            boolBuilder.must(multiMatchQueryBuilder);
        } else {
            MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(field[0], value); // 匹配查询到的
            boolBuilder.must(matchQueryBuilder);
        }

        /*------------------------------------------------
         * 2. 构造
         ------------------------------------------------*/
        /*
         * 2.1 添加查询条件
         * must   : 满足该条件
         * filter : 顾虑条件中的
         * should :
         */

        /*
         * 2.2 添加查询属性
         */
        sourceBuilder.query(boolBuilder)
            .from(0) // 从0开始
            .size(2) // 查询条数
            .explain(true)
            .timeout(new TimeValue(5, TimeUnit.SECONDS)); // 设置一个可选的超时，控制允许搜索的时间。

        return search(index, clazz, sourceBuilder);
    }


    /**
     * 搜索
     * @param index index
     * @param clazz 结果对象
     * @param searchSourceBuilder 查询条件
     * @param <T> 结果对象泛型
     * @return 结果集合
     */
    public <T> List<T> search (String index, Class<T> clazz, SearchSourceBuilder searchSourceBuilder) {
        SearchRequest request = new SearchRequest(index);
        request.source(searchSourceBuilder);

        // 3. 查询
        SearchResponse response;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();  //SearchHits提供有关所有匹配的全局信息，例如总命中数或最高分数：
            SearchHit[] searchHits = hits.getHits();
            List<T> result = new ArrayList<>();
            for (SearchHit hit : searchHits) {
                if (!hit.hasSource()) {
                    continue;
                }
                log.debug("查询结果: {}",hit.getSourceAsString());
                result.add(sourceToClazz(hit.getSourceAsString(),clazz));
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    // endregion 搜索






    // region 判断数据

    /**
     * 判断是否存在
     * @param index index
     * @param id id
     * @return 是否存在
     * @throws IOException IOException
     */
    public boolean exists (String index,String id) throws IOException {
        return client.exists(getRequest(index,id), RequestOptions.DEFAULT);
    }

    // endregion 判断数据






    // region 新增数据

    /**
     * 新增数据
     * @param base 对象
     */
    public void add (EsBaseDTO base) {
        checkIndexAndId(base);
        IndexRequest request = new IndexRequest(base.getEsIndex());
        try {
            request.id(base.getEsId());
            request.source(objToSource(base), XContentType.JSON);
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            // 有保存失败的
            if (response.status() != RestStatus.OK) {
                throw new RuntimeException("保存失败");
            }
            log.debug("添加数据成功 索引为: {}, response 状态: {}, id为: {}",base.getEsId(),response.status().getStatus(), response.getId());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("新增对象错误: %s, 对象: %s",e.getMessage(),base.toString()));
        }
    }

    /**
     * 批量新增
     * @param bases 对象
     */
    public void addBatch(List<? extends EsBaseDTO> bases) {
        checkList(bases);
        BulkRequest bulkRequest = new BulkRequest();
        for (EsBaseDTO base : bases) {
            IndexRequest indexRequest = new IndexRequest();
            indexRequest.index(base.getEsIndex());
            indexRequest.id(base.getEsId());
            indexRequest.source(objToSource(base), XContentType.JSON);
            bulkRequest.add(indexRequest);
        }

        try {
            BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (response.status() != RestStatus.OK) {
                throw new RuntimeException(response.buildFailureMessage());
            }
        } catch (IOException e) {
            throw new RuntimeException(String.format("新增对象错误: %s, 对象: %s", e.getMessage(), bases.toString()));
        }
    }

    // endregion 新增数据






    // region 删除数据

    public void deleteById (String index,String id) throws IOException {
        checkIndexAndId(index,id);
        //执行客户端请求
        DeleteResponse delete = client.delete(getDeleteRequest(index,id), RequestOptions.DEFAULT);
        System.out.println(delete);
    }

    // endregion 删除数据






    // region 其他

    private GetRequest getRequest(String index,String id) {
        checkIndexAndId(index, id);
        return new GetRequest(index, id);
    }

    private DeleteRequest getDeleteRequest(String index,String id) {
        checkIndexAndId(index, id);
        return new DeleteRequest(index, id);
    }

    /**
     * source转Class
     * @param source
     * @param clazz
     * @param response
     * @param <T>
     * @return
     */
    private <T> T sourceToClazz (Map<String, Object> source, Class<T> clazz, GetResponse response) {
        try {
            Object obj = JsonUtil.map2Bean(source, clazz);
            setIndexAndId(obj,response);
            return (T) obj;
        } catch (Exception e) {
            throw new RuntimeException("转换 ES SOURCE 失败: " + source);
        }
    }

    /**
     * source转Class
     * @param source
     * @param clazz
     * @param <T>
     * @return
     */
    private <T> T sourceToClazz (String source, Class<T> clazz) {
        try {
            Object obj = JsonUtil.json2Object(source, clazz);
            return (T) obj;
        } catch (Exception e) {
            throw new RuntimeException("转换 ES SOURCE 失败: " + source);
        }
    }

    /**
     * 对象转source
     * @param obj
     * @return
     */
    private String objToSource (Object obj) {
        try {
            String source = JsonUtil.obj2Json(obj);
            log.debug("新增数据: {}",source);
            return source;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 向对象中添加index和id
     * @param obj
     * @param response
     */
    private void setIndexAndId(Object obj, GetResponse response) {
        if (obj instanceof EsBaseDTO) {
            EsBaseDTO base = (EsBaseDTO) obj;
            base.setEsIndex(response.getIndex());
            base.setEsId(response.getId());
        }
    }

    /**
     * 检查集合中的Index和Id是否合法
     * @param bases 请求内容
     */
    private void checkList (List<? extends EsBaseDTO> bases) {
        if (CollUtil.isEmpty(bases)) {
            throw new IllegalArgumentException("数据不得为空");
        }
        bases.forEach(this::checkIndexAndId);
    }

    /**
     * 检查对象中的Index和Id是否合法
     */
    private void checkIndexAndId (EsBaseDTO base) {
        checkIndexAndId(base.getEsIndex(),base.getEsId());
    }

    /**
     * 检查参数Index和Id是否合法
     */
    private void checkIndexAndId (String index,String id) {
        if (StrUtil.isBlank(index)) {
            throw new IllegalArgumentException("index 不得为空");
        }

        if (StrUtil.isBlank(id)) {
            throw new IllegalArgumentException("id 不得为空");
        }
    }
    // endregion
}
