package com.jasmine.sbspringboot.context.scan;

import java.lang.annotation.*;
import java.lang.annotation.Target;

/**
 * @author wangyf
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface TargetAnnotation {
}
