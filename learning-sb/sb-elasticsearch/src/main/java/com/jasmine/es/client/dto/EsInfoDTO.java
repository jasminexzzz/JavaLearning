package com.jasmine.es.client.dto;

import lombok.Data;
import org.elasticsearch.client.core.MainResponse;

import java.io.Serializable;

/**
 * @author wangyf
 * @since 0.0.1
 */
@Data
public class EsInfoDTO implements Serializable {
    private String clusterName;
    private String clusterUuid;
    private String nodeName;
    private MainResponse.Version version;

    @Override
    public String toString() {
        return "EsInfoDTO{" +
                "集群名称='" + clusterName + '\'' +
                ", 集群标识='" + clusterUuid + '\'' +
                ", 节点名称='" + nodeName + '\'' +
                ", 版本=" + version.toString() +
            '}';
    }
}
