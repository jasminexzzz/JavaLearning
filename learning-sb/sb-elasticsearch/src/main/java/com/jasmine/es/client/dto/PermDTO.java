package com.jasmine.es.client.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wangyf
 * @since 0.0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PermDTO extends EsBaseDTO {
    private String permName;
}
