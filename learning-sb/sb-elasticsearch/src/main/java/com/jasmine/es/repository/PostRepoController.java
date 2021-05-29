package com.jasmine.es.repository;

import com.jasmine.es.repository.dto.UserDoc;
import com.jasmine.es.repository.service.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author wangyf
 * @since 0.0.1
 */
@RestController
@RequestMapping("/es/repo")
public class PostRepoController {

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/add/doc")
    public void addDoc (@RequestBody UserDoc user) {
        userMapper.save(user);
    }
}
