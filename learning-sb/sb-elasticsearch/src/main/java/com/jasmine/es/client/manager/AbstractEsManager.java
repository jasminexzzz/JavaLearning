package com.jasmine.es.client.manager;

import cn.hutool.core.util.StrUtil;
import com.jasmine.common.core.util.json.JsonUtil;
import com.jasmine.es.client.dto.EsBaseDTO;
import com.jasmine.es.client.dto.EsInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MainResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.elasticsearch.cluster.metadata.MappingMetadata;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author wangyf
 * @since 0.0.1
 */
@Slf4j
public class AbstractEsManager {

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
    public AbstractEsManager(RestHighLevelClient restHighLevelClient) {
        this.client = restHighLevelClient;
    }

    // ------------------------------< 客户端连接 >------------------------------


    /**
     * 获取 high client
     * @return RestHighLevelClient
     */
    public final RestHighLevelClient getHighClient () {
        return client;
    }

    /**
     * 获取低版本客户端连接, 不推荐使用
     * @return LowLevelClient
     */
    @Deprecated
    public final RestClient getLowLevelClient () {
        return client.getLowLevelClient();
    }

    // ------------------------------< 基本信息 >------------------------------

    /**
     * 获取ES信息
     *
     * <code>GET host:port</code>
     *
     * @return ES信息
     */
    public final EsInfoDTO getInfo() {
        EsInfoDTO esInfoDTO = new EsInfoDTO();
        try {
            MainResponse response = client.info(RequestOptions.DEFAULT); // 返回集群的各种信息
            esInfoDTO.setClusterName(response.getClusterName());            // 集群名称
            esInfoDTO.setClusterUuid(response.getClusterUuid());            // 群集的唯一标识符
            esInfoDTO.setNodeName(response.getNodeName());                  // 已执行请求的节点的名称
            esInfoDTO.setVersion(response.getVersion());                    // 已执行请求的节点的版本
            return esInfoDTO;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return esInfoDTO;
    }

    /**
     * 获取全部 index 集合
     *
     * <code>GET _alias</code>
     *
     * @return index 集合
     */
    public final Set<String> getAliases () {
        try {
            GetAliasesRequest request = new GetAliasesRequest();
            GetAliasesResponse getAliasesResponse = client.indices().getAlias(request, RequestOptions.DEFAULT);
            Map<String, Set<AliasMetadata>> map = getAliasesResponse.getAliases();
            return map.keySet();
        } catch (IOException e) {
            throw new RuntimeException("查询错误");
        }
    }

    /**
     * 获取 Index 的字段映射
     *
     * <code>GET xxx_index/_mapping</code>
     *
     * @param index Index
     * @return 字段映射
     */
    public final Map<String, Object> getMappings (String index) {
        try {
            GetMappingsRequest request = new GetMappingsRequest().indices(index);
            GetMappingsResponse res = client.indices().getMapping(request, RequestOptions.DEFAULT);
            Map<String, MappingMetadata> mapping = res.mappings();
            return mapping.get(index).getSourceAsMap();
        } catch (IOException e) {
            throw new RuntimeException("查询错误");
        }
    }

    // ------------------------------< 索引管理 >------------------------------

    public final boolean createIndex (String index) {
        CreateIndexRequest request = new CreateIndexRequest(index);
        return true;
    }

    // ------------------------------< 工具方法 >------------------------------

    /**
     * 创建一个GetRequest
     * @param index index
     * @param id id
     * @return GetRequest
     */
    protected GetRequest buildGetRequest(String index, String id) {
        checkIndexAndId(index, id);
        return new GetRequest(index, id);
    }

    /**
     * 创建一个 deleteRequest
     * @param index index
     * @param id id
     * @return deleteRequest
     */
    protected DeleteRequest buildDeleteRequest(String index, String id) {
        checkIndexAndId(index, id);
        return new DeleteRequest(index, id);
    }

    /**
     * 对象转source
     * @param obj 对象
     * @return 对象Json
     */
    protected String objToSource (Object obj) {
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
     * 输出响应结果为JSON
     * @param map 响应内容
     */
    protected void debugResponseResourceJson(Map<String,Object> map) {
        log.debug("响应结果RESOURCE => {}", JsonUtil.obj2Json(map));
    }

    /**
     * 检查对象中的Index和Id是否合法
     */
    protected void checkIndexAndId (EsBaseDTO base) {
        checkIndexAndId(base.getEsIndex(),base.getEsId());
    }

    /**
     * 检查参数Index和Id是否合法
     */
    protected void checkIndexAndId (String index,String id) {
        checkIndex(index);
        checkId(id);
    }

    /**
     * 检查Index是否合法
     * @param index index
     */
    protected void checkIndex (String index) {
        if (StrUtil.isBlank(index)) {
            throw new IllegalArgumentException("index 不得为空");
        }
    }

    /**
     * 检查Id是否合法
     * @param id id
     */
    protected void checkId (String id) {
        if (StrUtil.isBlank(id)) {
            throw new IllegalArgumentException("id 不得为空");
        }
    }

    protected void esStatusExceptionHandler(ElasticsearchStatusException e) {
        e.printStackTrace();
        if (e.getMessage().contains("type=index_not_found_exception")) {
            throw new RuntimeException("没有找到对应的索引:" + e.getMessage());
        }
    }
}
