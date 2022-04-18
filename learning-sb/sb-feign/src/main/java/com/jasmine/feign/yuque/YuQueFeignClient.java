package com.jasmine.feign.yuque;

import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @author wangyf
 */

@Headers({Constant.CONTENT_TYPE,Constant.TOKEN})
public interface YuQueFeignClient {

    @RequestLine("GET /api/v2/user")
    YuqueUser user();

    /**
     * 获取文档集合
     *
     * @param id 1:10858843
     *           2:11236337
     * @return 文档集合
     */
    @RequestLine("GET api/v2/repos/{id}/docs")
    YuqueDocs docs(@Param("id") Integer id);
}

class Constant {
    public static final String CONTENT_TYPE = "Content-Type:application/json";
    public static final String TOKEN = "X-Auth-Token:OEwlQNA5gmWfzz3I28KHYU0N8OBcpIVWYEbiEf0C";
}