//package com.jasmine.es.common.demo1;
//
//import cn.hutool.core.collection.CollectionUtil;
//import cn.hutool.core.map.MapUtil;
//import cn.hutool.core.util.ArrayUtil;
//import cn.hutool.core.util.StrUtil;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.common.util.set.Sets;
//import org.elasticsearch.search.SearchHits;
//import org.elasticsearch.search.aggregations.Aggregations;
//import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;
//import org.elasticsearch.search.aggregations.bucket.terms.Terms;
//import org.elasticsearch.search.aggregations.metrics.InternalNumericMetricsAggregation;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.data.domain.Page;
//import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
//import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
//import org.springframework.data.elasticsearch.core.geo.CustomGeoModule;
//import org.springframework.data.elasticsearch.core.query.*;
//import org.springframework.util.ObjectUtils;
//
//import java.io.IOException;
//import java.sql.SQLFeatureNotSupportedException;
//import java.util.*;
//
//
///**
// * @ClassName: ElasticsearchUtil
// * @Description: Elasticsearch操作的工具类
// * @author: zhangxw
// * @date: 2019/5/16
// */
//public class ElasticsearchUtil {
//	private static Logger logger = LoggerFactory.getLogger(ElasticsearchUtil.class);
//
//	/** Elasticsearch的查询模板 */
//	private static ElasticsearchTemplate elasticsearchTemplate;
//
//	/** 批量处理数据时每次处理的数量 */
//	private static final int BATCH_COUNT = 1000;
//
//	/** 默认的返回的分词统计的纪录条数 */
//	private static final int WORD_RECORD_SIZE = 20;
//
//	/**
//	 * 构造器
//	 * @Title: ElasticsearchUtil
//	 * @param elasticsearchTemplate ES的查询模板
//	 */
//	public ElasticsearchUtil(ElasticsearchRestTemplate elasticsearchTemplate) {
//		ElasticsearchUtil.elasticsearchTemplate = elasticsearchTemplate;
//	}
//
//	/**
//	 * 创建索引
//	 * @Title: createIndex
//	 * @param t 实体对象
//	 */
//	private static <T extends BaseDomain> void createIndex(T t) {
//		// 创建索引，会根据索引类的@Document注解信息来创建
//		elasticsearchTemplate.createIndex(t.getClass());
//		// 配置映射，会根据索引类中的@Id、@Field等字段来自动完成映射
//		elasticsearchTemplate.putMapping(t.getClass());
//	}
//
//	/**
//	 * 保存单个索引
//	 * @Title: saveIndex
//	 * @param t 实体类
//	 * @return String 索引保存成功之后返回的索引Id
//	 */
//	public <T extends BaseDomain> String saveIndex(T t) {
//		if (null == t) {
//			throw new IllegalArgumentException("创建索引的实体类为空");
//		}
//		createIndex(t);
//		IndexQuery indexQuery = new IndexQueryBuilder().withId(String.valueOf(t.getId())).withObject(t).build();
//		String documentId = elasticsearchTemplate.index(indexQuery);
//		elasticsearchTemplate.refresh(t.getClass());
//		return documentId;
//	}
//
//	/**
//	 * 批量保存索引
//	 * @Title: saveAllIndex
//	 * @param list 实体类集合
//	 * @return int 处理的条数
//	 */
//	public <T extends BaseDomain> int saveAllIndex(List<T> list) {
//		if (CollectionUtil.isEmpty(list)) {
//			throw new IllegalArgumentException("创建索引的实体类集合为空");
//		}
//		int count = list.size();
//		// Jdk1.8 集合Stream的批处理
//		CollectionUtil.batchConsume(ElasticsearchUtil::saveIndexs, list, BATCH_COUNT);
//		return count;
//	}
//
//	/**
//	 * 批量保存索引
//	 * @Title: saveAllIndex
//	 * @param list 实体类集合
//	 */
//	private static <T extends BaseDomain> void saveIndexs(List<T> list) {
//		List<IndexQuery> indexQueryList = new ArrayList<>();
//		T tmp = null;
//		int counter = 0;
//		for (T t : list) {
//			if (null == t) {
//				continue;
//			}
//			if (null == tmp) {
//				tmp = t;
//				createIndex(tmp);
//			}
//			indexQueryList.add(new IndexQueryBuilder().withId(String.valueOf(t.getId())).withObject(t).build());
//
//			/*
//			// 分批处理
//			counter ++;
//			if (counter % BATCH_COUNT == 0) {
//				elasticsearchTemplate.bulkIndex(indexQueryList);
//				elasticsearchTemplate.refresh(tmp.getClass());
//				indexQueryList.clear();
//			}
//			*/
//		}
//		if (indexQueryList.size() >= 1) {
//			elasticsearchTemplate.bulkIndex(indexQueryList);
//			elasticsearchTemplate.refresh(tmp.getClass());
//		}
//	}
//
//	/**
//	 * 将实体类转成字符串【参照elasticsearchTemplate.index中构造IndexRequest的做法】
//	 * @Title: convertObjectToString
//	 * @param t 实体类
//	 * @return String 实体类字符串
//	 */
//	private static <T extends BaseDomain> String convertObjectToString(T t) {
//		try {
//			return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//					.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
//					.registerModule(new CustomGeoModule()).writeValueAsString(t);
//		} catch (JsonProcessingException e) {
//			throw new RuntimeException("将实体类转成字符串失败");
//		}
//	}
//
//	/**
//	 * 保存或修改索引
//	 * @Title: saveOrUpdateIndex
//	 * @param t 实体类
//	 * @return String 修改的索引的id
//	 */
//	public <T extends BaseDomain> String saveOrUpdateIndex(T t) {
//		if (null == t) {
//			throw new IllegalArgumentException("创建索引的实体类为空");
//		}
//		createIndex(t);
//		UpdateQuery updateQuery = new UpdateQueryBuilder()
//				.withId(String.valueOf(t.getId()))
//				.withClass(t.getClass())
//				.withIndexRequest(new IndexRequest().source(convertObjectToString(t)))
//				// 没有索引时做插入
//				.withDoUpsert(true)
//				.build();
//		String documentId = elasticsearchTemplate.update(updateQuery).getId();
//		elasticsearchTemplate.refresh(t.getClass());
//		return documentId;
//	}
//
//
//	/**
//	 * 批量保存或者修改索引
//	 * @Title: saveOrUpdateAllIndex
//	 * @param list 实体类集合
//	 * @return int 成功处理的条数
//	 */
//	public <T extends BaseDomain> int saveOrUpdateAllIndex(List<T> list) {
//		if (CollectionUtil.isEmpty(list)) {
//			throw new IllegalArgumentException("创建索引的实体类集合为空");
//		}
//		int count = list.size();
//		// Jdk1.8 集合Stream的批处理
//		CollectionUtil.batchConsume(ElasticsearchUtil::saveOrUpdateIndexs, list, BATCH_COUNT);
//		return count;
//	}
//
//	/**
//	 * 批量保存或者修改索引
//	 * @Title: saveOrUpdateAllIndex
//	 * @param list 实体类集合
//	 */
//	private static <T extends BaseDomain> void saveOrUpdateIndexs(List<T> list) {
//		List<UpdateQuery> updateQueryList = new ArrayList<>();
//		T tmp = null;
//		for (T t : list) {
//			if (null == t) {
//				continue;
//			}
//			if (null == tmp) {
//				tmp = t;
//				createIndex(tmp);
//			}
//			updateQueryList.add(new UpdateQueryBuilder()
//					.withId(String.valueOf(t.getId()))
//					.withClass(t.getClass())
//					.withIndexRequest(new IndexRequest().source(convertObjectToString(t)))
//					// 没有索引时做插入
//					.withDoUpsert(true)
//					.build());
//		}
//		if (updateQueryList.size() >= 1) {
//			elasticsearchTemplate.bulkUpdate(updateQueryList);
//			elasticsearchTemplate.refresh(tmp.getClass());
//		}
//	}
//
//	/**
//	 * 根据实体类删除该实体对应的所有索引
//	 * @Title: deleteAllIndex
//	 * @param t 实体类
//	 */
//	public <T extends BaseDomain> void deleteAllIndex(T t) {
//		if (null == t) {
//			throw new IllegalArgumentException("删除索引的实体类为空");
//		}
//		elasticsearchTemplate.deleteIndex(t.getClass());
//		elasticsearchTemplate.refresh(t.getClass());
//	}
//
//	/**
//	 * 根据Class删除所有的索引
//	 * @Title: deleteAllIndex
//	 * @param clasz 实体对应的class类
//	 */
//	public <T extends BaseDomain> void deleteAllIndex(Class<T> clasz) {
//		if (null == clasz) {
//			throw new IllegalArgumentException("删除索引的参数为空");
//		}
//		elasticsearchTemplate.deleteIndex(clasz);
//		elasticsearchTemplate.refresh(clasz);
//	}
//
//	/**
//	 * 根据实体类的id删除单个索引
//	 * @Title: deleteIndexById
//	 * @param t 实体类
//	 */
//	public <T extends BaseDomain> void deleteIndexById(T t) {
//		if (null == t) {
//			throw new IllegalArgumentException("删除索引的实体类为空");
//		}
//		elasticsearchTemplate.delete(t.getClass(), String.valueOf(t.getId()));
//		elasticsearchTemplate.refresh(t.getClass());
//	}
//
//	/**
//	 * 根据Id删除单个索引
//	 * @Title: deleteIndexById
//	 * @param clasz 实体对应的class
//	 * @param id 实体对应的id
//	 */
//	public <T extends BaseDomain> void deleteIndexById(Class<T> clasz, String id) {
//		if (null == clasz || StrUtil.isEmpty(id)) {
//			throw new IllegalArgumentException("删除索引的参数为空");
//		}
//		elasticsearchTemplate.delete(clasz, id);
//		elasticsearchTemplate.refresh(clasz);
//	}
//
//
//	/**
//	 * 根据Id集合批量删除索引
//	 * @Title: deleteIndexByIds
//	 * @param clasz 实体对应的class
//	 * @param ids 实体对应的id
//	 */
//	public <T extends BaseDomain> void deleteIndexByIds(Class<T> clasz, List<String> ids) {
//		if (null == clasz || CollectionUtil.isEmpty(ids)) {
//			throw new IllegalArgumentException("删除索引的参数为空");
//		}
//		CriteriaQuery criteriaQuery = new CriteriaQuery(new Criteria());
//		criteriaQuery.setIds(ids);
//		elasticsearchTemplate.delete(criteriaQuery, clasz);
//		elasticsearchTemplate.refresh(clasz);
//	}
//
//	/**
//	 * 无排序的分页查询
//	 * @Title: queryForPage
//	 * @param clasz 实体类对应的class
//	 * @return Page<T> 分页对象
//	 */
//	public <T extends BaseDomain> Page<T> queryForPage(Class<T> clasz) {
//		return queryForPage(clasz, 0, 0);
//	}
//
//	/**
//	 * 分页查询
//	 * @Title: queryForPage
//	 * @param clasz 实体类对应的class
//	 * @param page 当前页码
//	 * @param size 每页的条数
//	 * @return Page<T> 分页对象
//	 */
//	public <T extends BaseDomain> Page<T> queryForPage(Class<T> clasz, int page, int size) {
//		return queryForPage(clasz, page, size, null, null);
//	}
//
//	/**
//	 * 分页查询
//	 * @Title: queryForPage
//	 * @param clasz 实体类对应的class
//	 * @param sortField 排序字段
//	 * @param sortOrder 排序的方式
//	 * @return Page<T> 分页对象
//	 */
//	public <T extends BaseDomain> Page<T> queryForPage(Class<T> clasz, String sortField,
//													   String sortOrder) {
//		return queryForPage(clasz, 0, 0, sortField, sortOrder);
//	}
//
//	/**
//	 * 无排序的分页查询
//	 * @Title: queryForPage
//	 * @param clasz 实体类对应的class
//	 * @param keyWord 搜索的关键字
//	 * @param fields 搜索的字段数组
//	 * @return Page<T> 分页对象
//	 */
//	public <T extends BaseDomain> Page<T> queryWithKeyWordForPage(Class<T> clasz, String keyWord, String... fields) {
//		return queryWithKeyWordForPage(clasz, 0, 0, keyWord, fields);
//	}
//
//	/**
//	 * 分页查询
//	 * @Title: queryForPage
//	 * @param clasz 实体类对应的class
//	 * @param page 当前页码
//	 * @param size 每页的条数
//	 * @param sortField 排序字段
//	 * @param sortOrder 排序的方式
//	 * @return Page<T> 分页对象
//	 */
//	public <T extends BaseDomain> Page<T> queryForPage(Class<T> clasz, int page, int size, String sortField,
//													   String sortOrder) {
//		return queryForPage(clasz, page, size, sortField, sortOrder, null, (String[]) null);
//	}
//
//	/**
//	 * 无排序的分页查询
//	 * @Title: queryForPage
//	 * @param clasz 实体类对应的class
//	 * @param page 当前页码
//	 * @param size 每页的条数
//	 * @param keyWord 搜索的关键字
//	 * @param fields 搜索的字段数组
//	 * @return Page<T> 分页对象
//	 */
//	public <T extends BaseDomain> Page<T> queryWithKeyWordForPage(Class<T> clasz, int page, int size, String keyWord,
//																  String... fields) {
//		return queryForPage(clasz, page, size, null, null, keyWord, fields);
//	}
//
//	/**
//	 * 快速搜索的分页查询
//	 * @Title: queryForPage
//	 * @param clasz 实体类对应的class
//	 * @param page 当前页码
//	 * @param size 每页的条数
//	 * @param sortField 排序字段
//	 * @param sortOrder 排序的方式
//	 * @param keyWord 搜索的关键字
//	 * @param fields 搜索的字段数组
//	 * @return Page<T> 分页对象
//	 */
//	public <T extends BaseDomain> Page<T> queryForPage(Class<T> clasz, int page, int size, String sortField,
//													   String sortOrder, String keyWord, String... fields) {
//		if (null == clasz) {
//			throw new IllegalArgumentException("查询索引的参数为空");
//		}
//
//		Criteria criteria = new Criteria();
//
//		// 进行字段搜索的
//		if (StrUtil.isNotEmpty(keyWord) && ArrayUtil.isNotEmpty(fields)) {
//			for (String field : fields) {
//				criteria.or(new Criteria(field).contains(keyWord));
//			}
//		}
//		CriteriaQuery criteriaQuery = QueryBuildUtil.createCriteriaQuery(criteria, page, size, sortField, sortOrder);
//		return elasticsearchTemplate.queryForPage(criteriaQuery, clasz);
//	}
//
//	/**
//	 * 高级搜索的分页查询
//	 * @Title: queryForPage
//	 * @param clasz 实体类对应的class
//	 * @param paramMap 原始查询参数的map集合
//	 * @return Page<T> 分页对象
//	 */
//	public <T extends BaseDomain> Page<T> queryForPage(Class<T> clasz, Map<String, Object> paramMap) {
//		return queryForPage(clasz, 0, 0, paramMap);
//	}
//
//	/**
//	 * 高级搜索的分页查询
//	 * @Title: queryForPage
//	 * @param clasz 实体类对应的class
//	 * @param page 当前页码
//	 * @param size 每页的条数
//	 * @param paramMap 原始查询参数的map集合
//	 * @return Page<T> 分页对象
//	 */
//	public <T extends BaseDomain> Page<T> queryForPage(Class<T> clasz, int page, int size,
//													   Map<String, Object> paramMap) {
//		return queryForPage(clasz, page, size, null, null, paramMap);
//	}
//
//	/**
//	 * 高级搜索的分页查询
//	 * @Title: queryForPage
//	 * @param clasz 实体类对应的class
//	 * @param sortField 排序字段
//	 * @param sortOrder 排序的方式
//	 * @param paramMap 原始查询参数的map集合
//	 * @return Page<T> 分页对象
//	 */
//	public <T extends BaseDomain> Page<T> queryForPage(Class<T> clasz, String sortField,
//													   String sortOrder, Map<String, Object> paramMap) {
//		return queryForPage(clasz, 0, 0, sortField, sortOrder, paramMap);
//	}
//
//	/**
//	 * 高级搜索的分页查询
//	 * @Title: queryForPage
//	 * @param clasz 实体类对应的class
//	 * @param page 当前页码
//	 * @param size 每页的条数
//	 * @param sortField 排序字段
//	 * @param sortOrder 排序的方式
//	 * @param paramMap 原始查询参数的map集合
//	 * @return Page<T> 分页对象
//	 */
//	public <T extends BaseDomain> Page<T> queryForPage(Class<T> clasz, int page, int size, String sortField,
//													   String sortOrder, Map<String, Object> paramMap) {
//		if (null == clasz || MapUtil.isEmpty(paramMap)) {
//			throw new IllegalArgumentException("查询索引的参数为空");
//		}
//		SearchQuery searchQuery =
//				QueryBuildUtil.createSearchQuery(QueryBuildUtil.buildBoolQueryBuilder(new IdxCondition(paramMap)),
//						page, size, sortField, sortOrder);
//		return elasticsearchTemplate.queryForPage(searchQuery, clasz);
//	}
//
//	/**
//	 * 通过Sql语句去查询Es中的数据
//	 * @Title: queryBySql
//	 * @param sql 查询Es的sql语句
//	 * @return EsSearchResult Es查询后的结果
//	 */
//	public EsSearchResult queryBySql(String sql) {
//		if (StrUtil.isEmpty(sql)) {
//			throw new IllegalArgumentException("查询ES的sql语句为空");
//		}
//		long before = System.currentTimeMillis();
//		// 1、解释SQL
//		SearchDao searchDao = new SearchDao(elasticsearchTemplate.getClient());
//		QueryAction queryAction;
//		Object execution;
//		try {
//			queryAction = searchDao.explain(sql);
//			// 2、执行
//			execution = QueryActionElasticExecutor.executeAnyAction(searchDao.getClient(), queryAction);
//		} catch (RuntimeException e) {
//			logger.error("解析查询ES的sql语句报错：{}", StackTraceUtil.getExceptionMsg(e));
//			throw new RuntimeException("解析sql语句异常");
//		} catch (SQLFeatureNotSupportedException e) {
//			logger.error("ES目前只能执行SELECT、SHOW和DELETE三种语句：{}", StackTraceUtil.getExceptionMsg(e));
//			throw new RuntimeException("ES目前只能执行SELECT、SHOW和DELETE三种语句");
//		} catch (IOException e) {
//			logger.error("使用Sql查询Es出现IO异常：{}", StackTraceUtil.getExceptionMsg(e));
//			throw new RuntimeException("使用Sql查询Es出现IO异常");
//		}
//		// 3、格式化查询结果
//		ObjectResult result = getObjectResult(execution, true, false, false, true);
//
//		// 4、将ES查询到的结果转换成我们自己需要的结果并返回
//		EsSearchResult searchResult = new EsSearchResult();
//		fillEsSearchResult(searchResult, execution, result);
//		searchResult.setTime((System.currentTimeMillis() - before) / 1000);
//		return searchResult;
//	}
//
//	/**
//	 * 得到查询的结果
//	 * @Title: getObjectResult
//	 * @param execution ES中查询到的结果
//	 * @param flat 合并重复的字段
//	 * @param includeScore 结果集中是否包含score
//	 * @param includeType 结果集中是否包含type
//	 * @param includeId 结果集中是否包含ID
//	 * @return ObjectResult 对象结果
//	 */
//	private ObjectResult getObjectResult(Object execution, boolean flat, boolean includeScore, boolean includeType, boolean includeId) {
//		try {
//			return (new ObjectResultsExtractor(includeScore, includeType, includeId)).extractResults(execution, flat);
//		} catch (ObjectResultsExtractException e) {
//			throw new RuntimeException("将实体类转成字符串失败");
//		}
//	}
//
//	/**
//	 * 封装EsSearchResult的数据
//	 * @Title: fillEsSearchResult
//	 * @param searchResult 查询结果对象
//	 * @param execution es执行后格式化前的结果
//	 * @param result Es返回的格式化后的结果
//	 */
//	private void fillEsSearchResult(EsSearchResult searchResult, Object execution, ObjectResult result) {
//		searchResult.setResultColumns(Sets.newHashSet(result.getHeaders()));
//		List<IndexRowData> indexRowDatas = new ArrayList<>();
//		IndexRowData indexRowData;
//		for (List<Object> line : result.getLines()) {
//			indexRowData = new IndexRowData();
//			for (int i = 0; i < result.getHeaders().size(); i++) {
//				indexRowData.build(result.getHeaders().get(i), line.get(i));
//			}
//			indexRowDatas.add(indexRowData);
//		}
//		searchResult.setResultSize(indexRowDatas.size());
//		if (execution instanceof SearchHits) {
//			searchResult.setTotal(((SearchHits) execution).getTotalHits());
//		} else {
//			searchResult.setTotal(indexRowDatas.size());
//		}
//		searchResult.setResult(indexRowDatas);
//	}
//
//	/**
//	 * 总数计算
//	 * 		同义sql语句： select count(id) from XX
//	 * @Title: count
//	 * @param clasz 实体类对应的class
//	 * @param paramMap 原始数据集合
//	 * @return Long 结果数量
//	 */
//	public <T extends BaseDomain> Long count(Class<T> clasz, Map<String, Object> paramMap) {
//		return ObjectUtil.convertObjectToLong(aggregate(clasz, COUNT, new IdxCondition(paramMap)));
//	}
//
//	/**
//	 * 分组
//	 * 		同义sql语句：select count(id), groupKey[0], groupKey[1] from XX group by groupKey[0], groupKey[1]
//	 * @Title: group
//	 * @param clasz 实体类对应的class
//	 * @param paramMap 原始数据集合
//	 * @param groupKeys 分组字段（目前最多支持两个）
//	 * @return Map<String, AggregateGroupResult>
//	 *     一级key - 一级结果集
//	 *                  |
//	 *               二级key - 二级结果集
//	 */
//	public <T extends BaseDomain> Map<String, AggregateGroupResult> group(Class<T> clasz, Map<String, Object> paramMap,
//																		  String... groupKeys) {
//		return group(clasz, paramMap, false, groupKeys);
//	}
//
//	/**
//	 * 分组
//	 * 		同义sql语句：select count(id), groupKey[0], groupKey[1] from XX group by groupKey[0], groupKey[1]
//	 * @Title: group
//	 * @param clasz 实体类对应的class
//	 * @param paramMap 原始数据集合
//	 * @param asc 是否升序
//	 * @param groupKeys 分组字段（目前最多支持两个）
//	 * @return Map<String, AggregateGroupResult>
//	 *     一级key - 一级结果集
//	 *                  |
//	 *               二级key - 二级结果集
//	 */
//	public <T extends BaseDomain> Map<String, AggregateGroupResult> group(Class<T> clasz, Map<String, Object> paramMap, Boolean asc,
//																		  String... groupKeys) {
//		return convertObjectToMap(aggregate(clasz, COUNT, new IdxCondition(paramMap, asc == null ? false : asc, groupKeys)));
//	}
//
//	/**
//	 * 查询列表并获取顶部指定条数
//	 * 		同义sql语句：Select * from (select count(id), groupKey from XX group by groupKey order by count(id) asc) where rownum < topNum
//	 * @Title: top
//	 * @param clasz 实体类对应的class
//	 * @param paramMap 原始数据集合
//	 * @param groupKey 分组字段（目前最多支持两个）
//	 * @param asc 是否升序
//	 * @param topNum 取顶结果数量
//	 * @return Map<String, AggregateGroupResult>
//	 *         一级key - 一级结果集
//	 */
//	public <T extends BaseDomain> Map<String, AggregateGroupResult> top(Class<T> clasz, Map<String, Object> paramMap,
//																		String groupKey, Boolean asc, Integer topNum) {
//		return convertObjectToMap(aggregate(clasz, COUNT,
//				new IdxCondition(paramMap, topNum, asc, true, new String[]{groupKey})));
//	}
//
//	/**
//	 * 求和
//	 * 		同义sql语句：select sum(sumKey) from XX
//	 * @Title: sum
//	 * @param clasz 实体类对应的class
//	 * @param paramMap 原始数据集合
//	 * @param sumKey 求和字段
//	 * @return Double 记录之和
//	 */
//	public <T extends BaseDomain> Double sum(Class<T> clasz, Map<String, Object> paramMap, String sumKey) {
//		return ObjectUtil.convertObjectToDouble(aggregate(clasz, SUM, new IdxCondition(paramMap, sumKey)));
//	}
//
//	/**
//	 * 分组求和
//	 * 		同义sql语句：select sum(a), groupKeys[0], groupKeys[1] from XX group by groupKeys[0], groupKeys[1]
//	 * @Title: sumGroup
//	 * @param clasz 实体类对应的class
//	 * @param sumKey 求和字段
//	 * @param paramMap 原始数据集合
//	 * @param groupKeys 分组字段
//	 * @return Map<String, AggregateGroupResult>
//	 *     一级key - 一级结果集
//	 *                  |
//	 *               二级key - 二级结果集
//	 */
//	public <T extends BaseDomain> Map<String, AggregateGroupResult> sumGroup(Class<T> clasz, String sumKey,
//																			 Map<String, Object> paramMap, String... groupKeys) {
//		return convertObjectToMap(aggregate(clasz, SUM,
//				new IdxCondition(paramMap, sumKey, groupKeys)));
//	}
//
//	/**
//	 * 指定分组字段的词频的统计分析
//	 * @Title: word
//	 * @param clasz 实体类对应的class
//	 * @param paramMap 原始数据集合
//	 * @param wordKey 分组字段
//	 * @return Map<String, AggregateGroupResult>
//	 *     一级key - 一级结果集
//	 */
//	public <T extends BaseDomain> Map<String, AggregateGroupResult> word(Class<T> clasz, Map<String, Object> paramMap,
//																		 String wordKey) {
//		return word(clasz, paramMap, wordKey, WORD_RECORD_SIZE);
//	}
//
//	/**
//	 * 指定分组字段的词频的统计分析
//	 * @Title: word
//	 * @param clasz 实体类对应的class
//	 * @param paramMap 原始数据集合
//	 * @param wordKey 分组字段
//	 * @param wordNum 分词结果数量
//	 * @return Map<String, AggregateGroupResult>
//	 *     一级key - 一级结果集
//	 */
//	public <T extends BaseDomain> Map<String, AggregateGroupResult> word(Class<T> clasz, Map<String, Object> paramMap,
//																		 String wordKey, Integer wordNum) {
//		return convertObjectToMap(aggregate(clasz, WORD,
//				new IdxCondition(paramMap, wordKey, wordNum)));
//	}
//
//	/**
//	 * 去重计数（有误差）
//	 * 		同义sql语句：select count(distinct countDistinctKey) from XX
//	 * @Title: countDistinct
//	 * @param clasz 实体类对应的class
//	 * @param paramMap 原始数据集合
//	 * @param countDistinctKey 去重计数字段
//	 * @return Long 结果数量
//	 */
//	@Deprecated
//	public <T extends BaseDomain> Long countDistinct(Class<T> clasz, Map<String, Object> paramMap, String countDistinctKey) {
//		return ObjectUtil.convertObjectToLong(aggregate(clasz, COUNT_DISTINCT, new IdxCondition(paramMap,
//				countDistinctKey)));
//	}
//
//	/**
//	 * 去重计数（准确）
//	 * 		同义sql语句：select count(distinct countDistinctKey), groupKey from XX group by groupKey
//	 * @Title: countDistinct
//	 * @param clasz 实体类对应的class
//	 * @param paramMap 原始数据集合
//	 * @param countDistinctKey 去重计数字段
//	 * @param groupKey 分组字段
//	 * @return Long 结果数量
//	 */
//	public <T extends BaseDomain> Long countDistinct(Class<T> clasz, Map<String, Object> paramMap,
//													 String countDistinctKey,
//													 String groupKey) {
//		Map<String, AggregateGroupResult> result = countDistinctGroup(clasz, paramMap, countDistinctKey, groupKey);
//		return result.values().stream().mapToLong(aggregateGroupResult -> aggregateGroupResult == null ? 0L : aggregateGroupResult.getCount()).sum();
//	}
//
//	/**
//	 * 去重计数（结果最多只包含1000条，数据量少时可以使用）
//	 * 		同义sql语句：select count(distinct countDistinctKey), groupKeys[0], groupKeys[1] from XX group by groupKeys[0], groupKeys[1]
//	 * @Title: countDistinctGroup
//	 * @param clasz 实体类对应的class
//	 * @param paramMap 原始数据集合
//	 * @param countDistinctKey 去重计数字段
//	 * @param groupKeys 分组字段
//	 * @return Map<String, AggregateGroupResult>
//	 *     一级key - 一级结果集
//	 *                  |
//	 *               二级key - 二级结果集
//	 */
//	public <T extends BaseDomain> Map<String, AggregateGroupResult> countDistinctGroup(Class<T> clasz, Map<String,
//			Object> paramMap, String countDistinctKey, String... groupKeys) {
//		String[] actualGroupKeys = Arrays.copyOf(groupKeys, groupKeys.length + 1);
//		actualGroupKeys[groupKeys.length] = countDistinctKey;
//		Map<String, AggregateGroupResult> map = convertObjectToMap(aggregate(clasz, COUNT,
//				new IdxCondition(paramMap, actualGroupKeys)));
//		return convertKeyValueMapIntoKeySizeMap(map, 0, groupKeys.length);
//	}
//
//
//	/**
//	 * 结果转换
//	 * @Title: convertKeyValueMapIntoKeySizeMap
//	 * @param map 聚合统计结果的map对象
//	 * @param currentDepth 当前深度
//	 * @param totalDepth 总共的深度
//	 * @return Map<String, AggregateGroupResult>
//	 */
//	private Map<String, AggregateGroupResult> convertKeyValueMapIntoKeySizeMap(Map<String, AggregateGroupResult> map,
//																			   int currentDepth, int totalDepth) {
//		if (currentDepth == totalDepth) {
//			return null;
//		}
//		currentDepth++;
//		Map<String, AggregateGroupResult> keySizeMap = new HashMap<>(map.size());
//		for (Map.Entry<String, AggregateGroupResult> entry : map.entrySet()) {
//			keySizeMap.put(entry.getKey(), new AggregateGroupResult(entry.getValue().getSubResult() == null ? 0 :
//					entry.getValue().getSubResult().size(),
//					convertKeyValueMapIntoKeySizeMap(entry.getValue().getSubResult(), currentDepth, totalDepth)));
//		}
//		return keySizeMap;
//	}
//
//	/**
//	 * 将Object转成聚合统计结果的map对象
//	 * @Title: convertObjectToMap
//	 * @param result Object对象
//	 * @return Map<String, AggregateGroupResult> 聚合统计结果的map对象
//	 */
//	@SuppressWarnings("unchecked")
//	private Map<String, AggregateGroupResult> convertObjectToMap(Object result) {
//		if (result instanceof Map) {
//			return (Map<String, AggregateGroupResult>) result;
//		} else {
//			return new HashMap<>(0);
//		}
//	}
//
//
//	/**
//	 * 聚合统计的核心方法
//	 * @Title: aggregate
//	 * @param clasz 实体类对应的class
//	 * @param statisticType 统计类型
//	 * @param idxCondition 查询条件
//	 * @return Object 根据查询类型不同，返回值的结果也不同
//	 */
//	private <T extends BaseDomain> Object aggregate(Class<T> clasz, StatisticType statisticType,
//													IdxCondition idxCondition) {
//		validate(clasz, statisticType, idxCondition);
//		AggregatedPage<T> result;
//		try {
//			result = elasticsearchTemplate.queryForPage(QueryBuildUtil.buildAggregationQuery(statisticType,
//					QueryBuildUtil.buildBoolQueryBuilder(idxCondition),
//					idxCondition), clasz);
//		} catch (Exception e) {
//			logger.error("aggregate:{}", StackTraceUtil.getExceptionMsg(e));
//			throw new RuntimeException("ES查询失败", e);
//		}
//		try {
//			return convertResultByStatisticType(statisticType, idxCondition, result);
//		} catch (Exception e) {
//			logger.error("aggregate:{}", StackTraceUtil.getExceptionMsg(e));
//			throw new RuntimeException("ES查询结果转换失败", e);
//		}
//	}
//
//	/**
//	 * 聚合统计的参数校验
//	 * @Title: validate
//	 * @param clasz 实体类对应的class
//	 * @param statisticType 统计类型
//	 * @param idxCondition 查询条件
//	 */
//	private <T extends BaseDomain> void validate(Class<T> clasz, StatisticType statisticType,
//												 IdxCondition idxCondition) {
//		if (clasz == null) {
//			throw new RuntimeException("索引Class为空");
//		}
//		if (statisticType == null) {
//			throw new RuntimeException("统计类型为空");
//		}
//		if (idxCondition == null) {
//			throw new RuntimeException("查询条件为空");
//		}
//	}
//
//	/**
//	 * 根据统计类型来转换聚合统计的结果
//	 * @Title: convertResultByStatisticType
//	 * @param statisticType 统计类型
//	 * @param idxCondition 所以查询的条件
//	 * @param aggregatedPage 聚合统计查询到的结果
//	 */
//	private <T> Object convertResultByStatisticType(StatisticType statisticType, IdxCondition idxCondition,
//												AggregatedPage<T> aggregatedPage) {
//		if (StatisticType.WORD == statisticType || !ObjectUtils.isEmpty(idxCondition.getGroupKeys())) {
//			return convertCountGroupResult(aggregatedPage.getAggregations(), 0);
//		} else {
//			return getAggregationValue(aggregatedPage.getAggregations());
//		}
//	}
//
//	/**
//	 * 转换分组统计的结果
//	 * @Title: convertCountGroupResult
//	 * @param aggregations 集合到的结果
//	 * @param level 分组统计的层级【目前一般两级】
//	 * @return Map<AggregateGroupResult>
//	 */
//	private Map<String, AggregateGroupResult> convertCountGroupResult(Aggregations aggregations, int level) {
//		// 根据“聚合查询取的名”获取对应的聚合函数的聚合子类，该聚合子类也是个map集合,里面的value就是桶Bucket，我们要获得Bucket
//		Terms terms = aggregations.get(IdxCondition.GROUP_KEY_PREFIX + level);
//		if (terms == null || terms.getBuckets() == null || terms.getBuckets().isEmpty()) {
//			return new HashMap<>(0);
//		}
//		// 获取桶
//		List buckets = terms.getBuckets();
//		Map<String, AggregateGroupResult> result = new LinkedHashMap<>(buckets.size());
//		int nextLevel = level + 1;
//		Terms subTerms;
//		double value;
//		Terms.Bucket bucket;
//		// 循环桶
//		for (Object obj : buckets) {
//			// 内部条件桶
//			bucket = ((InternalTerms.Bucket) obj);
//			// 根据“聚合查询取的名”获取对应的聚合函数的聚合子类，该聚合子类也是个map集合,里面的value就是桶Bucket，我们要获得Bucket
//			subTerms = bucket.getAggregations().get(IdxCondition.GROUP_KEY_PREFIX + nextLevel);
//			if (subTerms == null || subTerms.getBuckets() == null) {
//				value = getAggregationValue(bucket.getAggregations()) == -1D ? bucket.getDocCount()
//						: getAggregationValue(bucket.getAggregations());
//				result.put(bucket.getKey().toString(), new AggregateGroupResult(value, null));
//			} else {
//				result.put(bucket.getKey().toString(),
//						new AggregateGroupResult(bucket.getDocCount(),
//								convertCountGroupResult(bucket.getAggregations(), nextLevel)));
//			}
//		}
//		return result;
//	}
//
//	/**
//	 * 获取到聚合的结果
//	 * @Title: getAggregationValue
//	 * @param aggregations 聚合
//	 * @return Double 聚合的结果
//	 */
//	private Double getAggregationValue(Aggregations aggregations) {
//		if (aggregations.get(IdxCondition.AGG_KEY) == null) {
//			return -1D;
//		} else {
//			return Double.valueOf(((InternalNumericMetricsAggregation.SingleValue) aggregations.get(IdxCondition.AGG_KEY)).getValueAsString());
//		}
//	}
//
//	/*
//	  一、ES中的条件拼接符号：
//	  	where
//	  	and
//	  	or
//	 二、字段查询的类型：
//	 	is =
//	 	not !=
//	 	contains
//	 	startsWith
//	 	endsWith
//	 	fuzzy
//	 	expression
//	 	boost
//	 	between
//	 	lessThan
//	 	lessThanEqual
//	 	greaterThan
//		greaterThanEqual
//		in
//		notIn
//		within
//		boundedBy
//	三、常用的sql条件类型
//		select * from users us where us.username = 'zhangxingwang';
//		select * from users us where us.username != 'zhangxingwang';
//		select * from users us where us.createDate >= dateFrom and createDate <= dataTo
//		select * from users us where us.orgcode like '.%'
//		select * from users us where us.orgcode like '%.1.%'
//		select * from users us where us.orgcode like '%.1.'
//
//	 */
//}
