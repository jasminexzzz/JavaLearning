package com.jasmine.es.client;

import com.jasmine.es.common.EsInfo;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MainResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author wangyf
 * @since 0.0.1
 */
@Slf4j
@RestController
@RequestMapping("/es/client")
public class InfoClientController {

    @Autowired
    private RestHighLevelClient client;


    @GetMapping("/info")
    public EsInfo info () {
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

}
