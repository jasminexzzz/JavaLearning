package com.jasmine.中间件.elasticsearch;

import com.jasmine.Other.MyUtils.JsonUtil;
import com.jasmine.中间件.ElasticSearch.RestClient用法官方推荐.Games;
import com.jasmine.中间件.ElasticSearch.RestClient用法官方推荐.RestHighFactory;
import org.elasticsearch.Build;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.Version;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequest;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.main.MainResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.ScrollableHitSource;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;

import static java.util.Collections.singletonMap;

/**
 * @link https://www.cnblogs.com/LUA123/p/10157386.html#top
 * @author : jasmineXz
 */
@SuppressWarnings("all")
public class EsByRestClientTest {

    private RestHighLevelClient client;// es链接
    List<Games> documentList = new ArrayList<>();// 文档集合

    // 初始化数据
    {
        Games game1 = new Games(1,"巫师3",
                "是由CD Projekt RED制作发行的一款由同名小说改编而成的角色扮演类单机游戏");
        documentList.add(game1);

        Games game2 = new Games(2,"GTA5",
                "是由Rockstar Games游戏公司出版发行的一款围绕犯罪为主题的开放式动作冒险游戏");
        documentList.add(game2);

        Games game3 = new Games(3,"剑网3",
                "是由金山软件西山居开发，金山运营的3D武侠角色扮演电脑客户端游戏。");
        documentList.add(game3);

        Games game4 = new Games(4,"刺客信条2",
                "是育碧公司的蒙特利尔工作室开发的一款第三人称冒险类单机游戏");
        documentList.add(game4);

        Games game5 = new Games(5,"美国末日",
                "是由著名工作室顽皮狗第二团队秘密开发两年的作品，讲述了人类因现代传染病而面临绝种危机，当环境从废墟的都市再度自然化时，幸存的人类为了生存而自相残杀的故事");
        documentList.add(game5);
    }

    @Before
    public void createClient(){
        client = RestHighFactory.getInstance().getClient();
    }

    // ===================================================== 新增 ===============================================================

    /**
     * 新增一条
     */
    @Test
    public void addIndex() throws IOException {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "kimchy");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out Elasticsearch");

        IndexRequest indexRequest = new IndexRequest("book", "itbook", "6").source(jsonMap);
        IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println(response.status().name());
    }

    /**
     * 异步新增
     */
    @Test
    public void addIndexAsync() throws InterruptedException {
        ActionListener listener = new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                    System.out.println("数据不存在,新增. 执行状态 [ " + indexResponse.status().name() + "]");
                } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                    System.out.println("数据存在,修改. 执行状态 [ " + indexResponse.status().name() + "]");
                }
                // 处理成功分片小于总分片的情况
                ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
                if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
                    // Todo
                }
            }
            @Override
            public void onFailure(Exception e) {
                System.out.println("异步执行错误:" + e.getMessage());
                e.printStackTrace();
            }
        };
        IndexRequest indexRequest = new IndexRequest("testindex", "testType", "1")
                .source("user", "wangyunfei",
                        "message", "异步写入数据")
                .routing("my_route");   // 指定路由
        client.indexAsync(indexRequest, RequestOptions.DEFAULT, listener);  // 异步方式
        Thread.sleep(2000);//等待数据写入
    }

    /**
     * 批量新增
     * @throws IOException
     */
    public void addMulti() throws IOException {
        BulkRequest request = new BulkRequest();
        request.add(new IndexRequest("twitter", "t_doc", "25")
                .source(XContentType.JSON,"user", "Tom","flag","1"));
        request.add(new IndexRequest("twitter", "t_doc", "26")
                .source(XContentType.JSON,"user", "foo","flag","2"));
        request.add(new IndexRequest("twitter", "t_doc", "27")
                .source(XContentType.JSON,"user", "bar","flag","2"));
        request.add(new IndexRequest("twitter", "t_doc", "28")
                .source(XContentType.JSON,"user", "baz","flag","2"));
        BulkResponse bulk = client.bulk(request, RequestOptions.DEFAULT);
        System.out.println("Status:" + bulk.status().name() + ",hasFailures:" + bulk.hasFailures());
        // 下面是multiGet
        MultiGetRequest multiGetRequest = new MultiGetRequest()
                .add(new MultiGetRequest.Item("twitter", "t_doc", "25"))
                .add(new MultiGetRequest.Item("twitter", "t_doc", "26"))
                .add(new MultiGetRequest.Item("twitter", "t_doc", "27"))
                .add(new MultiGetRequest.Item("twitter", "t_doc", "28"));
        MultiGetResponse response = client.mget(multiGetRequest, RequestOptions.DEFAULT);
        MultiGetItemResponse[] itemResponses = response.getResponses();
        for(MultiGetItemResponse r : itemResponses){
            System.out.println(r.getResponse().getSourceAsString());
        }
    }

    /**
     * BulkProcessor 批量插入
     * BulkProcessor通过提供一个实用程序类来简化Bulk API的使用，它允许索引/更新/删除操作在添加到处理器时透明地执行。
     * 为了执行请求，BulkProcessor需要以下组件:
     * - RestHighLevelClient    : 此客户端用于执行BulkRequest和检索BulkResponse
     * - BulkProcessor.Listener : 在每个BulkRequest执行之前和之后，或者在BulkRequest失败时，都会调用这个侦听器
     * - BulkProcessor.builder  : 方法可用于构建新的BulkProcessor:
     */
    @Test
    public void addBuldProcessor() throws InterruptedException {

        BulkProcessor.Listener listener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
                int numberOfActions = request.numberOfActions();
                System.out.println("请求数量：" + numberOfActions);
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                if (response.hasFailures()) {
                    System.out.println("Bulk 失败,ID:" + executionId + ",Status:" + response.status().name());
                    for (BulkItemResponse bulkItemResponse : response) {
                        if (bulkItemResponse.isFailed()) {
                            BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                            System.err.println(failure.getCause().getMessage());
                        }
                    }
                } else {
                    System.out.println("Bulk "+ executionId +" 成功 in" + response.getTook().getMillis() + "s");
                }
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                System.out.println("Failed to execute bulk:" + failure);
            }
        };

        BiConsumer<BulkRequest, ActionListener<BulkResponse>> bulkConsumer = (request, bulkListener) ->
                client.bulkAsync(request, RequestOptions.DEFAULT, bulkListener);
        BulkProcessor bulkProcessor = BulkProcessor.builder(bulkConsumer, listener)
                // 1. 请求（Index，Update，Delete）的数量达到500，就刷新一次bulk request【默认1000】
                .setBulkActions(500)
                // 2. 累计请求所占的空间达到1M，就刷新一次bulk request【默认5M】
                // .setBulkSize(new ByteSizeValue(1L, ByteSizeUnit.MB))
                // 3. 设置允许执行的并发请求数量(默认为1，使用0只允许执行单个请求)
                .setConcurrentRequests(0)
                // 4. 每隔一段时间刷新一次【默认未设置】
                // .setFlushInterval(TimeValue.timeValueSeconds(10L))
                // 5. 设置一个初始等待1秒并重试3次的Backoff策略
                .setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1L), 3))
                .build();
//        for(int i = 1; i <= 35; i++){
//            bulkProcessor.add(new IndexRequest("game", "itbook", ""+i)
//                    .source(XContentType.JSON,"bookname","Java书籍"+i,"author","作者"+i));
//        }

        for(int i = 0 ; i < documentList.size(); i++){
            bulkProcessor.add(new IndexRequest("game", "pc", String.valueOf(documentList.get(i).getId())).source(JsonUtil.bean2Map(documentList.get(i))));
        }

        bulkProcessor.flush();
        Thread.sleep(2000);
        bulkProcessor.close();
    }

    // ===================================================== 查询 ===============================================================

    /**
     * 获取单个文档
     */
    @Test
    public void getOne() throws IOException {
        GetRequest request = new GetRequest("book", "itbook", "1");   // 指定routing的数据，查询也要指定
        try {
            GetResponse response = client.get(request, RequestOptions.DEFAULT);
            System.out.println(response.getSourceAsString());
        } catch (ElasticsearchException e) {
            // 处理找不到index的异常
            if(e.status() == RestStatus.NOT_FOUND){
                System.out.println("未查询到数据");
            }
        }
    }

    /**
     * 异步获取
     * 需要的字段
     * 排除的字段
     */
    @Test
    public void getOneAsync() throws InterruptedException {
        ActionListener<GetResponse> listener = new ActionListener<GetResponse>() {
            @Override
            public void onResponse(GetResponse documentFields) {
                System.out.println(documentFields.getSourceAsString());
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("异步执行错误:" + e.getMessage());
                e.printStackTrace();
            }
        };

        GetRequest request = new GetRequest("book", "itbook", "1");
        String[] includes = new String[]{"bookName"};   // 包含的字段
        String[] excludes = Strings.EMPTY_ARRAY;                // 排除的字段
        FetchSourceContext fetchSourceContext =
                new FetchSourceContext(true, includes, excludes);
        request.fetchSourceContext(fetchSourceContext);
        client.getAsync(request, RequestOptions.DEFAULT, listener);
        Thread.sleep(2000);
    }


    /**
     * matchQuery
     * 排序，高亮查询
     */
    @Test
    public void search() throws IOException {
        try {
            SearchResponse search = client.search(new SearchRequest("")
                    .source(new SearchSourceBuilder()
                            .query(QueryBuilders.matchQuery("content", "是"))
                            // 根据分数倒序排序
                            .sort("_id", SortOrder.ASC)
                            // 返回结果开始位置
                            .from(0)
                            // 返回结果数量
                            .size(5)
                            // 超时
                            .timeout(TimeValue.timeValueSeconds(10))
                            // 高亮
                            .highlighter(new HighlightBuilder()
                                    .field("content",200)
                                    .preTags("<pre>").postTags("</pre>"))
                    ), RequestOptions.DEFAULT);

            System.out.println("=====> 命中结果 : " + search.getHits().totalHits);

            search.getHits().forEach(e -> {
                System.out.println("=====> 分数：" + e.getScore());
                System.out.println("=====> 结果：" + e.getSourceAsString());

                //高亮内容
                Map<String, HighlightField> highlightFields = e.getHighlightFields();
                for (String key : highlightFields.keySet()){
                    HighlightField field = highlightFields.get(key);
                    System.out.println(key + "：" + field.fragments()[0]);
                }
                System.out.println("──────────────────────────────────────────────────────────────────────────────────────────────");

            });
        } catch (ElasticsearchException e) {
            if(e.status() == RestStatus.NOT_FOUND){
                System.out.println("未查询到索引-" + e.getIndex());
            }
        }
    }


    /**
     * 滚动获取数据
     */
    @Test
    public void scrollSearch() throws IOException {
        // 设置超时
        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
        SearchRequest request = new SearchRequest("game");
        request.source(new SearchSourceBuilder()
                .query(QueryBuilders.matchAllQuery())
                .sort("_id",SortOrder.ASC)
                .size(1)) // 每批大小
                .scroll(scroll);    // 设置scroll
        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT); // 执行查询
        String scrollId = searchResponse.getScrollId();
        SearchHit[] hits = searchResponse.getHits().getHits();
        while(hits != null && hits.length > 0) {
            System.out.println("========Begin=======");
            for (SearchHit hit : hits) {
                System.out.println(hit.getSourceAsString());
            }

            System.out.println("Size:" + hits.length + ",Scroll:" + scrollId);
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId).scroll(scroll);    // 设置SearchScrollRequest
            searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);  // 拉取新的数据
            scrollId = searchResponse.getScrollId();
            hits = searchResponse.getHits().getHits();
            System.out.println("========End=======");
        };
        // 当scroll超时时，Search Scroll API使用的搜索上下文将自动删除
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);
        ClearScrollResponse clearScrollResponse = client.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
        boolean succeeded = clearScrollResponse.isSucceeded();
        System.out.println("ClearScroll:" + succeeded);
    }

    /**
     * 文档是否存在
     */
    @Test
    public void exist() throws IOException {
        GetRequest request = new GetRequest("book", "itbook", "1");
        request.storedFields("_none_"); // 禁用获取存储字段
        request.fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE); // 禁用抓取_source
        boolean exists = client.exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    /**
     * 查询集群状态
     */
    @Test
    public void info() throws IOException {
        boolean ping = client.ping(RequestOptions.DEFAULT);
        MainResponse info = client.info(RequestOptions.DEFAULT);
        ClusterName clusterName = info.getClusterName();
        String clusterUuid = info.getClusterUuid();
        String nodeName = info.getNodeName();
        Version version = info.getVersion();
        Build build = info.getBuild();
        System.out.println("Ping:" + ping);
        System.out.println("集群名称：" + clusterName.value());
        System.out.println("Uuid：" + clusterUuid);
        System.out.println("节点名称：" + nodeName);
        System.out.println("Version：" + version.toString());
        System.out.println("Bulid：" + build.toString());
    }

    /**
     * 分词器
     */
    @Test
    public void analyze() throws IOException {
        AnalyzeRequest request = new AnalyzeRequest();
        request.text("美国末日",
                "是由著名工作室顽皮狗第二团队秘密开发两年的作品，讲述了人类因现代传染病而面临绝种危机，" +
                        "当环境从废墟的都市再度自然化时，幸存的人类为了生存而自相残杀的故事");
        request.analyzer("ik_smart");
        AnalyzeResponse response = client.indices().analyze(request, RequestOptions.DEFAULT);
        List<AnalyzeResponse.AnalyzeToken> tokens = response.getTokens();

        for(AnalyzeResponse.AnalyzeToken t : tokens){
            int endOffset = t.getEndOffset();
            int position = t.getPosition();
            int positionLength = t.getPositionLength();
            int startOffset = t.getStartOffset();
            String term = t.getTerm();
            String type = t.getType();
            System.out.println("Start:" + startOffset + ",End:" + endOffset + ",Position:" + position + ",Length:" + positionLength +
                    ",Term:" + term + ",Type:" + type);
        }
    }

    /**
     * 获取mapping
     */
    @Test
    public void getMapping() throws IOException {
        GetMappingsRequest request = new GetMappingsRequest();
        request.indices("game");
        request.types("pc");
        request.masterNodeTimeout(TimeValue.timeValueMinutes(1));
        GetMappingsResponse getMappingResponse = client.indices().getMapping(request, RequestOptions.DEFAULT);
        getMappingResponse.getMappings().forEach(e -> {
            String key = e.key;
            ImmutableOpenMap<String, MappingMetaData> value = e.value;
            value.forEach(v -> {
                System.out.println(key + "|" + v.key + "|" + v.value.getSourceAsMap());
            });
        });
    }

    /**
     * 获取指定字段的Mapping
     */
    @Test
    public void getFieldMapping() throws IOException {
        GetFieldMappingsRequest request = new GetFieldMappingsRequest();
        request.indices("game");   // 可以多个索引
        request.types("pc");             // 多个类型
        request.fields("name","content");  // 多个字段
        GetFieldMappingsResponse response = client.indices().getFieldMapping(request, RequestOptions.DEFAULT);
        Map<String, Map<String, Map<String, GetFieldMappingsResponse.FieldMappingMetaData>>> mappings = response.mappings();
        mappings.keySet().forEach(e -> {
            Map<String, Map<String, GetFieldMappingsResponse.FieldMappingMetaData>> mapMap = mappings.get(e);
            mapMap.keySet().forEach(i -> {
                Map<String, GetFieldMappingsResponse.FieldMappingMetaData> metaDataMap = mapMap.get(i);
                metaDataMap.keySet().forEach(j -> {
                    GetFieldMappingsResponse.FieldMappingMetaData fieldMappingMetaData = metaDataMap.get(j);
                    System.out.println(e + "|" + i + "|" + j + "|" + fieldMappingMetaData.sourceAsMap());
                });
            });
        });
    }
    // ===================================================== 删除 ===============================================================

    /**
     * 删除一条
     */
    @Test
    public void deleteOne() throws IOException {
        DeleteRequest request = new DeleteRequest("book", "itbook", "6");
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        System.out.println(response.status().name());
        // 处理找不到的情况
        if (response.getResult() == DocWriteResponse.Result.NOT_FOUND) {
            System.out.println("未找到数据,删除失败!");
        }
    }


    /**
     * 根据查询删除数据
     */
    @Test
    public void deleteByQuery() throws IOException {
        SearchRequest sr = new SearchRequest("book")
                .source(new SearchSourceBuilder()
                                .query(QueryBuilders.matchQuery("bookName", "Java")));

        DeleteByQueryRequest request = new DeleteByQueryRequest(sr);
        request.setConflicts("proceed");    // 发生冲突即略过
//        request.setQuery(QueryBuilders.matchQuery("flag","2"));
        BulkByScrollResponse bulkResponse = client.deleteByQuery(request, RequestOptions.DEFAULT);
        TimeValue timeTaken = bulkResponse.getTook();
        boolean timedOut = bulkResponse.isTimedOut();
//        long totalDocs = bulkResponse.ge;
        long updatedDocs = bulkResponse.getUpdated();
        long deletedDocs = bulkResponse.getDeleted();
        long batches = bulkResponse.getBatches();
        long noops = bulkResponse.getNoops();
        long versionConflicts = bulkResponse.getVersionConflicts();
        System.out.println("花费时间：" + timeTaken + ",是否超时：" + timedOut + ",总文档数：" + null + ",更新数：" +
                updatedDocs + ",删除数：" + deletedDocs + ",批量次数：" + batches + ",跳过数：" + noops + ",冲突数：" + versionConflicts);
        List<ScrollableHitSource.SearchFailure> searchFailures = bulkResponse.getSearchFailures();  // 搜索期间的故障
        searchFailures.forEach(e -> {
            System.err.println("Cause:" + e.getReason().getMessage() + "Index:" + e.getIndex() + ",NodeId:" + e.getNodeId() + ",ShardId:" + e.getShardId());
        });
        List<BulkItemResponse.Failure> bulkFailures = bulkResponse.getBulkFailures();   // 批量索引期间的故障
        bulkFailures.forEach(e -> {
            System.err.println("Cause:" + e.getCause().getMessage() + "Index:" + e.getIndex() + ",Type:" + e.getType() + ",Id:" + e.getId());
        });
    }

    /**
     * BulkProcessor 批量删除
     */
    @Test
    public void eleteBuldProcessor() throws InterruptedException {

        BulkProcessor.Listener listener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
                int numberOfActions = request.numberOfActions();
                System.out.println("请求数量：" + numberOfActions);
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                if (response.hasFailures()) {
                    System.out.println("Bulk 失败,ID:" + executionId + ",Status:" + response.status().name());
                    for (BulkItemResponse bulkItemResponse : response) {
                        if (bulkItemResponse.isFailed()) {
                            BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                            System.err.println(failure.getCause().getMessage());
                        }
                    }
                } else {
                    System.out.println("Bulk "+ executionId +" 成功 in" + response.getTook().getMillis() + "s");
                }
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                System.out.println("Failed to execute bulk:" + failure);
            }
        };

        BiConsumer<BulkRequest, ActionListener<BulkResponse>> bulkConsumer = (request, bulkListener) ->
                client.bulkAsync(request, RequestOptions.DEFAULT, bulkListener);
        BulkProcessor bulkProcessor = BulkProcessor.builder(bulkConsumer, listener)
                // 1. 请求（Index，Update，Delete）的数量达到500，就刷新一次bulk request【默认1000】
                .setBulkActions(500)
                // 2. 累计请求所占的空间达到1M，就刷新一次bulk request【默认5M】
                // .setBulkSize(new ByteSizeValue(1L, ByteSizeUnit.MB))
                // 3. 设置允许执行的并发请求数量(默认为1，使用0只允许执行单个请求)
                .setConcurrentRequests(0)
                // 4. 每隔一段时间刷新一次【默认未设置】
                // .setFlushInterval(TimeValue.timeValueSeconds(10L))
                // 5. 设置一个初始等待1秒并重试3次的Backoff策略
                .setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1L), 3))
                .build();
//        for(int i = 1; i <= 2000; i++){
//            bulkProcessor.add(new IndexRequest("book", "itbook", ""+i)
//                    .source(XContentType.JSON,"bookname","Java书籍"+i,"author","作者"+i));
//        }

//        for(int i = 0 ; i < documentList.size(); i++){
            bulkProcessor.add(new DeleteRequest("book", "itbook", "4"));
//        }

        bulkProcessor.flush();
        Thread.sleep(2000);
        bulkProcessor.close();
    }



    // ===================================================== 修改 ===============================================================

    /**
     * 修改一条
     * 字段不存在则添加
     */
    @Test
    public void updateOne() throws IOException {
        UpdateRequest request = new UpdateRequest("book", "itbook", "1");

        Map<String, Object> parameters = singletonMap("bookName", "测试书名");
        Script inline = new Script(ScriptType.INLINE, "painless",
                "ctx._source.bookName = params.bookName", parameters);
        request.script(inline);

        UpdateResponse update = client.update(request, RequestOptions.DEFAULT);
        System.out.println(update.status().name());
    }

    /**
     * 通过Map修改
     */
    @Test
    public void updateOneByMap() throws IOException {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("bookName", "测试书名1");

        UpdateRequest request = new UpdateRequest("book", "itbook", "1").doc(jsonMap);
        UpdateResponse update = client.update(request, RequestOptions.DEFAULT);
        System.out.println(update.status().name());
    }

    /**
     * 通过KeyPairs修改
     */
    @Test
    public void updateOneByKeyPairs() throws IOException {
        UpdateRequest request = new UpdateRequest("book", "itbook", "1")
                .doc("bookName","测试书名2","author","测试作者2");

        UpdateResponse update = client.update(request, RequestOptions.DEFAULT);
        System.out.println(update.status().name());
    }



}
