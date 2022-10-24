package com.xzzz.sbspringbootmodule.autoconfig.impot.pattern1;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : jasmineXz
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({ImportTarget1.class,ImportTarget2.class})
@interface EnabledConfig {

}
