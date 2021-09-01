package com.jasmine.es.client.anotations;

import org.springframework.core.annotation.AliasFor;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.*;

import java.lang.annotation.*;

/**
 * @author Rizwan Idrees
 * @author Mohsin Husen
 * @author Artur Konczak
 * @author Jonathan Yan
 * @author Jakub Vavrik
 * @author Kevin Leturc
 * @author Peter-Josef Meisch
 * @author Xiao Yu
 * @author Aleksei Arsenev
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Inherited
public @interface Field {

	/**
	 * Alias for {@link #name}.
	 *
	 * @since 3.2
	 */
	@AliasFor("name")
	String value() default "";

	/**
	 * The <em>name</em> to be used to store the field inside the document.
	 * <p>
	 * √5 If not set, the name of the annotated property is used.
	 *
	 * @since 3.2
	 */
	@AliasFor("value")
	String name() default "";

	FieldType type() default FieldType.Auto;

	boolean index() default true;

	DateFormat format() default DateFormat.none;

	String pattern() default "";

	boolean store() default false;

	boolean fielddata() default false;

	String searchAnalyzer() default "";

	String analyzer() default "";

	String normalizer() default "";

	String[] ignoreFields() default {};

	boolean includeInParent() default false;

	String[] copyTo() default {};

	/**
	 * @since 4.0
	 */
	int ignoreAbove() default -1;

	/**
	 * @since 4.0
	 */
	boolean coerce() default true;

	/**
	 * @since 4.0
	 */
	boolean docValues() default true;

	/**
	 * @since 4.0
	 */
	boolean ignoreMalformed() default false;

	/**
	 * @since 4.0
	 */
	IndexOptions indexOptions() default IndexOptions.none;

	/**
	 * @since 4.0
	 */
	boolean indexPhrases() default false;

	/**
	 * implemented as array to enable the empty default value
	 *
	 * @since 4.0
	 */
	IndexPrefixes[] indexPrefixes() default {};

	/**
	 * @since 4.0
	 */
	boolean norms() default true;

	/**
	 * @since 4.0
	 */
	String nullValue() default "";

	/**
	 * @since 4.0
	 */
	int positionIncrementGap() default -1;

	/**
	 * @since 4.0
	 */
	Similarity similarity() default Similarity.Default;

	/**
	 * @since 4.0
	 */
	TermVector termVector() default TermVector.none;

	/**
	 * @since 4.0
	 */
	double scalingFactor() default 1;

	/**
	 * @since 4.0
	 */
	int maxShingleSize() default -1;

	/**
	 * if true, the field will be stored in Elasticsearch even if it has a null value
	 *
	 * @since 4.1
	 */
	boolean storeNullValue() default false;

	/**
	 * to be used in combination with {@link FieldType#Rank_Feature}
	 * 
	 * @since 4.1
	 */
	boolean positiveScoreImpact() default true;

	/**
	 * to be used in combination with {@link FieldType#Object}
	 *
	 * @since 4.1
	 */
	boolean enabled() default true;

	/**
	 * @since 4.1
	 */
	boolean eagerGlobalOrdinals() default false;

	/**
	 * @since 4.1
	 */
	NullValueType nullValueType() default NullValueType.String;
}
