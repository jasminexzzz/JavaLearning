package com.xzzz.feign.test;

import feign.*;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * @author wangyf
 */
@Headers({"Content-Type:application/json"})
public interface TestFeignClient {

    // GET 方式

    /**
     * <p>如果服务端使用如下方式接收参数
     * <pre>
     * {@code @GetMapping("/user")
     *        public User user(String name, Integer age) {}}
     * </pre>
     * 可以在 {@link RequestLine} 设置占位符, 然后通过 {@link Param} 注解映射到占位符中
     *
     *
     * @param name 参数1
     * @param age 参数2
     * @return 响应
     */
    @RequestLine("GET feign/target/user?name={name}&age={age}")
    User get1(@Param("name") String name, @Param("age") Integer age);

    /**
     * <p>如果服务端直接使用参数接口, 也可以通过 {@link QueryMap} 传参:
     * <pre>
     * {@code @GetMapping("/user")
     *        public User user(String name, Integer age) {}}
     *  </pre>
     * <p>如果服务端直接使用参数, 或使用 {@link ModelAttribute} 方式接收了一个对象, 也可以使用 {@link QueryMap} 的方式传参
     * <p>如服务端如下:
     * <pre>
     * {@code @GetMapping("/user")
     *        public User user(@ModelAttribute User user){}}
     * </pre>
     *
     *
     * @param user 请求参数
     * @return 响应
     */
    @RequestLine("GET feign/target/user")
    User get2(@QueryMap User user);



    // POST 方式

    /**
     * 通过POST的方式请求, 那么参数默认就是请求体, 注意需要设置请求头: {@link Headers}
     * <pre>
     * {@code @Headers({"Content-Type:application/json"}) }
     * </pre>
     *
     * @param user 请求体
     * @return 响应
     */
    @RequestLine("POST feign/target/user")
    User post1(User user);

    /**
     * 如果既需要传入参数, 又需要传入请求体
     *
     * @param name 参数
     * @param user 请求体
     * @return 响应
     */
    @RequestLine("POST feign/target/user/param?name={name}")
    User post2(@Param("name") String name, User user);

}
