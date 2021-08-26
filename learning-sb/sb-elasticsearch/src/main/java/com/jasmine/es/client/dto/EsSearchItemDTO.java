package com.jasmine.es.client.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wangyf
 * @since 1.2.2
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EsSearchItemDTO extends EsBaseDTO {
    private float esScore;
}
