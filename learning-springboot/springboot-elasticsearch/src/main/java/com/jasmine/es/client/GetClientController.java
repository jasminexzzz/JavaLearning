package com.jasmine.es.client;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
public class GetClientController {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private EsManager esManager;

    @GetMapping("/indexByName")
    public Object getIndexByName (String index, String id) {
        GetRequest request = new GetRequest(index, id);
        GetResponse response;
        try {
            response = client.get(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("查询数据失败:" + e.getMessage());
        }

        if (!response.isSourceEmpty() && response.isExists()) {
            // 获取数据,转换为map
            Map<String,Object> map = response.getSourceAsMap();
            map.put("index",response.getIndex());
            map.put("id", response.getId());

            System.out.println(map.toString());
            return map;
        }

        return "未查询到数据";

    }


    @GetMapping("/isExist")
    public boolean isExist (String index) {
        return esManager.isIndexExist(index);
    }

}
