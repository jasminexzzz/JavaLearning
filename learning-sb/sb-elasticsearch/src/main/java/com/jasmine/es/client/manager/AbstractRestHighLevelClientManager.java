package com.jasmine.es.client.manager;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.jasmine.common.core.util.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MainResponse;
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
public class AbstractRestHighLevelClientManager {

    /**
     * 字段后缀
     */
    protected static final String KEYWORD = ".keyword";

    /**
     * 连接客户端
     */
    protected RestHighLevelClient client;


    /**
     * 构造方法
     */
    public AbstractRestHighLevelClientManager (RestHighLevelClient restHighLevelClient) {
        this.client = restHighLevelClient;
    }


    /**
     * 获取ES信息
     * @return ES信息
     */
    public EsInfo getInfo() {
        EsInfo esInfo = new EsInfo();
        try {
            MainResponse response = client.info(RequestOptions.DEFAULT); // 返回集群的各种信息
            esInfo.setClusterName(response.getClusterName());            // 集群名称
            esInfo.setClusterUuid(response.getClusterUuid());            // 群集的唯一标识符
            esInfo.setNodeName(response.getNodeName());                  // 已执行请求的节点的名称
            esInfo.setVersion(response.getVersion());                    // 已执行请求的节点的版本
            return esInfo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return esInfo;
    }


    /**
     * 获取 high client
     * @return RestHighLevelClient
     */
    public final RestHighLevelClient getHighClient () {
        return client;
    }


    /**
     * 获取低版本客户端连接
     * @return LowLevelClient
     */
    public final RestClient getLowLevelClient () {
        return client.getLowLevelClient();
    }


    /**
     * 根据index 和 id 查询
     * @param index index
     * @param id id
     * @return 返回对象
     */
    public Map<String,Object> get(String index, String id) {
        GetRequest request = buildGetRequest(index, id);
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


    /**
     * 查询条数
     * @param index index
     * @param queryBuilder 查询条件
     * @return 条数
     */
    public long count(String index, QueryBuilder queryBuilder) {
        return 0;
    }


    /**
     * 搜索
     * @param index index
     * @param searchSourceBuilder 查询条件
     * @return 结果source集合
     */
    public List<String> search(String index, SearchSourceBuilder searchSourceBuilder) {
        return null;
    }


    /**
     * 创建一个GetRequest
     * @param index index
     * @param id id
     * @return GetRequest
     */
    private GetRequest buildGetRequest(String index, String id) {
        checkIndexAndId(index, id);
        return new GetRequest(index, id);
    }


    /**
     * 创建一个 deleteRequest
     * @param index index
     * @param id id
     * @return deleteRequest
     */
    private DeleteRequest buildDeleteRequest(String index, String id) {
        checkIndexAndId(index, id);
        return new DeleteRequest(index, id);
    }


    /**
     * 对象转source
     * @param obj 对象
     * @return source
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
     * 输出响应结果为字符串
     * @param map 响应内容
     */
    protected void printResponseResourceString (Map<String,Object> map) {
        log.debug("响应结果RESOURCE => {}",MapUtil.join(map,":"," | ",""));
    }

    /**
     * 输出响应结果为JSON
     * @param map 响应内容
     */
    protected void printResponseResourceJson (Map<String,Object> map) {
        log.debug("响应结果RESOURCE => {}",JsonUtil.obj2Json(map));
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
