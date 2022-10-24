package com.xzzz.es.common.demo1;

/**
 * @ClassName: StatisticType
 * @Description: ElasticSearch统计分析的类型
 * @author: zhangxw
 * @date: 2019/5/30
 */
public enum StatisticType {
	/* count条件 */
	COUNT,
	/* 去重count条件 */
	COUNT_DISTINCT,
	/* sum条件 */
	SUM,
	/* ik分词 */
	WORD
}
