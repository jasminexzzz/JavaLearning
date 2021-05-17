package com.jasmine.es.client;

import com.jasmine.es.client.dto.UserDTO;
import com.jasmine.es.client.manager.EsHighLevelClientManager;
import com.jasmine.es.client.manager.EsInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author wangyf
 * @since 0.0.1
 */
@Slf4j
@RestController
@RequestMapping("/es/client")
public class GetClientController {

    @Autowired
    private EsHighLevelClientManager manager;

    @GetMapping("/info")
    public EsInfo info () {
        return manager.getInfo();
    }


    @GetMapping("test")
    public void test () {
        manager.getHighClient();
    }


    /**
     * 根据
     * @param index index
     * @param id id
     * @return 返回数据
     */
    @GetMapping("/get")
    public UserDTO getDoc (String index, String id) {
        return manager.get(index, id, UserDTO.class);
    }


    /**
     * 查询是否存在
     * @param index index
     * @param id id
     * @return true/false
     */
    @GetMapping("/exists")
    public boolean exists (String index,String id) {
        try {
            return manager.exists(index, id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 搜索
     */
    @GetMapping("/search")
    public List<UserDTO> search (String index, String field, String value) {
        return manager.search(index,field,value,UserDTO.class);
    }

}
