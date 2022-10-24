package com.xzzz.es.client.dto;

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
    private EsTool esTool;

    @Data
    public static class EsTool {
        private String desc;
        private String version;
    }
}
