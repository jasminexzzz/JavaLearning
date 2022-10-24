package com.xzzz.es.common.demo1;

import java.util.Map;

/**
 * @ClassName: AggregateGroupResult
 * @Description: elasticsearch分组查询结果集
 * @author: gust
 * @date: 2017/12/14
 */
public class AggregateGroupResult {

	private double aggValue;
	private Map<String, AggregateGroupResult> subResult;

	public AggregateGroupResult(double aggValue) {
		this.aggValue = aggValue;
	}

	public AggregateGroupResult(double aggValue, Map<String, AggregateGroupResult> subResult) {
		this.aggValue = aggValue;
		this.subResult = subResult;
	}

	public double getAggValue() {
		return aggValue;
	}

	public void setAggValue(double aggValue) {
		this.aggValue = aggValue;
	}

	public Map<String, AggregateGroupResult> getSubResult() {
		return subResult;
	}

	public void setSubResult(Map<String, AggregateGroupResult> subResult) {
		this.subResult = subResult;
	}

	public long getCount() {
		return Double.valueOf(aggValue).longValue();
	}
}
