package com.xzzz.sbspringbootmodule.autoconfig.impot.pattern3;

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
@Import({ImportTargetRegistrar.class})
@interface EnabledConfig {

}
