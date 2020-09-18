//package com.jasmine.learingsb.middleware.elasticsearch;
//
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
///**
// * @author jasmineXz
// */
//@Component
//public interface ArticleRepository extends ElasticsearchRepository<ArticleEntity, String> {
//
//    /**
//     * 模糊查询title
//     * @param keyword 关键字
//     * @return 查询结果
//     */
//    List<ArticleEntity> findByTitleLike(String keyword);
//
//    /**
//     * 模糊查询内容
//     * @param keyword 关键字
//     * @return 查询结果
//     */
//    List<ArticleEntity> findByContentLike(String keyword);
//
//}
