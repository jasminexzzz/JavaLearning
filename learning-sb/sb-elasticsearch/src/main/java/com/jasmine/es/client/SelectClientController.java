package com.jasmine.es.client;

import com.jasmine.es.client.dto.ItemDTO;
import com.jasmine.es.client.manager.EsCurdManager;
import com.jasmine.es.client.manager.EsSearchManager;
import com.jasmine.es.client.dto.EsInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author wangyf
 * @since 0.0.1
 */
@Slf4j
@RestController
@RequestMapping("/es/client")
public class SelectClientController {

    @Autowired
    private EsCurdManager manager;

    @GetMapping("/info")
    public EsInfo info () {
        return manager.getInfo();
    }

    /**
     * 根据
     *
     * @param index index
     * @param id id
     * @return 返回数据
     */
    @GetMapping("/get")
    public ItemDTO getDoc (String index, String id) {
        return manager.get(index, id, ItemDTO.class);
    }


    /**
     * 查询条数
     * @param index index
     * @return 条数
     */
    @GetMapping("/count")
    public long getCount(String index) {
        return manager.count(index);
    }


    /**
     * 查询是否存在
     * @param index index
     * @param id id
     * @return true/false
     */
    @GetMapping("/exists")
    public boolean exists (String index,String id) {
        return manager.exists(index, id);
    }

}
