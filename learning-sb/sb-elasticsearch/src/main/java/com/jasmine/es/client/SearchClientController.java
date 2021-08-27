package com.jasmine.es.client;

import cn.hutool.core.util.StrUtil;
import com.jasmine.common.core.util.json.JsonUtil;
import com.jasmine.es.client.dto.EsSearchItemDTO;
import com.jasmine.es.client.dto.ItemDTO;
import com.jasmine.es.client.dto.EsSearchDTO;
import com.jasmine.es.client.manager.EsCurdManager;
import com.jasmine.es.client.manager.EsSearchManager;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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
    private EsCurdManager manager;


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
        return manager.simpleSearch(searchDTO, clazz);
    }
}
