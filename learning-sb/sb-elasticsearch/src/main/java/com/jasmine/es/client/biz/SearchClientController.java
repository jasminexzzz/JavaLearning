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
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedHistogram;
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

    @GetMapping("/aggs")
    public void aggs () {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);
        final String field = "retailPrice";
        AggregationBuilder aggs = AggregationBuilders
            .histogram(field)
            .field(field)
            .interval(100000)
            .missing(-1)
            .minDocCount(0)
            .extendedBounds(-1, 3000000)
        ;
        searchSourceBuilder.aggregation(aggs).explain(true);
        // 查询数据
        SearchResponse searchResponse = manager.originalSearch("index_item", searchSourceBuilder);
        // 打印聚合类型
        System.out.println(searchResponse.getAggregations().get(field).getClass().getName());
        // 用来解析 Terms 聚合
        ParsedHistogram histogram = searchResponse.getAggregations().get(field);
        System.out.println("==========================================================================================================");
        System.out.println("《Histogram 聚合的响应参数说明》\n");
        System.out.println(String.format("\t聚合的类型: " +
                        "\n\t\t1: histogram      (普通直方图聚合) : %s" +
                        "\n\t\t2: date_histogram (时间直方图聚合) : %s" +
                        "\n\t\t>  本次响应: %s",
                ParsedHistogram.class.getName(),
                ParsedDateHistogram.class.getName(),
                histogram.getType()));
        System.out.println("==========================================================================================================");

        List<? extends Histogram.Bucket> buckets = histogram.getBuckets();
        for (Histogram.Bucket bucket : buckets) {
            System.out.println(String.format("count: % 5d, key: %s", bucket.getDocCount(), bucket.getKeyAsString()));
        }
    }


    @PostMapping
    public EsSearchDTO<ItemDTO> search (@RequestBody EsSearchDTO<ItemDTO> search) {
        return manager.search(search,ItemDTO.class);
    }
}
