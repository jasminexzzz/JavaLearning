package com.jasmine.moduledemo.autoconfig.impot.pattern1;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author : jasmineXz
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({ImportTarget1.class,ImportTarget2.class})
@interface EnabledConfig {

}
