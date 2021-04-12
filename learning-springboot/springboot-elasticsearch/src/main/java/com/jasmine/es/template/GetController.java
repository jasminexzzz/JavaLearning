package com.jasmine.es.template;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.main.MainResponse;
import org.elasticsearch.client.RequestOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangyf
 * @since 0.0.1
 */
@Slf4j
@RestController
@RequestMapping("/es")
public class GetController {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @GetMapping("/template/info")
    public Map<String,String> info () {
        Map<String,String> map = new HashMap<>(5);
        try {
            MainResponse response = elasticsearchRestTemplate.getClient().info(RequestOptions.DEFAULT);
            log.warn(response.toString());
            map.put("版本", response.getVersion().toString());
            map.put("集群名称", response.getClusterName().toString());
            map.put("集群ID", response.getClusterUuid());
            map.put("节点名称", response.getNodeName());
            map.put("build", response.getBuild().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }


}
