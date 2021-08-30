package com.jasmine.es.client.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.jasmine.common.core.util.json.JsonUtil;
import com.jasmine.es.client.config.EsConstants;
import com.jasmine.es.client.config.QueryConditionEnum;
import com.jasmine.es.client.config.QueryLogicEnum;
import com.jasmine.es.client.dto.EsSearchDTO;
import com.jasmine.es.client.dto.EsSearchItemDTO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 搜索处理类, 专用于搜索方法
 *
 * @author wangyf
 * @since 0.0.1
 */
@Slf4j
public class EsSearchManager extends AbstractEsManager {

    public EsSearchManager(RestHighLevelClient restHighLevelClient) {
        super(restHighLevelClient);
    }


    /**
     * 最简单的通过字段搜索
     *
     * @param clazz 结果类
     * @param index index
     * @param value 查询参数
     * @param fields 查询字段
     * @param <T> 结果泛型
     * @return EsSearchDTO
     */
    public <T extends EsSearchItemDTO> EsSearchDTO<T> search(Class<T> clazz, String index, String value, String... fields) {
        EsSearchDTO<T> searcher = new EsSearchDTO<>();
        List<EsSearchDTO.QueryField> querys = new ArrayList<>();
        for (String field : fields) {
            EsSearchDTO.QueryField query = new EsSearchDTO.QueryField();
            query.setField(field);
            query.setValue(value);
            querys.add(query);
        }
        searcher.setEsIndex(index);
        searcher.setQueryFields(querys);
        return search(searcher, clazz);
    }

    /**
     * 通过封装类搜索
     * {@link EsSearchDTO.QueryField} 中的 login 会将相同的作为同组条件
     *
     * @param searcher 查询条件封装
     * @param clazz 查询结果类, 必须继承 {@link EsSearchItemDTO}
     * @param <T> 查询结果类泛型
     * @return 查询结果封装
     */
    public <T extends EsSearchItemDTO> EsSearchDTO<T> search(EsSearchDTO<T> searcher, Class<T> clazz) {
        // 请求查询
        SearchSourceBuilder searchSource = searchBeforeHandler(searcher);

        // 如果没有查询字段或查询值, 默认该index下的全部
        if (CollUtil.isEmpty(searcher.getQueryFields())) {
            searchSource.query(QueryBuilders.matchAllQuery());
        } else {
            BoolQueryBuilder finalQuery = QueryBuilders.boolQuery();
            searcher.getQueryFields().forEach(query -> logic(finalQuery, query));
            searchSource.query(finalQuery);
            log.warn("搜索条件: \n{}", finalQuery.toString());
        }

        // 获取查询结果
        return searchAfterHandler(searcher, originalSearch(searcher.getEsIndex(), searchSource).getHits(), clazz);
    }

    /**
     * 原生搜索功能, 只基于match搜索多个字段的相同值
     *
     * @param clazz 结果类
     * @param index index
     * @param value 查询参数
     * @param fields 查询字段
     * @param <T> 结果泛型
     * @return 结果集合
     */
    public <T> List<T> originalSearch(Class<T> clazz, String index, String value, String... fields) {
        SearchSourceBuilder searchSource = SearchSourceBuilder.searchSource();
        SearchRequest request = new SearchRequest(index).source(searchSource);
        BoolQueryBuilder logic = QueryBuilders.boolQuery();
        for (String field : fields) {
            logic.must(QueryBuilders.matchQuery(field, value));
        }

        SearchResponse response = originalSearch(index, searchSource);
        List<T> result = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            // 没有内容则返回下一个
            if (!hit.hasSource()) {
                continue;
            }
            T obj = JsonUtil.map2Obj(hit.getSourceAsMap(), clazz);
            if (obj != null) {
                result.add(obj);
            }
        }
        return result;
    }

    /**
     * 原生搜索功能
     *
     * @param index        index
     * @param searchSource 搜索条件
     * @return 搜索结果
     */
    public SearchResponse originalSearch(String index, SearchSourceBuilder searchSource) {
        SearchRequest request = new SearchRequest(index).source(searchSource);
        try {
            return client.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("查询失败:" + e.getMessage());
        }
    }




    /**
     * 创建 SearchSourceBuilder
     * @param searcher 搜索条件
     * @return the SearchSourceBuilder
     */
    private SearchSourceBuilder searchBeforeHandler(EsSearchDTO<?> searcher) {
        SearchSourceBuilder searchSource = new SearchSourceBuilder();
        checkIndex(searcher.getEsIndex());

        // TODO 不能有重复的条件

        // 开始位置
        if (searcher.getFrom() != null) {
            searchSource.from(searcher.getFrom());
        }
        // 查询长度
        if (searcher.getSize() != null) {
            searchSource.size(searcher.getSize());
        }
        // 排序字段
        if (StrUtil.isNotBlank(searcher.getSortField())) {
            searchSource.sort(searcher.getSortField()+ KEYWORD, searcher.getSortOrder());
        }
        // 超时时间
        if (searcher.getDuration() != 0) {
            searchSource.timeout(new TimeValue(searcher.getDuration() == null ? EsConstants.DURATION : searcher.getDuration(), TimeUnit.MILLISECONDS)); // 设置一个可选的超时，控制允许搜索的时间。
        }
        // 高亮
        if (searcher.isHighLight()) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            searcher.getQueryFields().forEach(query -> highlightBuilder.field(query.getField()));
            highlightBuilder.requireFieldMatch(false).preTags("<span style='color:#9c27b0;font-weight: bold;'>").postTags("</span>");
            searchSource.highlighter(highlightBuilder);
        }

        return searchSource;
    }


    /**
     *
     * @param searcher 查询条件封装
     * @param hits 命中结果
     * @param clazz 查询结果类, 必须继承 {@link EsSearchItemDTO}
     * @param <T> 查询结果类泛型
     * @return 查询结果封装
     */
    private <T extends EsSearchItemDTO> EsSearchDTO<T> searchAfterHandler(EsSearchDTO<T> searcher, SearchHits hits, Class<T> clazz) {
        // 命中总数
        searcher.setTotalHit(hits.getTotalHits().value);
        // 最高分数
        searcher.setMaxScore(hits.getMaxScore());
        // 创建结果集
        searcher.setHits(new ArrayList<>(hits.getHits().length));
        // 遍历命中结果
        for (SearchHit hit : hits.getHits()) {
            // 没有内容则返回下一个
            if (!hit.hasSource()) {
                continue;
            }
            T obj = JsonUtil.map2Obj(hit.getSourceAsMap(),  clazz);

            // 高亮结果处理
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            ArrayList<EsSearchItemDTO.highLight> highLights = new ArrayList<>();
            searcher.getQueryFields().forEach(query -> {
                HighlightField highlight = highlightFields.get(query.getField());
                if (highlight != null) {
                    Text[] fragments = highlight.fragments();
                    if (fragments != null) {
                        EsSearchItemDTO.highLight highLight = new EsSearchItemDTO.highLight();
                        highLight.setField(query.getField());
                        highLight.setValue(fragments[0].string());
                        highLights.add(highLight);
                    }
                }
            });
            if (obj != null) {
                obj.setEsScore(hit.getScore());
                searcher.getHits().add(obj);
                obj.setEsHighLights(highLights);
            }
        }
        return searcher;
    }

    /**
     * 构造逻辑
     * @param queryBuilder 逻辑对象
     * @param query 查询字段
     */
    private void logic(BoolQueryBuilder queryBuilder, EsSearchDTO.QueryField query) {
        // 条件必须为真
        if (QueryLogicEnum.must.name().equals(query.getLogic())) {
            queryBuilder.must(condition(query));
        }
        // 条件不需不为真
        else if (QueryLogicEnum.mustNot.name().equals(query.getLogic())){
            queryBuilder.mustNot(condition(query));
        }
        // 或者
        else if (QueryLogicEnum.should.name().equals(query.getLogic())) {
            queryBuilder.should(condition(query));
        }
        else {
            throw new IllegalArgumentException("错误的条件类型: " + query.toString());
        }
    }

    /**
     * 构造条件
     * @param query 查询字段
     * @return 条件对象
     */
    private QueryBuilder condition (EsSearchDTO.QueryField query) {
        QueryBuilder condition;
        if (QueryConditionEnum.term.name().equals(query.getType())) {
            condition = QueryBuilders.termQuery(query.getField(), query.getValue());
        }
        else if (QueryConditionEnum.match.name().equals(query.getType())) {
            condition = QueryBuilders.matchQuery(query.getField(), query.getValue());
        }
        // 范围查询
        else if (QueryConditionEnum.range.name().equals(query.getType())) {
            if (QueryConditionEnum.gt.name().equals(query.getTypeRange())) {
                condition = QueryBuilders.rangeQuery(query.getField()).gt(query.getValue());
            } else if (QueryConditionEnum.gte.name().equals(query.getTypeRange())) {
                condition = QueryBuilders.rangeQuery(query.getField()).gte(query.getValue());
            } else if (QueryConditionEnum.lt.name().equals(query.getTypeRange())) {
                condition = QueryBuilders.rangeQuery(query.getField()).lt(query.getValue());
            } else if (QueryConditionEnum.lte.name().equals(query.getTypeRange())) {
                condition = QueryBuilders.rangeQuery(query.getField()).lte(query.getValue());
            } else {
                throw new IllegalArgumentException("错误的条件类型: " + query.toString());
            }
        }
        else {
            throw new IllegalArgumentException("错误的条件类型: " + query.toString());
        }
        return condition;
    }


}
