package com.jasmine.Other.写着玩的小工具.gamersky;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.jasmine.Other.MyUtils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class GSUtil implements GSProperties {
    private static final Logger log = LoggerFactory.getLogger(GSUtil.class);

    static String fileNameResolver (String url) {
        return StrUtil.sub(url,StrUtil.lastIndexOfIgnoreCase(url,"/") + 1,url.length());
    }

    /**
     * 解析请求API路径
     * @return API路径
     */
    static String urlResolver() {
        return StrUtil.sub(ART_URL,0,StrUtil.lastIndexOfIgnoreCase(ART_URL,REQUEST) + REQUEST.length() + 1);
    }

    /**
     * 解析请求体
     * @return 解析后的Request实体
     */
    static GSRequest requestResolver() {
        int requestBegin = StrUtil.lastIndexOfIgnoreCase(ART_URL,REQUEST) + REQUEST.length() + 1;
        int requestEnd = StrUtil.lastIndexOfIgnoreCase(ART_URL,"&");
        String requestURLCode = StrUtil.sub(ART_URL,requestBegin,requestEnd);


        if (StrUtil.isBlank(requestURLCode)) {
            throw new NullPointerException("[ request 为空 ]");
        }

        String requestJson = URLUtil.decode(requestURLCode);
        GSRequest request =  JsonUtil.json2Object(requestJson,GSRequest.class);
        Optional.ofNullable(request).orElseThrow(() -> new NullPointerException("[ 请求体解析错误: " + requestJson + " ]"));

        if (("DEBUG").equals(LOG_LEVEL)) {
            log.debug("requestURLCode： {}",requestURLCode);
            log.debug("requestJson: {}",requestJson);
        }

        return request;
    }

    /**
     * 封装请求体
     * @param request 请求实体
     * @return URL编码后的请求体
     */
    static String requestWrapper (GSRequest request) {
        Optional.ofNullable(request).orElseThrow(() -> new NullPointerException("[ 请求体封装错误，请求体为空 ]"));
        String requestJson = JsonUtil.object2Json(request);
        String requestURLCode = URLUtil.encode(requestJson);

//        System.out.println(requestJson);
//        System.out.println(requestURLCode);

        if (("DEBUG").equals(LOG_LEVEL)) {
            log.debug("==================================");
            log.debug("requestJson: {}",requestJson);
            log.debug("requestURLCode： {}",requestURLCode);
        }

        return requestURLCode;
    }
}
