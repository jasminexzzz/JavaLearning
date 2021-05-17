package com.jasmine.es.client.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wangyf
 * @since 0.0.1
 */
@Data
public class EsBaseDTO implements Serializable {
    private String esIndex;
    private String esId;
}
