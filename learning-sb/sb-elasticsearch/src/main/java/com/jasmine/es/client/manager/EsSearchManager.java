package com.jasmine.es.client.manager;

import cn.hutool.core.util.StrUtil;
import com.jasmine.common.core.util.json.JsonUtil;
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
     * 简单搜索
     * @param searcher 查询条件封装
     * @param clazz 查询结果类, 必须继承 {@link EsSearchItemDTO}
     * @param <T> 查询结果类泛型
     * @return 查询结果封装
     */
    public <T extends EsSearchItemDTO> EsSearchDTO<T> simpleSearch(EsSearchDTO<T> searcher, Class<T> clazz) {
        // 请求查询
        SearchSourceBuilder searchSource = searchBeforeHandler(searcher);

        // 如果没有查询字段或查询值, 默认该index下的全部
        if (searcher.getFields().length == 0 || StrUtil.isBlank(searcher.getValue())) {
            searchSource.query(QueryBuilders.matchAllQuery());
        } else {
            QueryBuilder finalQuery;
            // 是否完整匹配
            if (searcher.isTerm()) {
                searcher.setFields(new String[]{searcher.getFields()[0]});
                finalQuery = QueryBuilders.termQuery(searcher.getFields()[0] + KEYWORD,searcher.getValue());
            } else {
                finalQuery = QueryBuilders.multiMatchQuery(searcher.getValue(),searcher.getFields());
            }

            searchSource.query(finalQuery);
        }

        // 获取查询结果
        return searchAfterHandler(searcher, search(searcher.getIndex(), searchSource).getHits(), clazz);
    }

    /**
     * 基础搜索功能
     *
     * @param index        index
     * @param searchSource 搜索条件
     * @return 搜索结果
     */
    public SearchResponse search(String index, SearchSourceBuilder searchSource) {
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
            searchSource.timeout(new TimeValue(searcher.getDuration() == null ? 5000 : searcher.getDuration(), TimeUnit.MILLISECONDS)); // 设置一个可选的超时，控制允许搜索的时间。
        }
        // 高亮
        if (searcher.isHighLight()) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            for (String field : searcher.getFields()) {
                highlightBuilder.field(field);
            }

            highlightBuilder.requireFieldMatch(false).preTags("<span style=\"color:#9c27b0;font-weight: bold;\">").postTags("</span>");
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
            T obj = JsonUtil.map2Obj(hit.getSourceAsMap(), clazz);

            // 高亮结果处理
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            ArrayList<EsSearchItemDTO.highLight> highLights = new ArrayList<>();
            for (String field : searcher.getFields()) {
                HighlightField highlight = highlightFields.get(field);
                if (highlight != null) {
                    Text[] fragments = highlight.fragments();
                    if (fragments != null) {
                        EsSearchItemDTO.highLight highLight = new EsSearchItemDTO.highLight();
                        highLight.setField(field);
                        highLight.setValue(fragments[0].string());
                        highLights.add(highLight);
//                        String fragmentString = fragments[0].string();
//                        log.debug("高亮结果 => {}:  {}<br/>", highlight.getName(), fragmentString);
                    }
                }
            }
            if (obj != null) {
                obj.setEsScore(hit.getScore());
                searcher.getHits().add(obj);
                obj.setEsHighLights(highLights);
            }
        }

        return searcher;
    }

}
