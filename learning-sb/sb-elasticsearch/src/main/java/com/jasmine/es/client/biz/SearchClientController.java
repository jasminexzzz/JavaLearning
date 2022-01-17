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
import org.elasticsearch.search.aggregations.metrics.ParsedAvg;
import org.elasticsearch.search.aggregations.metrics.ParsedMax;
import org.elasticsearch.search.aggregations.metrics.ParsedMin;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
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

        searchSource.aggregation(
                AggregationBuilders.sum("")
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
        final String field = "retailPrice";

        // 总和
        AggregationBuilder sumAggs = AggregationBuilders.sum(field).field(field);
        SearchResponse sumResp = manager.originalSearch("index_item", new SearchSourceBuilder().size(0).aggregation(sumAggs));
        ParsedSum sum = sumResp.getAggregations().get(field);
        System.out.println("==========================================================================================================");
        System.out.println("《Sum 聚合的响应参数说明》");
        System.out.println(String.format("响应类: %s, 响应类型: %s", ParsedSum.class.getName(), sum.getType()));
        Double sumValue = sum.getValue();
        System.out.println("总和: " + sumValue);
        System.out.println("==========================================================================================================");

        // 最大值
        AggregationBuilder maxAggs = AggregationBuilders.max(field).field(field);
        SearchResponse maxResp = manager.originalSearch("index_item", new SearchSourceBuilder().size(0).aggregation(maxAggs));
        ParsedMax max = maxResp.getAggregations().get(field);
        System.out.println("==========================================================================================================");
        System.out.println("《Max 聚合的响应参数说明》");
        System.out.println(String.format("响应类: %s, 响应类型: %s", ParsedMax.class.getName(), max.getType()));
        Double maxValue = max.getValue();
        System.out.println("最大值: " + maxValue);
        System.out.println("==========================================================================================================");

        // 最小值
        AggregationBuilder minAggs = AggregationBuilders.min(field).field(field);
        SearchResponse minResp = manager.originalSearch("index_item", new SearchSourceBuilder().size(0).aggregation(minAggs));
        ParsedMin min = minResp.getAggregations().get(field);
        System.out.println("==========================================================================================================");
        System.out.println("《Min 聚合的响应参数说明》");
        System.out.println(String.format("响应类: %s, 响应类型: %s", ParsedMin.class.getName(), min.getType()));
        Double minValue = min.getValue();
        System.out.println("最小值: " + minValue);
        System.out.println("==========================================================================================================");

        // 平均值
        AggregationBuilder avgAggs = AggregationBuilders.avg(field).field(field);
        SearchResponse avgResp = manager.originalSearch("index_item", new SearchSourceBuilder().size(0).aggregation(avgAggs));
        ParsedAvg avg = avgResp.getAggregations().get(field);
        System.out.println("==========================================================================================================");
        System.out.println("《Avg 聚合的响应参数说明》");
        System.out.println(String.format("响应类: %s, 响应类型: %s", ParsedAvg.class.getName(), avg.getType()));
        Double avgValue = avg.getValue();
        System.out.println("平均值: " + avgValue);
        System.out.println("==========================================================================================================");
    }


    @PostMapping
    public EsSearchDTO<ItemDTO> search (@RequestBody EsSearchDTO<ItemDTO> search) {
        return manager.search(search,ItemDTO.class);
    }
}
