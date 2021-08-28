package com.jasmine.es.client.manager;

import com.jasmine.common.core.util.json.JsonUtil;
import com.jasmine.es.client.dto.EsBaseDTO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * ElasticSearch CURD 操作
 *
 * @author wangyf
 * @since 1.2.2
 */
@Slf4j
@Component
public class EsCurdManager extends EsSearchManager {

    /**
     * 字段后缀
     */
    public EsCurdManager(RestHighLevelClient restHighLevelClient) {
        super(restHighLevelClient);
    }



    // region -----------------------------------------------< 查询数据 >------------------------------------------------


    /**
     * 根据 index 和 id 查询
     * @param index index
     * @param id id
     * @param clazz 指定结果对象
     * @return 对象数据
     */
    public <T> T get(String index, String id, Class<T> clazz) {
        GetRequest request = buildGetRequest(index, id);
        try {
            GetResponse response = client.get(request, RequestOptions.DEFAULT);
            if (!response.isSourceEmpty() && response.isExists()) {
                debugResponseResourceJson(response.getSource());
                T obj = JsonUtil.map2Obj(response.getSource(), clazz);
                if (obj instanceof EsBaseDTO) {
                    EsBaseDTO base = (EsBaseDTO) obj;
                    base.setEsIndex(response.getIndex());
                    base.setEsId(response.getId());
                }
                return obj;
            }
        } catch (IOException e) {
            throw new RuntimeException("查询数据失败:" + e.getMessage());
        }
        return null;
    }



    // endregion







    // region -----------------------------------------------< 查询条数 >------------------------------------------------

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
     * @param index index
     * @param term 是否全字段匹配
     * @param value 字段值
     * @param fields 字段名
     * @return 条数
     */
    public long count (String index,boolean term,String value,String... fields) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        // 如果字段有一个,不使用多字段查询
        if (fields.length == 1) {
            if (term) {
                boolQuery.must(QueryBuilders.termQuery(fields[0] + KEYWORD,value));// 精确匹配
            } else {
                boolQuery.must(QueryBuilders.matchQuery(fields[0],value));// 模糊匹配
            }
        }

        if (fields.length > 1) {
            // 模糊匹配
            boolQuery.must(QueryBuilders.multiMatchQuery(value,fields));// 精确匹配
        }
        return count(index, boolQuery);
    }


    /**
     * 查询条数
     * @param index index
     * @param queryBuilder 查询条件
     * @return 查询条数
     */
    public long count (String index, QueryBuilder queryBuilder) {
        CountRequest request = new CountRequest(index);
        request.query(queryBuilder);
        try {
            CountResponse countResponse = client.count(request, RequestOptions.DEFAULT);
            return countResponse.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }


    // endregion







    // region -----------------------------------------------< 判断数据 >------------------------------------------------

    /**
     * 判断是否存在
     * @param index index
     * @param id id
     * @return 是否存在
     */
    public boolean exists (String index,String id) {
        try {
            return client.exists(buildGetRequest(index,id), RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("查询错误");
        }
    }

    // endregion







    // region -----------------------------------------------< 新增数据 >------------------------------------------------

    /**
     * 新增数据
     * @param base 对象
     * @param waitFor 刷新策略, 为 true 则等待此变动可查询时才返回
     */
    public <T extends EsBaseDTO> void add (T base, boolean waitFor) {
        checkIndexAndId(base);
        IndexRequest request = new IndexRequest(base.getEsIndex());
        if (waitFor) {
            request.setRefreshPolicy("wait_for");
        }
        try {
            request.id(base.getEsId());
            request.source(objToSource(base), XContentType.JSON);
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            if (response.status() != RestStatus.OK) {
                throw new RuntimeException("保存失败");
            }
            log.debug("添加数据成功 索引为: {}, response 状态: {}, id为: {}", base.getEsId(), response.status().getStatus(), response.getId());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("新增对象错误: %s, 对象: %s", e.getMessage(), base.toString()));
        }
    }

    /**
     * 批量新增
     * @param bases 对象
     * @param waitFor 刷新策略, 为 true 则等待此变动可查询时才返回
     */
    public void addBatch(List<? extends EsBaseDTO> bases, boolean waitFor) {
        BulkRequest request = new BulkRequest();
        for (EsBaseDTO base : bases) {
            checkIndexAndId(base);
            IndexRequest indexRequest = new IndexRequest();
            indexRequest.index(base.getEsIndex());
            indexRequest.id(base.getEsId());
            indexRequest.source(objToSource(base), XContentType.JSON);
            request.add(indexRequest);
        }
        batch(request, waitFor);
    }

    // endregion







    // region -----------------------------------------------< 修改数据 >------------------------------------------------


    /**
     * 修改数据
     *
     * @param base    修改对象
     * @param waitFor 刷新策略, 为 true 则等待此变动可查询时才返回
     */
    public <T extends EsBaseDTO> void update(T base, boolean waitFor) {
        checkIndexAndId(base);
        UpdateRequest request = new UpdateRequest(base.getEsIndex(), base.getEsId());
        if (waitFor) {
            request.setRefreshPolicy("wait_for");
        }
        try {
            request.doc(objToSource(base), XContentType.JSON);
            UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
            if (response.status() != RestStatus.OK) {
                throw new RuntimeException("修改失败");
            }
            log.debug("添加数据成功 索引为: {}, response 状态: {}, id为: {}", base.getEsId(), response.status().getStatus(), response.getId());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("新增对象错误: %s, 对象: %s", e.getMessage(), base.toString()));
        }
    }


    /**
     * 批量修改
     * @param bases 对象
     * @param waitFor 刷新策略, 为 true 则等待此变动可查询时才返回
     */
    public void updBatch (List<? extends EsBaseDTO> bases, boolean waitFor) {
        BulkRequest request = new BulkRequest();
        for (EsBaseDTO base : bases) {
            checkIndexAndId(base);
            UpdateRequest updRequest = new UpdateRequest();
            updRequest.index(base.getEsIndex());
            updRequest.id(base.getEsId());
            updRequest.doc(objToSource(base), XContentType.JSON);
            request.add(updRequest);
        }
        batch(request, waitFor);
    }


    // endregion







    // region -----------------------------------------------< 删除数据 >------------------------------------------------

    /**
     * 删除对象
     * @param index index
     * @param id id
     */
    public void delete (String index,String id) {
        checkIndexAndId(index,id);
        // 执行客户端请求
        try {
            DeleteResponse response = client.delete(buildDeleteRequest(index,id), RequestOptions.DEFAULT);
            if (response.status() != RestStatus.OK) {
                throw new RuntimeException("删除失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("删除对象错误, [INDEX:%s] [ID:%s] %s", index, id, e.getMessage()));
        }
    }


    /**
     * 批量删除
     * @param bases 对象
     * @param waitFor 刷新策略, 为 true 则等待此变动可查询时才返回
     */
    public void delBatch (List<? extends EsBaseDTO> bases, boolean waitFor) {
        BulkRequest request = new BulkRequest();
        for (EsBaseDTO base : bases) {
            request.add(buildDeleteRequest(base.getEsIndex(),base.getEsId()));
        }
        batch(request, waitFor);
    }

    // endregion






    /**
     * 批处理请求
     * @param bulkRequest 批处理请求
     * @param waitFor 刷新策略 {@link WriteRequest.RefreshPolicy}, 此处只使用 waitFor, 若为true会导致请求时间变长, 批量操作
     *                不建议此参数为 true
     *                - false  : 不要在此请求后刷新, 默认值。
     *                - true   : 作为请求的一部分强制刷新。此刷新策略不能用于高索引或搜索吞吐量，但对于具有非常低流量的索引提供一致的视图是有用的。它非常适合测试!
     *                - waitFor: 保持此请求打开，直到刷新使此请求的内容可以搜索。此刷新策略与高索引和搜索吞吐量兼容，但它会导致请求等待应答，直到刷新发生。
     */
    private void batch (BulkRequest bulkRequest, boolean waitFor) {
        if (waitFor) {
            bulkRequest.setRefreshPolicy("wait_for");
        }
        try {
            BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (response.status() != RestStatus.OK || response.hasFailures()) {
                throw new RuntimeException(response.buildFailureMessage());
            }
        } catch (IOException e) {
            throw new RuntimeException(String.format("批处理错误: %s",e.getMessage()));
        }
    }
}
