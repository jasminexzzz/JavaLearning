package com.jasmine.Other.写着玩的小工具.gamersky.common;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.jasmine.Other.MyUtils.JsonUtil;
import com.jasmine.Other.写着玩的小工具.gamersky.dto.GSRequest;

import java.io.File;
import java.util.Optional;

public class GSUtil implements GSProperties {

    public static String fileNameResolver (String url) {
        return StrUtil.sub(url,StrUtil.lastIndexOfIgnoreCase(url,"/") + 1,url.length());
    }

    /**
     * 解析请求API路径
     * @return API路径
     */
    public static String urlResolver() {
        return StrUtil.sub(ART_URL,0,StrUtil.lastIndexOfIgnoreCase(ART_URL,REQUEST) + REQUEST.length() + 1);
    }

    /**
     * 解析请求体
     * @return 解析后的Request实体
     */
    public static GSRequest requestResolver() {
        int requestBegin = StrUtil.lastIndexOfIgnoreCase(ART_URL,REQUEST) + REQUEST.length() + 1;
        int requestEnd = StrUtil.lastIndexOfIgnoreCase(ART_URL,"&");

        String requestURLCode = StrUtil.sub(ART_URL,requestBegin,requestEnd);
        if (StrUtil.isBlank(requestURLCode)) {
            throw new NullPointerException("[ request 为空 ]");
        }

        String requestJson = URLUtil.decode(requestURLCode);
        GSRequest request =  JsonUtil.json2Object(requestJson,GSRequest.class);
        Optional.ofNullable(request).orElseThrow(() -> new NullPointerException("[ 请求体解析错误: " + requestJson + " ]"));

        return request;
    }

    /**
     * 封装请求体
     * @param request 请求实体
     * @return URL编码后的请求体
     */
    public static String requestWrapper (GSRequest request) {
        Optional.ofNullable(request).orElseThrow(() -> new NullPointerException("[ 请求体封装错误，请求体为空 ]"));
        String requestJson = JsonUtil.object2Json(request);
        String requestURLCode = URLUtil.encode(requestJson);

        return requestURLCode;
    }

    /**
     * 创建文件夹
     * @param path
     */
    public static void createFolder (String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
            System.out.println("文件夹创建成功");
        } else {
            System.out.println("文件夹已存在无需创建");
        }
    }
}
