//package com.jasmine.es.elasticsearch;
//
//import cn.hutool.core.collection.CollectionUtil;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
//import org.springframework.data.elasticsearch.core.query.SearchQuery;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
///**
// * @author jasmineXz
// */
//@RestController
//@RequestMapping("/es")
//public class EsController {
//
//    @Autowired(required = false)
//    private ElasticsearchRestTemplate elasticsearchRestTemplate;
//
//    @Autowired
//    private ArticleRepository articleRepository;
//
//
//    /* ============================================================================================================= */
//    /*                                                      add                                                      */
//    /* ============================================================================================================= */
//    /**
//     * 新增,修改
//     * @param article 文章信息
//     * @return 返回添加的文章信息
//     */
//    @PostMapping(value = "/add")
//    public Object add(@RequestBody ArticleEntity article) {
//        //创建索引
//        // 1、直接用名称创建索引
//        //boolean indexRes = elasticsearchTemplate.createIndex("book_es");
//        // 2、填入class对象创建索引
//        //boolean indexRes = elasticsearchTemplate.createIndex(articleEntity.class);
//        //System.out.println("======创建索引结果：" + indexRes + "=========");
//
//        ArticleEntity articleEntity = new ArticleEntity();
//        articleEntity.setArticleId(article.getArticleId());
//        articleEntity.setTitle(article.getTitle());
//        articleEntity.setContent(article.getContent());
//        articleEntity.setUserId(51);
//        articleEntity.setWeight(100);
//
////        IndexQuery indexQuery = new IndexQueryBuilder()
////                .withId(articleEntity.getId())
////                .withObject(articleEntity)
////                .build();
////        String index = elasticsearchTemplate.index(indexQuery);
//        return articleRepository.save(articleEntity);
//    }
//
//    @PostMapping(value = "/template/add")
//    public Object addByTemplate(@RequestBody ArticleEntity article) {
//        //创建索引
//        // 1、直接用名称创建索引
//        boolean indexRes = elasticsearchRestTemplate.createIndex("book_es");
//        // 2、填入class对象创建索引
////        boolean indexRes = elasticsearchRestTemplate.createIndex(articleEntity.class);
//        System.out.println("======创建索引结果：" + indexRes + "=========");
//
//        return indexRes;
//    }
//
//
//    /* ============================================================================================================= */
//    /*                                                     update                                                    */
//    /* ============================================================================================================= */
//    /**
//     * 修改
//     * @param article 文章信息
//     * @return 返回添加的文章信息
//     */
//    @PutMapping(value = "/upd")
//    public Object upd(@RequestBody ArticleEntity article) {
//        ArticleEntity articleEntity = new ArticleEntity();
//        articleEntity.setArticleId(article.getArticleId());
//        articleEntity.setTitle(article.getTitle());
//        articleEntity.setContent(article.getContent());
//        articleEntity.setUserId(51);
//        articleEntity.setWeight(100);
//        return articleRepository.save(articleEntity);
//    }
//
//    /* ============================================================================================================= */
//    /*                                                     delete                                                    */
//    /* ============================================================================================================= */
//    /**
//     * 删除全部
//     */
//    @DeleteMapping(value = "/del/all")
//    public Object delAll() {
//        articleRepository.deleteAll();
//        return "全部删除成功";
//    }
//
//    /**
//     * 根据ID删除
//     * @param article 文章Id
//     */
//    @DeleteMapping(value = "/del/id")
//    public Object delById(@RequestBody ArticleEntity article) {
//        articleRepository.deleteById(article.getArticleId());
//        return "删除成功";
//    }
//
//
//    /* ============================================================================================================= */
//    /*                                                      get                                                      */
//    /* ============================================================================================================= */
//    /**
//     * 查询全部
//     * @return 包含查询信息
//     */
//    @GetMapping(value = "/all")
//    public Object all() {
//        return articleRepository.findAll();
//    }
//
//
//    /**
//     * 查询全部
//     * @return 只有内容对象
//     */
//    @GetMapping(value = "/all/content")
//    public Object getAllContent() {
//        Iterable<ArticleEntity> iterable = articleRepository.findAll();
//        List<ArticleEntity> list = new ArrayList<>();
//        iterable.forEach(list::add);
//        return list;
//    }
//
//
//    /**
//     * 根据ID查询
//     * @param articleId id
//     */
//    @GetMapping(value = "/id")
//    public Object getById(@RequestParam("articleId") String articleId) {
//        Optional<ArticleEntity> optional = articleRepository.findById(articleId);
//        if (optional.isPresent()) {
//            ArticleEntity article = optional.get();
//            return article;
//        }
//        return "没查到";
//    }
//
//
//    /**
//     * 匹配多字段
//     * @param keyword
//     * @return
//     */
//    @GetMapping(value = "/keyword")
//    public Object findByKeyword(@RequestParam("keyword") String keyword) {
//        List<ArticleEntity> contents = articleRepository.findByContentLike(keyword);
//        List<ArticleEntity> titles = articleRepository.findByTitleLike(keyword);
//        if (CollectionUtil.isNotEmpty(contents) && CollectionUtil.isNotEmpty(titles)) {
//            contents.addAll(titles);
//            return contents;
//        }
//        if (CollectionUtil.isNotEmpty(contents)) {
//            return contents;
//        }
//        if (CollectionUtil.isNotEmpty(titles)) {
//            return titles;
//        }
//        return "未查询到数据";
//    }
//
//    /**
//     * 分页查询
//     * @param content 查询体
//     * @param page 页数
//     * @param size 每页行数
//     * @return 返回查询体
//     */
//    @GetMapping("/page")
//    public Object findPage (
//            @RequestParam("content")String content,
//            @RequestParam("page") int page,
//            @RequestParam("size") int size
//    ) {
//        SearchQuery searchQuery =
//                new NativeSearchQueryBuilder()
//                    .withQuery(QueryBuilders.queryStringQuery(content))
//                    .withPageable(PageRequest.of(page, size)).build();
//        return articleRepository.search(searchQuery);
//    }
//
//
//}
