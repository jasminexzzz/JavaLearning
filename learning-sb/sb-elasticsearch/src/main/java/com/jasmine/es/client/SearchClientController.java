package com.jasmine.es.client;

import com.jasmine.es.client.dto.ItemDTO;
import com.jasmine.es.client.manager.EsHighLevelClientManager;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<Map<String,Object>> search () {

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder logicQuery = QueryBuilders.boolQuery();

        MatchQueryBuilder conditionBuilder = QueryBuilders.matchQuery("name","海");
//        logicQuery.must(conditionBuilder);

        sourceBuilder
            .query(conditionBuilder)
            .explain(true);

        // 请求查询
        SearchRequest request = new SearchRequest("test_client_index");
        request.source(sourceBuilder);
        SearchResponse response;
        List<Map<String,Object>> result = new ArrayList<>();
        try {
            response = manager.getHighClient().search(request, RequestOptions.DEFAULT);

            // SearchHits提供有关所有匹配的全局信息，例如总命中数或最高分数
            SearchHits hits = response.getHits();
            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                // 没有内容则返回下一个
                if (!hit.hasSource()) {
                    continue;
                }
                Map<String,Object> hitMap = hit.getSourceAsMap();

                // 添加一些查询的元信息
                Map<String, Object> hitContext = new HashMap<>();
                hitContext.put("score", hit.getScore());

                hitMap.put("hitContext",hitContext);
                result.add(hitMap);
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 普通搜索,搜索某个字段
     */
    @GetMapping("/normal")
    public List<ItemDTO> search (String index, String field, String value) {
        return manager.search(index, ItemDTO.class,value,field);
    }

}
