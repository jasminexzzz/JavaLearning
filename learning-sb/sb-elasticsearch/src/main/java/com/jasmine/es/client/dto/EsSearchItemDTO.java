package com.xzzz.es.client.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author wangyf
 * @since 1.2.2
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EsSearchItemDTO extends EsBaseDTO {

    /**
     * 命中分数
     */
    private float esScore;

    /**
     * 搜索高亮字段
     */
    private List<highLight> esHighLights;

    @Data
    public static class highLight {
        private String field;
        private String value;
    }

}
