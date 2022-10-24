package com.xzzz.es.client.dto;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xzzz.es.client.config.FieldType;
import com.xzzz.es.client.config.QueryBool;
import com.xzzz.es.client.config.QueryCond;
import lombok.Data;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.util.List;

/**
 * 搜索结果和参数类
 *
 * @author wangyf
 * @since 1.2.2
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EsSearchDTO<H extends EsSearchItemDTO> {

    // ----------------------------------------< 请求参数 >----------------------------------------
    private String esIndex;

    /**
     * 是否在日志中显示查询语句
     */
    private boolean showQueryToLog;

    /**
     * 查询开始位置
     *
     * {@link SearchSourceBuilder#from(int)}
     */
    private Integer from;

    /**
     * 查询条数
     *
     * {@link SearchSourceBuilder#size(int)}
     */
    private Integer size;

    /**
     * 搜索匹配的最大条数, 搜索会在匹配到该条数后终止
     * 注意, 此字段会影响 {@link EsSearchDTO#totalHit} 的准确性, 即使实际存在的文档数大于该字段, 则 totalHit 也会显示该字段
     *
     * 与 size 搭配可实现快速检索是否存在检索条件的文档, 如下
     *  size = 0
     *  terminateAfter = 1
     *
     * {@link SearchSourceBuilder#terminateAfter()}
     */
    private Integer terminateAfter;

    /**
     * 准确返回命中总数
     *
     * 例如有10W个文档, 根据某条件会查询到15000条, 则查询结果返回的 {@link SearchHits#getTotalHits().value}, 并不是实际真正的总数,
     * 而是该字段配置的阈值, 因为要获取匹配总数总会访问所有命中的文档, 这在大量文档下会影响性能, 该字段可优化效率
     *  - {@link SearchHits#getTotalHits().relation } 为 EQUAL_TO                 : 命中数是准确的,
     *  - {@link SearchHits#getTotalHits().relation } 为 GREATER_THAN_OR_EQUAL_TO : 则表明实际可命中的文档数大于当前字段
     *
     * ES默认值 : 10000, 如果命中
     *
     * {@link SearchSourceBuilder#trackTotalHits(boolean)}  : 是否准确返回命中数, 如果未false, 则不返回命中字段
     * {@link SearchSourceBuilder#trackTotalHitsUpTo(int)}} : 准确的最大命中数
     */
    private Integer traceTotalHits;

    /**
     * 超时时间
     *
     * {@link SearchSourceBuilder#timeout(TimeValue)}
     */
    private Long duration;

    /**
     * 排序字段
     *
     * {@link SearchSourceBuilder#sort(String, SortOrder)}
     */
    private String sortField;

    /**
     * 排序方式, 使用ES提供的枚举类型 {@link SortOrder}
     *
     * {@link SearchSourceBuilder#sort(String, SortOrder)}
     */
    private SortOrder sortOrder;

    /**
     * 查询条件
     *
     * {@link EsSearchDTO.Querys}
     */
    private List<Querys> querys;

    /**
     * 是否高亮
     */
    private boolean highLight;

    /**
     * 用高亮结果替换结果字段
     */
    private boolean hlReplaceBody;

    // ----------------------------------------< 响应内容 >----------------------------------------

    /**
     * 命中总数
     * {@link SearchResponse#getHits()#getTotalHit().value}
     */
    private Long totalHit;

    /**
     * 最大分数
     * {@link SearchResponse#getHits()#getMaxScore()}
     */
    private Float maxScore;

    /**
     * 命中结果
     * {@link SearchResponse#getHits()}
     */
    private List<H> hits;


    /**
     * 查询条件类
     */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Querys {

        /**
         * 查询字段
         */
        private String field;

        /**
         * 查询值
         */
        private Object value;

        /**
         * 逻辑条件
         */
        private String bool = QueryBool.must.name();

        /**
         * 查询条件
         *
         * 如果 cond 等于 {@link QueryCond#term}, 且 {@link Querys#field} 字段在ES的类型为 {@link FieldType#Text}
         * 则 {@link Querys#field} 字段需要拼接 ".keyword", 来标识查询 text 的子字段 text.keyword
         */
        private String cond = QueryCond.match.name();

        /**
         * range条件的子条件
         *
         * 当 {@link Querys#bool} 为 {@link QueryCond#range} 时必填
         */
        private String rangeCond;

        /**
         * 设置逻辑条件
         * @param bool must/mustNot/should
         */
        public void setBool(String bool) {
            if (StrUtil.isBlank(bool)) {
                this.bool = QueryBool.must.name();
            } else {
                this.bool = bool;
            }
        }

        /**
         * 设置条件类型
         * @param cond term/match/range/gt/gte/lt/lte
         */
        public void setCond(String cond) {
            if (StrUtil.isBlank(cond)) {
                this.cond = QueryCond.match.name();
            } else {
                this.cond = cond;
            }
        }
    }

}
