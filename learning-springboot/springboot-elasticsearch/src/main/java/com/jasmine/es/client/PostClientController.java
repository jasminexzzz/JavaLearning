package com.jasmine.es.client;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
    public void addDoc (Map<String,String> map) {
        IndexRequest request = new IndexRequest("test_index");
        try {
            request.id(map.get("id"));
            request.source(map, XContentType.JSON);
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            log.info("添加数据成功 索引为: {}, response 状态: {}, id为: {}","test_index",response.status().getStatus(), response.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
