package com.xzzz.sbspringboot.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class Basic {
    @ExcelProperty("产品名称")
    private String prodName;
    @ExcelProperty("成立日期")
    private Date dt;
}
