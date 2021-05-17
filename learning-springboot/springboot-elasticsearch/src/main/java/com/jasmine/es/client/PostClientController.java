package com.jasmine.es.client;

import cn.hutool.json.JSONUtil;
import common.jasmine.learning.util.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * @author wangyf
 * @since 0.0.1
 */

@Slf4j
@RestController
@RequestMapping("/es/client")
public class PostClientController {

    @Autowired
    private EsManager esManager;

    @Autowired
    private RestHighLevelClient client;

    @PostMapping("/createIndex")
    public void createIndex () {
        esManager.createIndex("test_index");
    }

    @PostMapping("/add/doc")
    public void addDoc (@RequestBody Map<String,Object> map) {

        System.out.println(JsonUtil.obj2Json(map));
        IndexRequest request = new IndexRequest(map.get("index").toString());
        try {
            request.id(map.get("id").toString());
            request.source(JsonUtil.obj2Json(map), XContentType.JSON);
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            log.info("添加数据成功 索引为: {}, response 状态: {}, id为: {}",map.get("index"),response.status().getStatus(), response.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
