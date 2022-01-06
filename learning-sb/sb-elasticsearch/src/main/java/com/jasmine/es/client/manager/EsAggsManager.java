package com.jasmine.es.client.manager;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Component;

@Component
public class EsAggsManager extends EsSearchManager {

    public EsAggsManager(RestHighLevelClient restHighLevelClient) {
        super(restHighLevelClient);
    }
}
