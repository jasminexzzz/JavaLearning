package com.jasmine.feign.test;

import org.springframework.web.bind.annotation.*;

/**
 * @author wangyf
 */
@RestController
@RequestMapping("/feign/target")
public class TestTargetController {

    @GetMapping("/user")
    public User user(String name, Integer age) {
        User user = new User();
        user.setName(name + "_add_name");
        user.setAge(age + 100);
        return user;
    }


    @PostMapping("/user")
    public User addUser(@RequestBody User user) {
        user.setName("新增用户名:" + user.getName());
        return user;
    }

    @PostMapping("/user/param")
    public User addUser(@RequestParam String name, @RequestBody User user) {
        user.setName("新增用户名:" + user.getName() + " · " +  name);
        return user;
    }


}
