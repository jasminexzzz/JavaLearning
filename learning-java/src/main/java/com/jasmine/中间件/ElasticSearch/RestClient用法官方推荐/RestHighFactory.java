package com.jasmine.中间件.ElasticSearch.RestClient用法官方推荐;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @link https://www.cnblogs.com/LUA123/p/10157386.html#top
 * Java高级REST客户机在Java低级REST客户机之上工作。它的主要目标是公开特定于API的方法，这些方法接受请求对象作为参数并返回响应对象
 * 可以同步或异步调用每个API。同步方法返回一个响应对象，而异步方法(其名称以async后缀结尾)需要一个侦听器参数
 * 一旦接收到响应或错误，侦听器参数(在低层客户机管理的线程池上)将被通知。
 * Java高级REST客户机依赖于Elasticsearch核心项目。它接受与TransportClient相同的请求参数，并返回相同的响应对象。
 * Java高级REST客户机需要Java 1.8
 * 客户机版本与开发客户机的Elasticsearch版本相同
 * 6.0客户端能够与任意6.X节点通信，6.1客户端能够与6.1、6.2和任意6.X通信
 * @author : jasmineXz
 */
public class RestHighFactory {
    private RestHighFactory(){}

    private static class Inner{
        private static final RestHighFactory instance = new RestHighFactory();
    }

    public static RestHighFactory getInstance(){
        return Inner.instance;
    }

    public RestHighLevelClient getClient(){
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        //new HttpHost("localhost", 9201, "http"),
                        new HttpHost("localhost", 32783, "http")
                )
        );
        return client;
    }

}