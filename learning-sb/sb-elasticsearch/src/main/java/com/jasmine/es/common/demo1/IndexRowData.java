package com.jasmine.es.common.demo1;

import java.util.LinkedHashMap;

/**
 * @ClassName: IndexRowData
 * @Description: 索引结果数据
 * @author: zhangxw
 * @date: 2019/5/27
 */
public class IndexRowData extends LinkedHashMap<String, Object> {

    private static final long serialVersionUID = 4040420174717552587L;

    public IndexRowData build(String filedName, Object filedValue) {
        this.put(filedName, filedValue);
        return this;
    }
}
