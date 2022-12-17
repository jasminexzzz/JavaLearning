package com.xzzz.sbspringboot.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import lombok.Data;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/excel")
public class ExcelController {

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {


        try (ExcelReader excelReader = EasyExcel.read(file.getInputStream()).build()) {

            List<Basic> basics = new ArrayList<>();

            ReadSheet readSheet1 = EasyExcel.readSheet(0)
            .head(Basic.class)
            .registerReadListener(new AnalysisEventListener<Basic>() {
                @Override
                public void invoke(Basic o, AnalysisContext analysisContext) {
                    System.out.println(o.toString());
                    basics.add(o);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext) {

                }
            })
            .build();

            ReadSheet readSheet2 = EasyExcel.readSheet(3)
            .head(Manager.class)
            .registerReadListener(new AnalysisEventListener<Manager>() {
                @Override
                public void invoke(Manager o, AnalysisContext analysisContext) {
                    System.out.println(o.toString());
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext) {

                }
            }).build();

            // 这里注意 一定要把sheet1 sheet2 一起传进去，不然有个问题就是03版的excel 会读取多次，浪费性能
            excelReader.read(readSheet1, readSheet2);
        }

        return "OK";
    }


    @Data
    public static class Basic {
        @ExcelProperty(index = 0)
        private String prodName;

        @ExcelProperty(index = 1)
        @DateTimeFormat("yyyy-MM-dd HH:mm:ss:")
        private Date dt;
    }

    @Data
    public static class Manager {
        @ExcelProperty(index = 0)
        private String prodName;

        @ExcelProperty(index = 1)
        @DateTimeFormat("yyyy-MM-dd HH:mm:ss:")
        private Date dt;

        @ExcelProperty(index = 2)
        @NumberFormat("#.##")
        private String doubleDate;

    }


}
