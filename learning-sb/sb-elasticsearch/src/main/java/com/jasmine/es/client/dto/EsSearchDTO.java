package com.jasmine.es.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.elasticsearch.search.sort.SortOrder;

import java.util.List;

/**
 * @author wangyf
 * @since 1.2.2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EsSearchDTO<T extends EsSearchItemDTO> extends EsBaseDTO {

    /**
     * 命中总数
     */
    private Long totalHit;

    /**
     * 最大分数
     */
    private Float maxScore;

    /**
     * 查询开始位置
     */
    private Integer from;

    /**
     * 查询条数
     */
    private Integer size;

    /**
     * 超时时间
     */
    private Long duration;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方式 {@link SortOrder}
     */
    private SortOrder sortOrder;


    private List<QueryField> queryFields;

    /**
     * 不分词查询
     */
    private boolean term;

    /**
     * 是否高亮
     */
    private boolean highLight;

    /**
     * 命中结果
     */
    private List<T> hits;


    @Data
    public static class QueryField {
        private String field;
        private String value;
        private String logic = "must";
        private String type = "match";
        private String typeRange;
    }

}
