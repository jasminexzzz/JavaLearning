package com.xzzz.es.client.config;

/**
 * 查询逻辑枚举
 * @author wangyf
 * @since 1.2.2
 */
public enum QueryBool {
    // 必须满足
    must,
    // 必须不满足, 为过滤上下文中, 不会返回命中分数
    mustNot,
    // 或者
    should,
    // 过滤, 为过滤上下文中, 不会返回命中分数
    filter
}
