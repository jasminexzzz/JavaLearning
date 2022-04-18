package com.jasmine.feign.yuque;

import lombok.Data;

/**
 * @author wangyf
 */
@Data
public class YuqueUser {

    private Data data;

    @lombok.Data
    private static class Data {
        private Integer id;
        private String type;
        private String space_id;
        private String account_id;
        private String login;
        private String name;
        private String avatar_url;
        private Integer books_count;
        private Integer public_books_count;
        private String description;
        private String created_at;
        private String updated_at;
    }

}
