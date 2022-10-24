package com.xzzz.es.client.biz;

import com.xzzz.common.core.dto.R;
import com.xzzz.es.client.biz.dto.ItemDTO;
import com.xzzz.es.client.dto.EsInfoDTO;
import com.xzzz.es.client.manager.EsCurdManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 查询ES信息
     * @return ES信息
     */
    @GetMapping("/info")
    public EsInfoDTO info() {
        return manager.getInfo();
    }

    /**
     * 查询全部索引
     * @return 索引数组
     */
    @GetMapping("/_alias")
    public R getAllIndex () {
        return R.ok(manager.getAliases());
    }

    /**
     * 查询索引的Mapping
     * @param index 索引名称
     * @return 映射信息
     */
    @GetMapping("/_mapping")
    public R _mapping (String index) {
        return R.ok(manager.getMappings(index));
    }

    /**
     * 根据
     *
     * @param index index
     * @param id id
     * @return 返回数据
     */
    @GetMapping("/get")
    public ItemDTO getDoc(String index, String id) {
        return manager.get(index, id, ItemDTO.class);
    }

    /**
     * 查询条数
     *
     * @param index index
     * @return 条数
     */
    @GetMapping("/count")
    public long getCount(String index) {
        return manager.count(index);
    }

    /**
     * 查询是否存在
     *
     * @param index index
     * @param id    id
     * @return true/false
     */
    @GetMapping("/exists")
    public boolean exists(String index, String id) {
        return manager.exists(index, id);
    }



    @GetMapping("/test")
    public void test() {
    }


}
