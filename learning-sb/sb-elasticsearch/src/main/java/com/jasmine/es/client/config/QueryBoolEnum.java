package com.jasmine.es.client.config;

/**
 * 查询逻辑枚举
 * @author wangyf
 * @since 1.2.2
 */
public enum QueryBoolEnum {
    // 必须满足
    must,
    // 必须不满足
    mustNot,
    // 或者
    should
}
