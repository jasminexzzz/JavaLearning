package com.jasmine.es.client.dto;

import lombok.Data;
import org.elasticsearch.index.query.QueryBuilder;

/**
 * @author wangyf
 * @since 1.2.2
 */
@Data
public class EsParamDTO {
    private String field;
    private String value;

    public EsParamDTO(String field, String value) {
        this.field = field;
        this.value = value;
    }
}
