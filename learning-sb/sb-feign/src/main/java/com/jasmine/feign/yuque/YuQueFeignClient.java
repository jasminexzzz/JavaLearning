package com.jasmine.feign.yuque;

import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 *
 * {@link feign.Headers}      : 设置请求头
 * {@link feign.RequestLine}  : 表明了请求方式和路径
 * {@link feign.Param}        : 在请求路径中映射参数
 * {@link feign.Body}         : 设置请求体
 *
 *
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

    /**
     * 一个表明用法的方法, 无法请求成功的
     */
    @RequestLine("POST xxx_uri")
    @Body("{\"age\":1}")
    void testPost1();

    /**
     * 也可以直接映射
     */
    @RequestLine("POST xxx_uri")
    @Body("{body}")
    void testPost1(@Param("body") String body);
}

class Constant {
    public static final String CONTENT_TYPE = "Content-Type:application/json";
    public static final String TOKEN = "X-Auth-Token:OEwlQNA5gmWfzz3I28KHYU0N8OBcpIVWYEbiEf0C";
}