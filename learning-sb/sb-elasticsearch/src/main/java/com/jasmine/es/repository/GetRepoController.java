package com.jasmine.es.repository;

import com.jasmine.es.repository.dto.UserDoc;
import com.jasmine.es.repository.service.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author wangyf
 * @since 0.0.1
 */
@RestController
@RequestMapping("/es/repo")
public class GetRepoController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 查询全部
     * @return 响应
     */
    @GetMapping("/all")
    public Iterable<UserDoc> getAll () {
        return userMapper.findAll();
    }

    /**
     * 根据ID查询
     * @param id 用户id
     * @return 查询结果
     */
    @GetMapping("/doc/id")
    public Optional<UserDoc> docById (String id) {
        return userMapper.findById(id);
    }

    /**
     * 根据ID查询
     * @param id 用户id
     * @return 查询结果
     */
    @GetMapping("/doc/multi")
    public Page<UserDoc> docByMulti (String id) {
        UserDoc user = new UserDoc();
        user.setUserId(id);
        String[] fields = new String[]{"userId"};
        // 执行查询，获取分页结果集
        Page<UserDoc> userPage = this.userMapper.searchSimilar(user,fields,PageRequest.of(1,1, Sort.by("userId")));
        return userPage;
    }
}
