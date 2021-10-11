package com.jasmine.es.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * @author wangyf
 * @since 0.0.1
 */
@Slf4j
@RestController
@RequestMapping("/es")
public class GetController {

    @Autowired
    private ElasticsearchRestTemplate es;

    @RequestMapping("/get")
    public Map<String, Object> get () {
        IndexCoordinates indexCoordinates = IndexCoordinates.of("test_index");
        return es.indexOps(indexCoordinates).getMapping();
    }

}
