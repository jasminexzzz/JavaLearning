package com.xzzz.sbspringbootmodule.autoconfig.impot.pattern2;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author : jasmineXz
 */
class ImportTargetSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[] {"com.jasmine.moduledemo.autoconfig.impot.pattern2.ImportTarget1"};
    }
}
