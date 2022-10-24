package com.xzzz.feign.yuque;

import lombok.Data;

import java.util.List;

/**
 * @author wangyf
 */
@Data
public class YuqueDocs {

    private List<Data> data;

    @lombok.Data
    private static class Data {
        private String title;
        private String description;
        private String content_updated_at;
    }
}
