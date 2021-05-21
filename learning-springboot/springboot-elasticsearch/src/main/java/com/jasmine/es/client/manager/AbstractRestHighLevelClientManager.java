package com.jasmine.es.client.manager;

import cn.hutool.core.util.StrUtil;
import common.jasmine.learning.util.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author wangyf
 * @since 0.0.1
 */
@Slf4j
public class AbstractRestHighLevelClientManager implements RestHighLevelClientManager {

    RestHighLevelClient client;


    public AbstractRestHighLevelClientManager (RestHighLevelClient restHighLevelClient) {
        this.client = restHighLevelClient;
    }


    @Override
    public Map<String,Object> get(String index, String id) {
        GetRequest request = getRequest(index, id);
        GetResponse response;
        try {
            response = client.get(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("查询数据失败:" + e.getMessage());
        }

        if (!response.isSourceEmpty() && response.isExists()) {
            return response.getSource();
        }

        return null;
    }

    @Override
    public long count(String index, QueryBuilder queryBuilder) {
        return 0;
    }

    @Override
    public List<String> search(String index, SearchSourceBuilder searchSourceBuilder) {
        return null;
    }


    private GetRequest getRequest(String index, String id) {
        checkIndexAndId(index, id);
        return new GetRequest(index, id);
    }

    private DeleteRequest getDeleteRequest(String index, String id) {
        checkIndexAndId(index, id);
        return new DeleteRequest(index, id);
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
     * 检查Index和Id
     */
    private void checkIndexAndId (String index,String id) {
        if (StrUtil.isBlank(index)) {
            throw new IllegalArgumentException("index 不得为空");
        }

        if (StrUtil.isBlank(id)) {
            throw new IllegalArgumentException("id 不得为空");
        }
    }
}
