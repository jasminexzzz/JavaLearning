package com.jasmine.es.client.dto;

import lombok.Data;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyf
 * @since 1.2.2
 */
@Data
public class EsSearchDTO<T extends EsSearchItemDTO> {

    private String index;

    private Long totalHit;
    private Float maxScore;
    private Integer from;
    private Integer size;
    private Long duration;
    private String sortField;
    private SortOrder sortOrder;

    private String[] names;
    private String value;
    private boolean term;

    private List<T> hits;


    public EsSearchDTO() {
        this.hits = new ArrayList<>();
    }

    public EsSearchDTO(SearchSourceBuilder searchSourceBuilder) {
        this.hits = new ArrayList<>();
        this.from = searchSourceBuilder.from();
        this.size = searchSourceBuilder.size();
    }
}
