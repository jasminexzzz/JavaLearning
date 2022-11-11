package com.xzzz.C1_framework.Log;

public class FileWriterTest {

    public static void main(String[] args) {
        MetricFileWriter metricFileWriter = new MetricFileWriter(
                20000000,
                100,
                "C:\\WangYunFei\\GitCode\\Learning\\learning-java\\src\\main\\java\\com\\xzzz\\C1_framework\\Log\\",
                "testApp");
        long start = System.currentTimeMillis();

        // 日志内容: 1667032377000|2022-10-29 16:32:57|bsms/sentinel/es/metric/histogram/resource|19|21|19|0|0|0|0|

        // 8_640_000: 以每条50条资源的速率, 连续写入2天, 共计8_640_000条数据, 大小为: 839MB, 普通机械盘用时93秒
        for (int i = 1; i <= 100; i++) {
            try {
                metricFileWriter.write(System.currentTimeMillis(), "1667032377000|2022-10-29 16:32:57|bsms/sentinel/es/metric/histogram/resource|19|21|19|0|0|0|0|" + i + "\r\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println((System.currentTimeMillis() - start) / 1000.0);
    }
}
