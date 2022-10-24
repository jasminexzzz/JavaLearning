package com.xzzz.es.client.config;

import lombok.Getter;

/**
 * @author wangyf
 * @since 1.2.2
 */
public enum  DiscoverEnum {
    SINGLE("single"),
    CLUSTER("cluster")
    ;

    @Getter
    private String type;

    DiscoverEnum(String type) {
        this.type = type;
    }
}
