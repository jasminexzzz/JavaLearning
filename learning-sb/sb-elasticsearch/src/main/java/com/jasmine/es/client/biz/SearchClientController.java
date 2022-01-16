package com.jasmine.es.client.biz;

import cn.hutool.core.map.MapUtil;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.jasmine.common.core.util.json.JsonUtil;
import com.jasmine.es.client.biz.dto.ItemDTO;
import com.jasmine.es.client.dto.EsSearchDTO;
import com.jasmine.es.client.manager.EsCurdManager;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;
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
import org.elasticsearch.search.fetch.subphase.FieldAndFormat;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
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
@RequestMapping("/es/client/")
public class SearchClientController {

    @Autowired
    private EsCurdManager manager;


    @GetMapping("/search/test")
    public void searchTest () {
        SearchSourceBuilder searchSource = new SearchSourceBuilder()
                // 超时时间
                .timeout(TimeValue.timeValueSeconds(2))
                .from(0)
                .size(5)
                // 设置最大命中数
                .trackTotalHits(true).trackTotalHitsUpTo(1000)
                // 指定filed 字段
                .fetchField("gmtCreated").fetchField("itemName").fetchField("supplierId").fetchField("supplierName")
                // 是否显示source字段
                .fetchSource(false)
                .explain(true);

        searchSource.query(
            QueryBuilders
                .rangeQuery("gmtCreated")
                    .gte("2021-04-30 10:30")
                    .lte("2021-04-30 10:45")
                    .boost(1.5F)
                    .format("yyyy-MM-dd HH:mm")
        );

        // 可以打印本次查询的API
        System.out.println(String.format("本次搜索条件:\n %s", searchSource.query().toString()));
        // 执行搜索
        SearchResponse response = manager.originalSearch("index_item", searchSource);
        // 打印响应结果
        printResponse(response);
    }



    private void printResponse(SearchResponse response) {
        System.out.println("==========================================================================================================");
        System.out.println("《search.query 的公共响应参数说明》\n");
        System.out.println(String.format("搜索用时(毫秒): %s",response.getTook().duration()));
        System.out.println(String.format("命中文档的数量：%d",response.getHits().getTotalHits().value));
        System.out.println(String.format("命中文档的解释方式：%s",response.getHits().getTotalHits().relation));
        System.out.println("\t命中文档的解释方式: EQUAL_TO                 : 命中数是准确的");
        System.out.println("\t命中文档的解释方式: GREATER_THAN_OR_EQUAL_TO : 真实的命中文档数大于该值");
        System.out.println("==========================================================================================================");
        // 获取命中结果
        SearchHits hits = response.getHits();
        System.out.println(String.format("命中结果的最大分数: %s", hits.getMaxScore()));
        System.out.println("命中结果集合如下: ");
        for (SearchHit hit : hits) {
            System.out.println(String.format("\t索引名称: %s", hit.getIndex()));
            System.out.println(String.format("\t文档ID: %s", hit.getId()));
            System.out.println(String.format("\t命中分数: %s", hit.getScore()));
            // 如果查询时的 fetchSource = false, 则没有 source 响应
            if (MapUtil.isNotEmpty(hit.getSourceAsMap())) {
                System.out.println(String.format("\t命中结果转 string: %s", hit.getSourceAsString()));
                System.out.println(String.format("\t命中结果转 map: %s", hit.getSourceAsMap()));
                System.out.println(String.format("\t命中结果格式化: %s", JsonUtil.toPrettyJson(hit.getSourceAsMap())));
            }
            // 如果查询时没有指定 field，则没有 fields 结果响应
            if (MapUtil.isNotEmpty(hit.getFields())) {
                System.out.println("========== 指定的 filed 字段 ==========");
                hit.getFields().forEach((k,v) -> {
                    System.out.println(String.format("\t\t%s : %s", v.getName(), v.getValues().get(0)));
                });
            }
        }
        System.out.println("==========================================================================================================");
    }






















    @GetMapping("/aggs/test")
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
