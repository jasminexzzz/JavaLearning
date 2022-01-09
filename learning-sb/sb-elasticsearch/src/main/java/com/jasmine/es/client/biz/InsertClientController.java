package com.jasmine.es.client.biz;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.jasmine.common.core.util.json.JsonUtil;
import com.jasmine.es.client.biz.dto.ItemDTO;
import com.jasmine.es.client.manager.EsCurdManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * @author wangyf
 * @since 0.0.1
 */

@Slf4j
@RestController
@RequestMapping("/es/client/add")
public class InsertClientController {

    @Autowired
    private EsCurdManager manager;

    @PostMapping
    public void addDoc (@RequestBody ItemDTO item) {
        manager.add(item,false);
    }

    /**
     * 批量新增
     */
    @PostMapping("/batch")
    public void addBatch (@RequestBody List<ItemDTO> users) {
        manager.addBatch(users,false);
    }


    private static final String FILE_NAME = "商品数据.json";

    /**
     * SELECT
     * 	a.id,
     * 	a.item_name,
     * 	a.sub_title,
     * 	a.retail_price,
     * 	a.supplier_id,
     * 	a.supplier_name,
     * 	a.brand_name,
     * 	date_format(a.gmt_created,'%Y-%m-%d %H:%i:%S') as gmt_created,
     * 	a.verify_status,
     * 	a.sales_num
     * FROM
     * 	item a;
     */
    @PostMapping("/file")
    public void addByFile() {

        File file = FileUtil.file(FILE_NAME);
        String fileContent = FileUtil.readString(file, "UTF-8");
        if (StrUtil.isBlank(fileContent)) {
            throw new IllegalArgumentException("未获取到文件内容, 请检查文件");
        }

        List<LinkedHashMap> list = JsonUtil.toObj(fileContent, List.class);
        List<ItemDTO> l = new ArrayList<>();
        list.forEach(m -> {
            ItemDTO item = new ItemDTO();
            item.setEsIndex      ("index_item");
            item.setEsId         (String.valueOf(m.get("id")));
            item.setId           (Long.parseLong(String.valueOf(m.get("id"))));
            item.setItemName     (String.valueOf(m.get("item_name")));
            item.setSubTitle     (String.valueOf(m.get("sub_title")));
            item.setRetailPrice  (Long.parseLong(String.valueOf( m.get("retail_price"))));
            item.setSupplierId   (Long.parseLong(String.valueOf( m.get("supplier_id"))));
            item.setSupplierName (String.valueOf( m.get("supplier_name")));
            item.setBrandName    (String.valueOf( m.get("brand_name")));
            item.setVerifyStatus (Integer.parseInt(String.valueOf( m.get("verify_status"))));
            item.setSalesNum     (Integer.parseInt(String.valueOf( m.get("sales_num"))));
            item.setGmtCreated   (DateUtil.parse((String) m.get("gmt_created")));

            l.add(item);
        });
        System.out.println(l.get(0).getBrandName());

        manager.addBatch(l,false);
    }


}

