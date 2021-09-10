package com.jasmine.es.client.anotations;

import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.*;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Inherited
public @interface Field {

	/**
	 * 字段名
	 * @return
	 */
	String name() default "";

	/**
	 * 字段类型
	 * @return
	 */
	FieldType type() default FieldType.Auto;

	/**
	 * 日期格式化
	 * @return
	 */
	DateFormat format() default DateFormat.none;

	/**
	 * 搜索分词
	 * @return
	 */
	String searchAnalyzer() default "";

	/**
	 * 索引分词
	 * @return
	 */
	String analyzer() default "";

	/**
	 * 规范化
	 * @return
	 */
	String normalizer() default "";

	/**
	 * null值填充
	 * @return
	 */
	NullValueType nullValueType() default NullValueType.String;
}
