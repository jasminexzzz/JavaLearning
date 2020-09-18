package com.jasmine.learingsb.config.condition;

import org.springframework.boot.autoconfigure.condition.ConditionalOnJava;
import org.springframework.boot.system.JavaVersion;

/**
 * @author : jasmineXz
 */
@ConditionalOnJava(JavaVersion.EIGHT)
public class ConditionalOnJavaTest {
}
