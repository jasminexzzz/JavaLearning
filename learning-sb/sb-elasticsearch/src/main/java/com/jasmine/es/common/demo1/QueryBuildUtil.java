//package com.jasmine.es.common.demo1;
//
//import cn.hutool.core.collection.CollectionUtil;
//import cn.hutool.core.util.StrUtil;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.index.query.RangeQueryBuilder;
//import org.elasticsearch.script.Script;
//import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
//import org.elasticsearch.search.aggregations.AggregationBuilder;
//import org.elasticsearch.search.aggregations.AggregationBuilders;
//import org.elasticsearch.search.aggregations.BucketOrder;
//import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
//import org.elasticsearch.search.sort.FieldSortBuilder;
//import org.elasticsearch.search.sort.SortOrder;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.elasticsearch.core.query.Criteria;
//import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
//import org.springframework.util.ObjectUtils;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//
//
///**
// * @ClassName: QueryBuildUtil
// * @Description: ElasticSearch创建Query的工具类
// * @author: zhangxw
// * @date: 2019/5/28
// */
//public class QueryBuildUtil {
//
//	/**
//	 * 构建复合查询构造器
//	 * 		常见的查询：
//	 * 			0、matchAllQuery
//	 * 				匹配所有文档，相当于就没有设置查询条件
//	 * 					QueryBuilder queryBuilder=QueryBuilders.matchAllQuery();
//	 * 			1、基于keyword、数字、日期、枚举类型的查询：
//	 * 				1、termQuery：单个字段匹配
//	 *					QueryBuilder queryBuilder=QueryBuilders.termQuery("fieldName", "fieldlValue");
//	 * 				2、termsQuery：多个字段匹配
//	 * 					QueryBuilder queryBuilder=QueryBuilders.termsQuery("fieldName", "fieldlValue1",
//	 * 					"fieldlValue2",...);
//	 *				3、rangeQuery：范围查询：
//	 *					QueryBuilders.rangeQuery("fieldName").from("fieldValue1").to("fieldValue2").includeUpper(false).includeLower(false);//默认是true，也就是包含
//	 *					QueryBuilder queryBuilder2 = QueryBuilders.rangeQuery("fieldName").gt("fieldValue");
//	 *					QueryBuilder queryBuilder3 = QueryBuilders.rangeQuery("fieldName").gte("fieldValue");
//	 *					QueryBuilder queryBuilder4 = QueryBuilders.rangeQuery("fieldName").lt("fieldValue");
//	 *					QueryBuilder queryBuilder5 = QueryBuilders.rangeQuery("fieldName").lte("fieldValue");
//	 *				4、existsQuery：查询某个字段不为空的查询(name is not null)
//	 *					QueryBuilders.existsQuery("name")
//	 *				5、prefixQuery：前缀匹配查询：
//	 *					QueryBuilders.prefixQuery("fieldName","fieldValue");
//	 *				6、wildcardQuery：通配符查询
//	 *					QueryBuilders.wildcardQuery("fieldName","ctr*");
//	 *					QueryBuilders.wildcardQuery("fieldName","c?r?");
//	 *				7、fuzzyQuery：模糊查询
//	 *					QueryBuilders.fuzzyQuery("hotelName", "tel").fuzziness(Fuzziness.ONE);
//	 * 			2、基于先分词在匹配的查询
//	 * 				1、matchQuery：单个字段匹配：
//	 * 					QueryBuilder queryBuilder2 = QueryBuilders.matchQuery("fieldName", "fieldlValue");
//	 * 				2、multiMatchQuery：多个字段匹配：
//	 * 					QueryBuilder queryBuilder= QueryBuilders.multiMatchQuery("fieldlValue", "fieldName1",
//	 * 					"fieldName2", ...);
//	 * 				3、queryStringQuery：字符串的左右模糊查询
//	 * 					QueryBuilders.queryStringQuery("fieldValue").field("fieldName");//左右模糊
//	 * 			3、组合查询：
//	 *				QueryBuilders.boolQuery()
//	 * 				QueryBuilders.boolQuery().must();//文档必须完全匹配条件，相当于and
//	 * 				QueryBuilders.boolQuery().mustNot();//文档必须不匹配条件，相当于not
//	 * 				QueryBuilders.boolQuery().should();//至少满足一个条件，这个文档就符合should，相当于or
//	 * 			4、moreLikeThisQuery：相似内容的查询：
//	 * 					QueryBuilders.moreLikeThisQuery(new String[] {"fieldName"}).addLikeText("pipeidhua");//如果不指定filedName，则默认全部，常用在相似内容的推荐上
//	 *
//	 * @Title: buildBoolQueryBuilder
//	 * @param idxCondition 索引查询条件
//	 * @return BoolQueryBuilder 复合查询构造器
//	 */
//	public static BoolQueryBuilder buildBoolQueryBuilder(IdxCondition idxCondition) {
//		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//		if (!ObjectUtils.isEmpty(idxCondition.getParamMap())) {
//			Map<String, Object> paramMap = idxCondition.getParamMap();
//			// 特殊处理条件（凡是不能直接使用and拼接的termQuery查询）
//			Set<String> specialConditions = new HashSet<>();
//			List<String> orkeys = idxCondition.getOrKeys();
//			List<String> orAndKeys = idxCondition.getOrAndKeys();
//			List<String> rangeKeys = idxCondition.getRangeKeys();
//			List<String> wildcardKeys = idxCondition.getWildcardKeys();
//			List<String> prefixKeys = idxCondition.getPrefixKeys();
//			List<String> notNullKeys = idxCondition.getNotNullKeys();
//			List<String> matchKeys = idxCondition.getMatchKeys();
//
//			// 先处理orAnd中的查询
//			if (CollectionUtil.isNotEmpty(orAndKeys)) {
//				BoolQueryBuilder orAndQuery = QueryBuilders.boolQuery();
//				RangeQueryBuilder rangeQueryBuilder;
//				for (String orAndkey : orAndKeys) {
//					if (StrUtil.isEmpty(orAndkey)) {
//						continue;
//					}
//					// 范围字段的处理，字段要与索引中的字段一致，否则查询不到
//					if (CollectionUtil.isNotEmpty(rangeKeys)) {
//						rangeQueryBuilder = dealRangeQuerys(rangeKeys, orAndkey, paramMap,
//								specialConditions);
//						if (null != rangeQueryBuilder) {
//							orAndQuery.must(rangeQueryBuilder);
//						}
//					}
//					// 通配符查询处理(like '%.1.2.%')
//					if (CollectionUtil.isNotEmpty(wildcardKeys)) {
//						for (String wildcardKey : wildcardKeys) {
//							if (orAndkey.equals(wildcardKey)) {
//								orAndQuery.must(QueryBuilders.wildcardQuery(wildcardKey, "*" + paramMap.get(wildcardKey) +
//										"*"));
//								specialConditions.add(wildcardKey);
//							}
//						}
//					}
//					// 前缀匹配查询(like '.1.2.%')
//					if (CollectionUtil.isNotEmpty(prefixKeys)) {
//						for (String prefixKey : prefixKeys) {
//							if (orAndkey.equals(prefixKey)) {
//								orAndQuery.must(QueryBuilders.prefixQuery(prefixKey, (String) paramMap.get(prefixKey)));
//								specialConditions.add(prefixKey);
//							}
//						}
//					}
//					// key中的值不为null【key is not null】
//					if (CollectionUtil.isNotEmpty(notNullKeys)) {
//						for (String notNullKey : notNullKeys) {
//							if (orAndkey.equals(notNullKey)) {
//								orAndQuery.must(QueryBuilders.existsQuery(notNullKey));
//								specialConditions.add(notNullKey);
//							}
//						}
//					}
//					// 分词查询处理
//					if (CollectionUtil.isNotEmpty(matchKeys)) {
//						for (String matchKey : matchKeys) {
//							if (orAndkey.equals(matchKey)) {
//								orAndQuery.must(QueryBuilders.matchQuery(matchKey, paramMap.get(matchKey)));
//								specialConditions.add(matchKey);
//							}
//						}
//					}
//
//					//其他的默认当做包含处理【key = 'zhangxw'】
//					orAndQuery.must(QueryBuilders.termQuery(orAndkey, paramMap.get(orAndkey)));
//					specialConditions.add(orAndkey);
//				}
//				boolQueryBuilder.should(orAndQuery);
//			}
//
//			// 先处理or中的查询
//			if (CollectionUtil.isNotEmpty(orkeys)) {
//				BoolQueryBuilder orQuery = QueryBuilders.boolQuery();
//				RangeQueryBuilder rangeQueryBuilder;
//				for (String orkey : orkeys) {
//					if (StrUtil.isEmpty(orkey)) {
//						continue;
//					}
//					// 范围字段的处理，字段要与索引中的字段一致，否则查询不到
//					if (CollectionUtil.isNotEmpty(rangeKeys)) {
//						rangeQueryBuilder = dealRangeQuerys(rangeKeys, orkey, paramMap,
//								specialConditions);
//						if (null != rangeQueryBuilder) {
//							orQuery.should(rangeQueryBuilder);
//						}
//					}
//					// 通配符查询处理(like '%.1.2.%')
//					if (CollectionUtil.isNotEmpty(wildcardKeys)) {
//						for (String wildcardKey : wildcardKeys) {
//							if (orkey.equals(wildcardKey)) {
//								orQuery.should(QueryBuilders.wildcardQuery(wildcardKey, "*" + paramMap.get(wildcardKey) +
//										"*"));
//								specialConditions.add(wildcardKey);
//							}
//						}
//					}
//					// 前缀匹配查询(like '.1.2.%')
//					if (CollectionUtil.isNotEmpty(prefixKeys)) {
//						for (String prefixKey : prefixKeys) {
//							if (orkey.equals(prefixKey)) {
//								orQuery.should(QueryBuilders.prefixQuery(prefixKey, (String) paramMap.get(prefixKey)));
//								specialConditions.add(prefixKey);
//							}
//						}
//					}
//					// key中的值不为null【key is not null】
//					if (CollectionUtil.isNotEmpty(notNullKeys)) {
//						for (String notNullKey : notNullKeys) {
//							if (orkey.equals(notNullKey)) {
//								orQuery.should(QueryBuilders.existsQuery(notNullKey));
//								specialConditions.add(notNullKey);
//							}
//						}
//					}
//					// 分词查询处理
//					if (CollectionUtil.isNotEmpty(matchKeys)) {
//						for (String matchKey : matchKeys) {
//							if (orkey.equals(matchKey)) {
//								orQuery.should(QueryBuilders.matchQuery(matchKey, paramMap.get(matchKey)));
//								specialConditions.add(matchKey);
//							}
//						}
//					}
//
//					//其他的默认当做包含处理【key = 'zhangxw'】
//					orQuery.should(QueryBuilders.termQuery(orkey, paramMap.get(orkey)));
//					specialConditions.add(orkey);
//				}
//				boolQueryBuilder.must(orQuery);
//			}
//
//			// 范围字段的处理，字段要与索引中的字段一致，否则查询不到
//			if (CollectionUtil.isNotEmpty(rangeKeys)) {
//				RangeQueryBuilder rangeQueryBuilder = null;
//				for (String rangeKey : rangeKeys) {
//					if (StrUtil.isEmpty(rangeKey)) {
//						continue;
//					}
//					rangeQueryBuilder = dealRangeQuery(paramMap, rangeKey, rangeQueryBuilder, specialConditions);
//				}
//				if (null != rangeQueryBuilder) {
//					boolQueryBuilder.must(rangeQueryBuilder);
//				}
//			}
//
//			// 通配符查询处理(like '%.1.2.%')
//			if (CollectionUtil.isNotEmpty(wildcardKeys)) {
//				for (String wildcardKey : wildcardKeys) {
//					if (StrUtil.isEmpty(wildcardKey)) {
//						continue;
//					}
//					boolQueryBuilder.must(QueryBuilders.wildcardQuery(wildcardKey, "*" + paramMap.get(wildcardKey) + "*"));
//					specialConditions.add(wildcardKey);
//				}
//			}
//
//			// 前缀匹配查询(like '.1.2.%')
//			if (CollectionUtil.isNotEmpty(prefixKeys)) {
//				for (String prefixKey : prefixKeys) {
//					if (StrUtil.isEmpty(prefixKey)) {
//						continue;
//					}
//					boolQueryBuilder.must(QueryBuilders.prefixQuery(prefixKey, (String) paramMap.get(prefixKey)));
//					specialConditions.add(prefixKey);
//				}
//			}
//			// key中的值不为null【key is not null】
//			if (CollectionUtil.isNotEmpty(notNullKeys)) {
//				for (String notNullKey : notNullKeys) {
//					if (StrUtil.isEmpty(notNullKey)) {
//						continue;
//					}
//					boolQueryBuilder.must(QueryBuilders.existsQuery(notNullKey));
//					specialConditions.add(notNullKey);
//				}
//			}
//			// 分词查询处理
//			if (CollectionUtil.isNotEmpty(matchKeys)) {
//				for (String matchKey : matchKeys) {
//					if (StrUtil.isEmpty(matchKey)) {
//						continue;
//					}
//					boolQueryBuilder.must(QueryBuilders.matchQuery(matchKey, paramMap.get(matchKey)));
//					specialConditions.add(matchKey);
//				}
//			}
//
//			paramMap.entrySet().stream().filter(entry -> !specialConditions.contains(entry.getKey()))
//					.forEach(entry -> boolQueryBuilder.must(QueryBuilders.termQuery(entry.getKey(), entry.getValue())));
//		}
//
//		if (StrUtil.isNotEmpty(idxCondition.getScript())) {
//			boolQueryBuilder.must(QueryBuilders.scriptQuery(new Script(idxCondition.getScript())));
//		}
//		return boolQueryBuilder;
//	}
//
//	/**
//	 * 创建SearchQuery(查询搜索器)
//	 * @Title: createSearchQuery
//	 * @param queryBuilder 查询创建器
//	 * @param page 当前页码
//	 * @param size 每页的条数
//	 * @param sortField 排序字段
//	 * @param sortOrder 排序的方式
//	 * @return SearchQuery 搜索查询器
//	 */
//	public static SearchQuery createSearchQuery(QueryBuilder queryBuilder, int page, int size, String sortField,
//												String sortOrder) {
//		if (null == queryBuilder) {
//			// 默认查询所有的
//			queryBuilder = QueryBuilders.matchAllQuery();
//		}
//
//		// 给分页参数设置默认值
//		if (page < 1) {
//			page = 1;
//		}
//
//		if (size < 0) {
//			size = 10;
//		}
//
//		// 本地搜索查询构建器
//		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
//		// 设置查询构建器
//		nativeSearchQueryBuilder.withQuery(queryBuilder);
//		// 设置分页参数
//		nativeSearchQueryBuilder.withPageable(PageRequest.of(page - 1, size));
//		// 设置排序参数
//		if (StrUtil.isNotEmpty(sortField)) {
//			FieldSortBuilder fieldSortBuilder = new FieldSortBuilder(sortField);
//			if (SortOrder.ASC.toString().equalsIgnoreCase(sortOrder)) {
//				fieldSortBuilder.order(SortOrder.ASC);
//			} else if (SortOrder.DESC.toString().equalsIgnoreCase(sortOrder)) {
//				fieldSortBuilder.order(SortOrder.DESC);
//			} else {
//				fieldSortBuilder.order(SortOrder.ASC);
//			}
//			nativeSearchQueryBuilder.withSort(fieldSortBuilder);
//		}
//		// 构建搜索查询器
//		return nativeSearchQueryBuilder.build();
//	}
//
//	/**
//	 * 创建CriteriaQuery(标准查询器)
//	 * @Title: createCriteriaQuery
//	 * @param criteria 标准对象
//	 * @param page 当前页码
//	 * @param size 每页的条数
//	 * @param sortField 排序字段
//	 * @param sortOrder 排序的方式
//	 * @return CriteriaQuery
//	 */
//	public static CriteriaQuery createCriteriaQuery(Criteria criteria, int page, int size, String sortField,
//													String sortOrder) {
//		if (null == criteria) {
//			criteria = new Criteria();
//		}
//
//		// 给分页参数设置默认值
//		if (page < 1) {
//			page = 1;
//		}
//
//		if (size < 0) {
//			size = 10;
//		}
//
//		// 创建标准查询器
//		CriteriaQuery criteriaQuery = new CriteriaQuery(criteria);
//		// 设置分页信息
//		criteriaQuery.setPageable(PageRequest.of(page - 1, size));
//		// 设置排序信息
//		if (StrUtil.isNotEmpty(sortField)) {
//			if (Sort.Direction.DESC.toString().equalsIgnoreCase(sortOrder)) {
//				criteriaQuery.addSort(Sort.by(Sort.Direction.DESC, sortField));
//			} else {
//				criteriaQuery.addSort(Sort.by(Sort.Direction.ASC, sortField));
//			}
//		}
//		return criteriaQuery;
//	}
//
//	/**
//	 * 构件聚合查询
//	 * 		聚合包含两种：
//	 * 			1、指标(Metrics)聚合：主要针对number类型的的数据，需要ES做比较多的计算工作。
//	 * 				1、min 函数
//	 * 					MinBuilder min=	AggregationBuilders.min("min_price").field("price");
//	 * 				2、max 函数
//	 * 					MaxBuilder mb= AggregationBuilders.max("max_price").field("price");
//	 * 				3、count 函数：
//	 * 					统计某个字段的数量：
//	 * 						ValueCountBuilder vcb=  AggregationBuilders.count("count_uid").field("uid");
//	 * 					去重统计某个字段的数量（有少量误差）：
//	 * 						CardinalityBuilder cb= AggregationBuilders.cardinality("distinct_count_uid").field("uid");
//	 * 				3、sum 函数
//	 * 					SumBuilder  sumBuilder=	AggregationBuilders.sum("sum_price").field("price");
//	 * 				4、avg 函数
//	 * 					AvgBuilder ab= AggregationBuilders.avg("avg_price").field("price");
//	 * 				5、top hit
//	 *					TopHitsBuilder thb=  AggregationBuilders.topHits("top_result");
//	 * 			2、桶(Buckets)聚合：划分不同的“桶”，将数据分配到不同的“桶”里。非常类似sql中的group语句的含义。
//	 * 				1、terms：按照某个字段分组
//	 * 					TermsBuilder tb=  AggregationBuilders.terms("group_name").field("name");
//	 * 				2、range
//	 * 					AggregationBuilders.range("agg")
//	 * 					.field("price").addUnboundedTo(50)
//	 * 					.addRange(51, 100).addRange(101, 1000)
//	 * 					.addUnboundedFrom(1001);
//	 * 				3、data range
//	 * 					AggregationBuilders.dateRange("agg")
//	 * 					.field("date").format("yyyy")
//	 * 					.addUnboundedTo("1970").addRange("1970", "2000")
//	 * 					.addRange("2000", "2010").addUnboundedFrom("2009");
//	 * 				4、Histogram
//	 * 				5、date Histogram：按照日期间隔分组
//	 *					DateHistogramBuilder dhb= AggregationBuilders.dateHistogram("gf_count").field("createDate").dateHistogramInterval(DateHistogramInterval.MONTH).format("yyyy-MM-dd")
//	 *					AggregationBuilders.dateHistogram("count").field("createDate").dateHistogramInterval(DateHistogramInterval.MONTH).format("yyyy-MM-dd")
//	 * 						//.minDocCount(0)//强制返回空 buckets,既空的月份也返回
//	 * 						.extendedBounds(new ExtendedBounds("2012-01-12", "2016-12-12"))// Elasticsearch 默认只返回你的数据中最小值和最大值之间的 buckets
//	 * 				6、Missing
//	 * @Title: buildAggregationQuery
//	 * @param statisticType 统计类型【四大统计类型的其中之一】
//	 * @param boolQueryBuilder 【相当于where语句】
//	 * @param idxCondition 【条件】
//	 * @return SearchQuery 搜索查询器
//	 */
//	public static SearchQuery buildAggregationQuery(StatisticType statisticType, BoolQueryBuilder boolQueryBuilder,
//													IdxCondition idxCondition) {
//		// 本地搜索查询构建器
//		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
//		// 设置查询构建器
//		nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
//
//		// 聚合构建器
//		AggregationBuilder aggregationBuilder;
//		// count聚合统计
//		if (StatisticType.COUNT == statisticType) {
//			// 如果用的count(*)则field要使用"_uid"。如果用的count(字段)，则field要使用"字段"。
//			aggregationBuilder = AggregationBuilders.count(IdxCondition.AGG_KEY).field(idxCondition.getAggKey() == null ? IdxCondition.UID : idxCondition.getAggKey());
//		} else if (StatisticType.COUNT_DISTINCT == statisticType) {
//			// 去重的聚合统计
//			aggregationBuilder = AggregationBuilders.cardinality(IdxCondition.AGG_KEY).field(idxCondition.getAggKey());
//		} else if (StatisticType.WORD == statisticType) {
//			// 计算该字段(假设为username)在所有文档中的出现频次。terms括号中的内容是：给聚合查询取的名
//			aggregationBuilder = AggregationBuilders.terms(IdxCondition.GROUP_KEY_PREFIX + "0").field(idxCondition.getAggKey()).size(idxCondition.getRecordNum());
//		} else {
//			// sum的聚合统计
//			aggregationBuilder = AggregationBuilders.sum(IdxCondition.AGG_KEY).field(idxCondition.getAggKey());
//		}
//
//		// 对分组字段字段遍历，进行分组统计
//		if (!ObjectUtils.isEmpty(idxCondition.getGroupKeys())) {
//			// 聚合条件生成器
//			TermsAggregationBuilder termsAggregationBuilder = null;
//			List<String> groupKeys = idxCondition.getGroupKeys();
//			TermsAggregationBuilder tempBuilder;
//			for (int i = groupKeys.size() - 1; i >= 0; i--) {
//				// 计算该字段(假设为username)在所有文档中的出现频次。terms括号中的内容是：给聚合查询取的名
//				tempBuilder = AggregationBuilders.terms(IdxCondition.GROUP_KEY_PREFIX + i).field(groupKeys.get(i)).size(idxCondition.getRecordNum());
//				if (termsAggregationBuilder == null) {
//					if (tempBuilder != null) {
//						// 子聚合条件拼接
//						tempBuilder.subAggregation(aggregationBuilder);
//						// 添加聚合排序
//						if (idxCondition.isAggOrder()) {
//							tempBuilder.order(BucketOrder.compound(BucketOrder.aggregation(IdxCondition.AGG_KEY, idxCondition.isAsc())));
//						} else {
//							tempBuilder.order(BucketOrder.key(idxCondition.isAsc()));
//						}
//					}
//					termsAggregationBuilder = tempBuilder;
//				} else {
//					// 子聚合条件拼接
//					termsAggregationBuilder = tempBuilder.subAggregation(termsAggregationBuilder);
//				}
//			}
//			// 添加聚合构建器封装的条件
//			nativeSearchQueryBuilder.addAggregation(termsAggregationBuilder);
//		} else {
//			// 添加聚合构建器封装的条件
//			nativeSearchQueryBuilder.addAggregation((AbstractAggregationBuilder) aggregationBuilder);
//		}
//		// 构建搜索查询器
//		return nativeSearchQueryBuilder.build();
//	}
//
//	/**
//	 * 批量处理范围查询
//	 * @Title: dealRangeQuerys
//	 * @param rangeKeys 范围字段集合
//	 * @param orkey or或者orAnd字段
//	 * @param paramMap 原始参数
//	 * @param specialConditions 范围查询构造器
//	 * @return RangeQueryBuilder 范围查询构造器
//	 */
//	private static RangeQueryBuilder dealRangeQuerys(List<String> rangeKeys, String orkey, Map<String, Object> paramMap,
//													 Set<String> specialConditions) {
//		RangeQueryBuilder rangeQueryBuilder = null;
//		for (String rangeKey : rangeKeys) {
//			if (orkey.equals(rangeKey)) {
//				rangeQueryBuilder = dealRangeQuery(paramMap, rangeKey, rangeQueryBuilder, specialConditions);
//			}
//		}
//
//		return rangeQueryBuilder;
//	}
//
//	/**
//	 * 处理范围查询
//	 * @Title: dealRangeQuery
//	 * @param paramMap 原始参数
//	 * @param rangeKey 范围字段
//	 * @param rangeQueryBuilder 范围查询构造器
//	 * @param specialConditions 特殊条件集合
//	 * @return RangeQueryBuilder 范围查询构造器
//	 */
//	private static RangeQueryBuilder dealRangeQuery(Map<String, Object> paramMap, String rangeKey,
//													RangeQueryBuilder rangeQueryBuilder,
//													Set<String> specialConditions) {
//		if (paramMap.containsKey(rangeKey + IdxCondition.RANGE_FROM_SUFFIX)) {
//			if (null == rangeQueryBuilder) {
//				rangeQueryBuilder = QueryBuilders.rangeQuery(rangeKey);
//			}
//			rangeQueryBuilder.gte(paramMap.get(rangeKey + IdxCondition.RANGE_FROM_SUFFIX));
//			specialConditions.add(rangeKey + IdxCondition.RANGE_FROM_SUFFIX);
//		}
//		if (paramMap.containsKey(rangeKey + IdxCondition.RANGE_TO_SUFFIX)) {
//			if (null == rangeQueryBuilder) {
//				rangeQueryBuilder = QueryBuilders.rangeQuery(rangeKey);
//			}
//			rangeQueryBuilder.lt(paramMap.get(rangeKey + IdxCondition.RANGE_TO_SUFFIX));
//			specialConditions.add(rangeKey + IdxCondition.RANGE_TO_SUFFIX);
//		}
//		return rangeQueryBuilder;
//	}
//}
