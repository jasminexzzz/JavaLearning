package com.jasmine.es.client.biz;

import com.jasmine.es.client.biz.dto.ItemDTO;
import com.jasmine.es.client.dto.EsSearchDTO;
import com.jasmine.es.client.manager.EsCurdManager;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    private EsCurdManager manager;


    @PostMapping("/test")
    public List<Map<String,Object>> searchTest (@RequestBody EsSearchDTO<ItemDTO> search) {
        SearchSourceBuilder searchSource = new SearchSourceBuilder().from(0).size(20).explain(true);
        BoolQueryBuilder bool = QueryBuilders.boolQuery();


        /*===================================================================================================
         * 各类条件测试
         *==================================================================================================*/
        // 1. term 查询, 不会对value字段进行分词
        TermQueryBuilder term = QueryBuilders.termQuery("name","麻辣");
        bool.must(term);


        log.warn("\n"+bool.toString());
        searchSource.query(bool);

        SearchResponse response = manager.originalSearch("test_client_index", searchSource);
        List<Map<String, Object>> result = new ArrayList<>();

        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {
            result.add(hit.getSourceAsMap());
        }
        return result;
    }

    @PostMapping
    public EsSearchDTO<ItemDTO> search (@RequestBody EsSearchDTO<ItemDTO> search) {
        return manager.search(search,ItemDTO.class);
    }
}
