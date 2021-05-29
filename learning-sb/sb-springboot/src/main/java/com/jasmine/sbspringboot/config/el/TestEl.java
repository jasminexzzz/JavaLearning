package com.jasmine.sbspringboot.config.el;

import org.springframework.stereotype.Component;

/**
 * @author wangyf
 * @since 0.0.1
 */
@Component
public class TestEl {

    @CustomElAnnotation(attachmentId = "123123")
    public void testMethod() {

    }
}
