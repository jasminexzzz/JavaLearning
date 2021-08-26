package com.jasmine.es.client;

import cn.hutool.core.util.StrUtil;
import com.jasmine.common.core.util.json.JsonUtil;
import com.jasmine.es.client.dto.EsSearchItemDTO;
import com.jasmine.es.client.dto.ItemDTO;
import com.jasmine.es.client.dto.EsSearchDTO;
import com.jasmine.es.client.manager.EsHighLevelClientManager;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author wangyf
 * @since 0.0.1
 */
@Slf4j
@RestController
@RequestMapping("/es/client/search")
public class SearchClientController {

    @Autowired
    private EsHighLevelClientManager manager;


    @GetMapping("/test")
    public EsSearchDTO<ItemDTO> search (@ModelAttribute EsSearchDTO<ItemDTO> search) {
        return search(search,ItemDTO.class);
    }

    /**
     * 搜索
     * @param searchDTO 基本条件
     * @param clazz 转换对象
     * @param <T> 结果泛型
     * @return 搜索结果
     */
    private <T extends EsSearchItemDTO> EsSearchDTO<T> search (EsSearchDTO<T> searchDTO,Class<T> clazz) {

        // 请求查询
        SearchRequest request = new SearchRequest(searchDTO.getIndex());
        SearchSourceBuilder searchSource = new SearchSourceBuilder();
        request.source(searchSource);
        searchSource
                .from(searchDTO.getFrom())                                               // 开始位置
                .size(searchDTO.getSize())                                               // 查询条件
                .explain(true)                                                           // 显示执行计划
                .sort(searchDTO.getSortField() + "keyword", SortOrder.ASC)
                .timeout(new TimeValue(searchDTO.getDuration() == null ? 5000 : searchDTO.getDuration(), TimeUnit.MILLISECONDS)); // 设置一个可选的超时，控制允许搜索的时间。


        // ---- 查询方法
        if (searchDTO.getNames().length == 0 || StrUtil.isBlank(searchDTO.getValue())) {
            searchSource.query(QueryBuilders.matchAllQuery());
        } else {
            // 是否完整匹配
            if (searchDTO.isTerm()) {
                searchDTO.setNames(new String[]{searchDTO.getNames()[0]});
                TermQueryBuilder condition = QueryBuilders.termQuery(searchDTO.getNames()[0] + ".keyword",searchDTO.getValue());
                searchSource.query(condition);
            } else {
                MultiMatchQueryBuilder condition = QueryBuilders.multiMatchQuery(searchDTO.getValue(),searchDTO.getNames());
                searchSource.query(condition);
            }
        }


//        searchSource.query(logic);

        try {
            SearchResponse response = manager.getHighClient().search(request, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();

            searchDTO.setTotalHit(hits.getTotalHits().value); // 命中总数
            searchDTO.setMaxScore(hits.getMaxScore());        // 最高分数

            for (SearchHit hit : hits.getHits()) {
                // 没有内容则返回下一个
                if (!hit.hasSource()) {
                    continue;
                }
                Map<String,Object> hitMap = hit.getSourceAsMap();
                T obj = JsonUtil.map2Bean(hitMap, clazz);
                if (obj != null) {
                    obj.setEsScore(hit.getScore());
                }
                searchDTO.getHits().add(obj);
            }
            return searchDTO;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("搜索结果异常:" + e.getMessage());
        }
    }


    /**
     * 普通搜索,搜索某个字段
     */
    @GetMapping("/normal")
    public List<ItemDTO> search (String index, String field, String value) {
        return manager.search(index, ItemDTO.class,value,field);
    }


}
