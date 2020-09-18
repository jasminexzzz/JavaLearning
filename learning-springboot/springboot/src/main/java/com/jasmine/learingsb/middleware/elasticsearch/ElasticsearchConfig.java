package com.jasmine.learingsb.middleware.elasticsearch;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author jasmineXz
 */

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.jasmine.learingsb.middleware.elasticsearch")
public class ElasticsearchConfig {

}
