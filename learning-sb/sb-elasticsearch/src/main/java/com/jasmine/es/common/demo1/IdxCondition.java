package com.xzzz.es.common.demo1;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: IdxCondition
 * @Description: 索引条件【查询的时候需要将原始的数据封装成IdxCondition才能使用】
 * @author: zhangxw
 * @date: 2019/5/29
 */
public class IdxCondition {

    /** 范围开始字段后缀 */
    public static final String RANGE_FROM_SUFFIX = "From";
    /** 范围结束字段后缀 */
    public static final String RANGE_TO_SUFFIX = "To";
    /** 聚合操作key */
    public static final String AGG_KEY = "agg";
    /** 聚合分组操作key前缀 */
    public static final String GROUP_KEY_PREFIX = "group_";
    /** 用于分组的id字段 */
    public static final String UID = "_uid";

    /** 原始条件集合【所有的参数】 */
    private Map<String, Object> paramMap;
    /** 需要使用or条件拼接的字段 */
    private List<String> orKeys;
    /** 需要使用or条件内部的and条件拼接的字段 */
    private List<String> orAndKeys;
    /** 日期条件keys（需要使用日期）【需要把日期字段的key存放于此】 */
    private List<String> rangeKeys;
    /** 需要使用通配符 条件keys（需要使用wildcard语法查询）【需要模糊查询的字段的key存放于此】 */
    private List<String> wildcardKeys;
    /** 需要使用前缀匹配查询的条件组 */
    private List<String> prefixKeys;
    /** key中的值不为null【key is not null】 */
    private List<String> notNullKeys;
    /** key中的值需要分词之后进行匹配的 */
    private List<String> matchKeys;
    /** 统计字段【count、sum的字段。参照IdxOperation类】 */
    private String aggKey;
    /** 分组字段【需要分组的字段的key存放于此】 */
    private List<String> groupKeys;
    /** 结果数量【默认展示多少条数据】 */
    private int recordNum = 1000;
    /** 是否升序（默认升序） */
    private boolean asc = true;
    /** 是否使用聚合结果排序 */
    private boolean aggOrder = false;
    /** 查询脚本【"doc['cDate'].value < doc['uDate'].value"】 */
    private String script;

    public IdxCondition() {
    }

    /**
     * 原始数据构造器，所有的数据全部是用and拼接的termQuery查询
     * @Title: IdxCondition
     * @param paramMap 原始数据
     */
    public IdxCondition(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    /**
     * 原始数据和分组数据构造器，所有的数据全部是用and拼接的termQuery查询，并进行分组
     * @Title: IdxCondition
     * @param paramMap 原始数据
     * @param groupKeys 分组的key
     */
    public IdxCondition(Map<String, Object> paramMap, String... groupKeys) {
        this(paramMap);
        this.groupKeys = groupKeys == null ? new ArrayList<>(0) : Arrays.stream(groupKeys).distinct().collect(Collectors.toList());
    }

    /**
     * 原始数据和聚合字段的构造器
     * @Title: IdxCondition
     * @param paramMap 原始数据
     * @param aggKey 聚合的字段
     */
    public IdxCondition(Map<String, Object> paramMap, String aggKey) {
        this(paramMap);
        this.aggKey = aggKey;
    }

    /**
     * 原始数据、聚合字段、分组字段的构造器
     * @Title: IdxCondition
     * @param paramMap 原始数据
     * @param aggKey 聚合的字段
     * @param groupKeys 分组字段
     */
    public IdxCondition(Map<String, Object> paramMap, String aggKey, String[] groupKeys) {
        this(paramMap, groupKeys);
        this.aggKey = aggKey;
    }

    /**
     * 原始数据、是否排序、分组字段的构造器
     * @Title: IdxCondition
     * @param paramMap 原始数据
     * @param asc 是否升序
     * @param groupKeys 分组字段
     */
    public IdxCondition(Map<String, Object> paramMap, boolean asc, String[] groupKeys) {
        this(paramMap, groupKeys);
        this.asc = asc;
    }

    /**
     * 原始数据、聚合字段、返回记录条数的构造器
     * @Title: IdxCondition
     * @param paramMap 原始数据
     * @param aggKey 聚合字段
     * @param recordNum 返回记录的条数
     */
    public IdxCondition(Map<String, Object> paramMap, String aggKey, int recordNum) {
        this(paramMap, aggKey);
        this.recordNum = recordNum;
    }

    /**
     * 原始数据、返回记录的条数、是否排序、是否使用聚合结果排序、分组字段的构造器
     * @Title: IdxCondition
     * @param paramMap 原始数据
     * @param recordNum 返回记录的条数
     * @param asc 是否升序
     * @param aggOrder 是否使用聚合结果排序
     * @param groupKeys 分组字段
     */
    public IdxCondition(Map<String, Object> paramMap, int recordNum, boolean asc, boolean aggOrder, String[] groupKeys) {
        this(paramMap, groupKeys);
        this.recordNum = recordNum;
        this.asc = asc;
        this.aggOrder = aggOrder;
    }

    /**
     * 构造器
     * @Title: IdxCondition
     * @param paramMap 原始数据
     * @param recordNum 返回记录的条数
     * @param asc 是否升序
     * @param aggOrder 是否使用聚合结果排序
     * @param aggKey 聚合字段
     * @param groupKeys 分组字段
     */
    public IdxCondition(Map<String, Object> paramMap, int recordNum, boolean asc, boolean aggOrder, String aggKey, String[] groupKeys) {
        this(paramMap, groupKeys);
        this.recordNum = recordNum;
        this.asc = asc;
        this.aggOrder = aggOrder;
        this.aggKey = aggKey;
    }

    /**
     * 构造器
     * @Title: IdxCondition
     * @param paramMap 原始数据
     * @param rangeKeys 范围查询字段
     * @param wildcardKeys 通配符查询的字段【like '%zhang%'】
     */
    public IdxCondition(Map<String, Object> paramMap, String[] rangeKeys, String[] wildcardKeys) {
        this.paramMap = paramMap;
        this.rangeKeys = rangeKeys == null ? new ArrayList<>(0) : Arrays.stream(rangeKeys).distinct().collect(Collectors.toList());
        this.wildcardKeys = wildcardKeys == null ? new ArrayList<>(0) : Arrays.stream(wildcardKeys).distinct().collect(Collectors.toList());
    }

    /**
     * 构造器
     * @Title: IdxCondition
     * @param paramMap 原始数据
     * @param rangeKeys 范围查询字段
     * @param wildcardKeys 通配符查询的字段【like '%zhang%'】
     * @param groupKeys 分组字段
     */
    public IdxCondition(Map<String, Object> paramMap, String[] rangeKeys, String[] wildcardKeys, String[] groupKeys) {
        this.paramMap = paramMap;
        this.rangeKeys = rangeKeys == null ? new ArrayList<>(0) : Arrays.stream(rangeKeys).distinct().collect(Collectors.toList());
        this.wildcardKeys = wildcardKeys == null ? new ArrayList<>(0) : Arrays.stream(wildcardKeys).distinct().collect(Collectors.toList());
        this.groupKeys = groupKeys == null ? new ArrayList<>(0) : Arrays.stream(groupKeys).distinct().collect(Collectors.toList());
    }

    /**
     * 构造器
     * @Title: IdxCondition
     * @param paramMap 原始数据
     * @param rangeKeys 范围查询字段
     * @param wildcardKeys 通配符查询的字段【like '%zhang%'】
     * @param aggKey 聚合字段
     * @param groupKeys 分组字段
     * @param script 脚本
     */
    public IdxCondition(Map<String, Object> paramMap, String[] rangeKeys, String[] wildcardKeys, String aggKey, String[] groupKeys, String script) {
        this.paramMap = paramMap;
        this.rangeKeys = rangeKeys == null ? new ArrayList<>(0) : Arrays.stream(rangeKeys).distinct().collect(Collectors.toList());
        this.wildcardKeys = wildcardKeys == null ? new ArrayList<>(0) : Arrays.stream(wildcardKeys).distinct().collect(Collectors.toList());
        this.groupKeys = groupKeys == null ? new ArrayList<>(0) : Arrays.stream(groupKeys).distinct().collect(Collectors.toList());
        this.script = script;
        this.aggKey = aggKey;
    }

    /**
     * 构造器
     * @Title: IdxCondition
     * @param paramMap 原始数据
     * @param rangeKeys 范围查询字段
     * @param wildcardKeys 通配符查询的字段【like '%zhang%'】
     * @param aggKey 聚合字段
     * @param script 脚本
     */
    public IdxCondition(Map<String, Object> paramMap, String[] rangeKeys, String[] wildcardKeys, String aggKey, String script) {
        this(paramMap, rangeKeys, wildcardKeys);
        this.aggKey = aggKey;
        this.script = script;
    }

    /**
     * 构造器
     * @Title: IdxCondition
     * @param paramMap 原始数据
     * @param orKeys 使用or拼接的字段
     * @param orAndKeys 大的or内部有多个and条件的字段
     * @param rangeKeys 范围查询字段
     * @param wildcardKeys 通配符查询的字段【like '%zhang%'】
     * @param prefixKeys 前缀匹配查询的字段【like 'zhang%'】
     * @param notNullKeys 不为null的字段查询【name is not null】
     * @param matchKeys 分词的匹配查询
     * @param aggKey 聚合字段
     * @param groupKeys 分组字段
     * @param script 脚本
     */
    public IdxCondition(Map<String, Object> paramMap, String[] orKeys, String[] orAndKeys, String[] rangeKeys,
                        String[] wildcardKeys,
                        String[] prefixKeys, String[] notNullKeys, String[] matchKeys, String aggKey,
                        String[] groupKeys,
                        String script) {
        this.paramMap = paramMap;
        this.orKeys = orKeys == null ? new ArrayList<>(0) : Arrays.stream(orKeys).distinct().collect(Collectors.toList());
        this.orAndKeys = orAndKeys == null ? new ArrayList<>(0) : Arrays.stream(orAndKeys).distinct().collect(Collectors.toList());
        this.rangeKeys = rangeKeys == null ? new ArrayList<>(0) : Arrays.stream(rangeKeys).distinct().collect(Collectors.toList());
        this.wildcardKeys = wildcardKeys == null ? new ArrayList<>(0) : Arrays.stream(wildcardKeys).distinct().collect(Collectors.toList());
        this.prefixKeys = prefixKeys == null ? new ArrayList<>(0) : Arrays.stream(prefixKeys).distinct().collect(Collectors.toList());
        this.notNullKeys = notNullKeys == null ? new ArrayList<>(0) : Arrays.stream(notNullKeys).distinct().collect(Collectors.toList());
        this.matchKeys = matchKeys == null ? new ArrayList<>(0) : Arrays.stream(matchKeys).distinct().collect(Collectors.toList());
        this.aggKey = aggKey;
        this.groupKeys = groupKeys == null ? new ArrayList<>(0) : Arrays.stream(groupKeys).distinct().collect(Collectors.toList());
        this.script = script;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public List<String> getOrKeys() {
        return orKeys;
    }

    public List<String> getOrAndKeys() {
        return orAndKeys;
    }

    public List<String> getRangeKeys() {
        return rangeKeys;
    }

    public List<String> getWildcardKeys() {
        return wildcardKeys;
    }

    public List<String> getPrefixKeys() {
        return prefixKeys;
    }

    public List<String> getNotNullKeys() {
        return notNullKeys;
    }

    public List<String> getMatchKeys() {
        return matchKeys;
    }

    public String getAggKey() {
        return aggKey;
    }

    public List<String> getGroupKeys() {
        return groupKeys;
    }

    public int getRecordNum() {
        return recordNum;
    }

    public boolean isAsc() {
        return asc;
    }

    public boolean isAggOrder() {
        return aggOrder;
    }

    public String getScript() {
        return script;
    }
}