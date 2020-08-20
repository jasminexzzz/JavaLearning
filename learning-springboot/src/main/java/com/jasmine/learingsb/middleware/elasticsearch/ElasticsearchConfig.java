package com.jasmine.learingsb.middleware.elasticsearch;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.InetAddress;

/**
 * @author jasmineXz
 */

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.jasmine.learingsb.middleware.elasticsearch")
public class ElasticsearchConfig {

}
