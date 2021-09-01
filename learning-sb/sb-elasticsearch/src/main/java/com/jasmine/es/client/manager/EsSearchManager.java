package com.jasmine.es.client.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.jasmine.common.core.util.json.JsonUtil;
import com.jasmine.es.client.config.EsConstants;
import com.jasmine.es.client.config.QueryCondEnum;
import com.jasmine.es.client.config.QueryBoolEnum;
import com.jasmine.es.client.dto.EsSearchDTO;
import com.jasmine.es.client.dto.EsSearchItemDTO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchStatusException;
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
     * 最简单的通过字段搜索, 只基于match搜索多个字段的相同值
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
        List<EsSearchDTO.Querys> querys = new ArrayList<>();
        for (String field : fields) {
            EsSearchDTO.Querys query = new EsSearchDTO.Querys();
            query.setField(field);
            query.setValue(value);
            querys.add(query);
        }
        searcher.setEsIndex(index);
        searcher.setQuerys(querys);
        return search(searcher, clazz);
    }

    /**
     * 通过封装类搜索
     * PS:
     *  - {@link EsSearchDTO.Querys} 中的 logic 会将相同的作为同组条件, 例如两个querys都为should或must, 则查询会构造成如下
     *  <code>
     *  {
     *    "bool" : {
     *      "should" : [
     *        {
     *          "match" : {
     *            "name" : {
     *              "query" : "麻辣"
     *            }
     *          }
     *        },
     *        {
     *          "match" : {
     *            "desc" : {
     *              "query" : "在家"
     *            }
     *          }
     *        }
     *      ]
     *    }
     *  }
     *  </code>
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
        if (CollUtil.isEmpty(searcher.getQuerys())) {
            searchSource.query(QueryBuilders.matchAllQuery());
        } else {
            BoolQueryBuilder finalQuery = QueryBuilders.boolQuery();
            searcher.getQuerys().forEach(query -> logic(finalQuery, query));
            searchSource.query(finalQuery);
            log.warn("搜索条件: \n{}", finalQuery.toString());
        }

        // 获取查询结果
        return searchAfterHandler(searcher, originalSearch(searcher.getEsIndex(), searchSource).getHits(), clazz);
    }

    /**
     * 原生搜索功能, 只基于match搜索多个字段的相同值, 即最基础的搜索功能
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
     * 原生搜索功能, 查询条件由调用方构造, 只做最基础的ES查询调用
     *
     * @param index index
     * @param searchSource 搜索条件
     * @return 搜索结果
     */
    public SearchResponse originalSearch(String index, SearchSourceBuilder searchSource) {
        SearchRequest request = new SearchRequest(index).source(searchSource);
        try {
            return client.search(request, RequestOptions.DEFAULT);
        } catch (ElasticsearchStatusException ex) {
            esStatusExceptionHandler(ex);
            throw new RuntimeException("ES处理错误:" + ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("查询失败:" + e.getMessage());
        }
    }


    // ------------------------------< 内部方法封装 >------------------------------


    /**
     * 封装的搜索方法 {@link EsSearchManager#search(EsSearchDTO, Class)} 的前置处理器
     * 会判断各项参数, 并返回一个 {@link SearchSourceBuilder}
     *
     * @param searcher 搜索条件
     * @return the SearchSourceBuilder
     */
    private SearchSourceBuilder searchBeforeHandler(EsSearchDTO<?> searcher) {
        SearchSourceBuilder searchSource = new SearchSourceBuilder();
        checkIndex(searcher.getEsIndex());

        // 查询条件中不能有重复的字段, 如查了两个 name 字段
        Map<Object, Long> fields = searcher.getQuerys().stream().collect(Collectors.groupingBy(EsSearchDTO.Querys::getField,Collectors.counting()));
        fields.forEach((k, v) -> {
            if (v > 1) {
                throw new IllegalArgumentException(String.format("每个查询字段只能出现一次, 字段[%s]", k));
            }
        });


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
            searcher.getQuerys().forEach(query -> highlightBuilder.field(query.getField()));
            highlightBuilder.requireFieldMatch(false).preTags("<span style='color:#9c27b0;font-weight: bold;'>").postTags("</span>");
            searchSource.highlighter(highlightBuilder);
        }

        return searchSource;
    }

    /**
     * 封装的搜索方法 {@link EsSearchManager#search(EsSearchDTO, Class)} 的后置处理器
     * 将ES的 {@link SearchResponse} 中的 {@link SearchHits} 封装成一个 {@link EsSearchDTO} 来返回,
     * 提供更直观的字段返回
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
            if (obj == null) {
                continue;
            }

            ArrayList<EsSearchItemDTO.highLight> highLights = new ArrayList<>();

            // 是否处理高亮结果处理
            if (searcher.isHighLight()) {
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                searcher.getQuerys().forEach(query -> {
                    HighlightField highlight = highlightFields.get(query.getField());
                    if (highlight != null) {
                        Text[] fragments = highlight.fragments();
                        if (fragments != null) {
                            // 用高亮结果替换原字段
                            if (searcher.isHlReplaceBody()) {
                                invokeSetSmthing(obj, query.getField(), fragments[0].string());
                            } else {
                                EsSearchItemDTO.highLight highLight = new EsSearchItemDTO.highLight();
                                highLight.setField(query.getField());
                                highLight.setValue(fragments[0].string());
                                highLights.add(highLight);
                            }
                        }
                    }
                });
            }
            obj.setEsScore(hit.getScore());
            searcher.getHits().add(obj);
            if (CollUtil.isNotEmpty(highLights)) {
                obj.setEsHighLights(highLights);
            }
        }
        return searcher;
    }

    /**
     * 构造逻辑条件 {@link BoolQueryBuilder}
     *
     * @param queryBuilder 逻辑对象
     * @param query 查询字段
     */
    private void logic(BoolQueryBuilder queryBuilder, EsSearchDTO.Querys query) {
        // 条件必须为真
        if (QueryBoolEnum.must.name().equals(query.getBool())) {
            queryBuilder.must(condition(query));
        }
        // 条件不需不为真
        else if (QueryBoolEnum.mustNot.name().equals(query.getBool())){
            queryBuilder.mustNot(condition(query));
        }
        // 或者
        else if (QueryBoolEnum.should.name().equals(query.getBool())) {
            queryBuilder.should(condition(query));
        }
        else {
            throw new IllegalArgumentException("错误的条件(bool)类型: " + query.getBool());
        }
    }

    /**
     * 构造查询条件 {@link QueryBuilder} 的各种条件子类等等
     *
     * @param query 查询字段
     * @return 条件对象
     */
    private QueryBuilder condition (EsSearchDTO.Querys query) {
        QueryBuilder condition;
        if (QueryCondEnum.term.name().equals(query.getCond())) {
            condition = QueryBuilders.termQuery(query.getField() + KEYWORD, query.getValue());
        }
        else if (QueryCondEnum.match.name().equals(query.getCond())) {
            condition = QueryBuilders.matchQuery(query.getField(), query.getValue());
        }
        // 范围查询
        else if (QueryCondEnum.range.name().equals(query.getCond())) {
            if (QueryCondEnum.gt.name().equals(query.getTypeRange())) {
                condition = QueryBuilders.rangeQuery(query.getField()).gt(query.getValue());
            } else if (QueryCondEnum.gte.name().equals(query.getTypeRange())) {
                condition = QueryBuilders.rangeQuery(query.getField()).gte(query.getValue());
            } else if (QueryCondEnum.lt.name().equals(query.getTypeRange())) {
                condition = QueryBuilders.rangeQuery(query.getField()).lt(query.getValue());
            } else if (QueryCondEnum.lte.name().equals(query.getTypeRange())) {
                condition = QueryBuilders.rangeQuery(query.getField()).lte(query.getValue());
            } else {
                throw new IllegalArgumentException("错误的条件(cond)类型: " + query.getCond());
            }
        }
        else {
            throw new IllegalArgumentException("错误的条件(cond)类型: " + query.getCond());
        }
        return condition;
    }

    /**
     * 调用某个字段的set方法来为该字段赋值, 反射获取set方法, 要求该字段必须只有一个set方法, 多个set方法无效
     *
     * @param obj 对象
     * @param field set的字段
     * @param value set的值
     */
    private void invokeSetSmthing (Object obj,String field, String value) {
        Method method = ReflectUtil.getMethodByName(obj.getClass(), "set" + StrUtil.upperFirst(field));
        if (method == null) {
            return;
        }
        try {
            method.invoke(obj, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
