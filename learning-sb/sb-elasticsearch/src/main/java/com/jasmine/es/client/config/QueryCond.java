package com.xzzz.es.client.config;

/**
 * 查询条件枚举
 * @author wangyf
 * @since 1.2.2
 */
public enum QueryCond {
    // 不分词查询
    term,
    // 分词查询
    match,
    // 范围查询
    range,
    // 范围查询 大于
    gt,
    // 范围查询 大于等于
    gte,
    // 范围查询 小于
    lt,
    // 范围查询 小于等于
    lte
    ;
}
