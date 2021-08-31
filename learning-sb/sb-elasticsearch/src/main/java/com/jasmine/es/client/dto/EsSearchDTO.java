package com.jasmine.es.client.dto;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jasmine.es.client.config.QueryCondEnum;
import com.jasmine.es.client.config.QueryBoolEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.elasticsearch.search.sort.SortOrder;

import java.util.List;

/**
 * 搜索结果和参数类
 *
 * @author wangyf
 * @since 1.2.2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EsSearchDTO<T extends EsSearchItemDTO> extends EsBaseDTO {

    /** 命中总数 */
    private Long totalHit;

    /** 最大分数 */
    private Float maxScore;

    /** 查询开始位置 */
    private Integer from;

    /** 查询条数 */
    private Integer size;

    /** 超时时间 */
    private Long duration;

    /** 排序字段 */
    private String sortField;

    /** 排序方式, 使用ES提供的枚举类型 {@link SortOrder} */
    private SortOrder sortOrder;

    /** 查询条件 */
    private List<Querys> querys;

    /** 是否高亮 */
    private boolean highLight;

    /** 用高亮结果替换结果字段 */
    private boolean hlReplaceBody;

    /** 命中结果 */
    private List<T> hits;


    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Querys {
        /** 查询字段 */
        private String field;
        /** 查询值 */
        private Object value;
        /** 逻辑条件 */
        private String bool = QueryBoolEnum.must.name();
        /** 查询条件 */
        private String cond = QueryCondEnum.match.name();
        /** range条件的子条件, 当 {@link Querys#bool} 为 {@link QueryCondEnum#range} 时必填 */
        private String typeRange;

        public void setBool(String bool) {
            if (StrUtil.isBlank(bool)) {
                this.bool = QueryBoolEnum.must.name();
            } else {
                this.bool = bool;
            }
        }

        public void setCond(String cond) {
            if (StrUtil.isBlank(cond)) {
                this.cond = QueryCondEnum.match.name();
            } else {
                this.cond = cond;
            }
        }
    }

}
