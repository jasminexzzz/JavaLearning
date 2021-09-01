package com.jasmine.es.client.biz;

import cn.hutool.core.map.MapUtil;
import com.carrotsearch.hppc.cursors.ObjectObjectCursor;
import com.jasmine.common.core.dto.R;
import com.jasmine.es.client.dto.EsInfoDTO;
import com.jasmine.es.client.biz.dto.ItemDTO;
import com.jasmine.es.client.manager.EsCurdManager;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.GetFieldMappingsRequest;
import org.elasticsearch.client.indices.GetFieldMappingsResponse;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.elasticsearch.cluster.metadata.MappingMetadata;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author wangyf
 * @since 0.0.1
 */
@Slf4j
@RestController
@RequestMapping("/es/client")
public class SelectClientController {

    @Autowired
    private EsCurdManager manager;

    @GetMapping("/info")
    public EsInfoDTO info() {
        return manager.getInfo();
    }

    @GetMapping("/_alias")
    public R getAllIndex () {
        return R.ok(manager.getAliases());
    }

    @GetMapping("/_mapping")
    public R _mapping (String index) {
        return R.ok(manager.getMappings(index));
    }

    /**
     * 根据
     *
     * @param index index
     * @param id id
     * @return 返回数据
     */
    @GetMapping("/get")
    public ItemDTO getDoc(String index, String id) {
        return manager.get(index, id, ItemDTO.class);
    }

    /**
     * 查询条数
     *
     * @param index index
     * @return 条数
     */
    @GetMapping("/count")
    public long getCount(String index) {
        return manager.count(index);
    }

    /**
     * 查询是否存在
     *
     * @param index index
     * @param id    id
     * @return true/false
     */
    @GetMapping("/exists")
    public boolean exists(String index, String id) {
        return manager.exists(index, id);
    }



    @GetMapping("/test")
    public void test() {
    }


}
