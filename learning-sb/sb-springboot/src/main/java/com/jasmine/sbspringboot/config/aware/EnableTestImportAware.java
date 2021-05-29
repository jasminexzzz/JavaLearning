package com.jasmine.sbspringboot.config.aware;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author : jasmineXz
 */
@Retention(RetentionPolicy.RUNTIME)
@Import(TestImportAware.class)
public @interface EnableTestImportAware {

    String value() default "默认";

}
