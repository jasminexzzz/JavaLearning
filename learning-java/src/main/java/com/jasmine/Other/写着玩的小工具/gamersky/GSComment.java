package com.jasmine.Other.写着玩的小工具.gamersky;

import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.jasmine.Other.写着玩的小工具.gamersky.common.GSNoneRespException;
import com.jasmine.Other.写着玩的小工具.gamersky.common.GSUtil;
import com.jasmine.Other.写着玩的小工具.gamersky.dto.GSRequest;
import com.jasmine.Other.写着玩的小工具.gamersky.dto.GSResponse;
import com.jasmine.Other.写着玩的小工具.gamersky.dto.PicElement;

/**
 * 读取评论
 */
public class GSComment extends GSDownloadQueue {
    private final String url;// 请求路径
    private final GSRequest gsRequest;// 请求体
    private final String pathName;

    private int maxPage = 1;

    public GSComment(String url, GSRequest gsRequest) {
        this.url = url;
        this.gsRequest = gsRequest;
        this.gsRequest.setPageIndex(1);
        this.pathName = this.gsRequest.getArticleId();
    }

    /**
     * 无线循环读取文章评论区
     */
    public void read () {
        System.out.println("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 开始请求 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
        TimeInterval time = new TimeInterval();
        time.start();

        for (;;) {
            //System.out.println("resq: " + gsRequest);
            String requestJson = GSUtil.requestWrapper(gsRequest);
            //System.out.println("requestJson: " + requestJson);
            try {
                GSResponse response = bodyResolver(HttpRequest.get(url + requestJson).execute().body());
                // 将每页留言中包含的图片放入队列
                GSResponse.Comments[] page = response.getResult().getComments();
                for (GSResponse.Comments comments : page) {
                    if (comments.getImageInfes() != null && comments.getImageInfes().length > 0) {
                        for (GSResponse.ImageInfes imageInfe : comments.getImageInfes()) {
                            String url = imageInfe.getOrigin();
                            PicElement element = new PicElement(pathName,url);
                            put(element); // 放入队列
                        }
                    }
                }
                requestIndexIncrement();
            // 其他异常跳过
            } catch (HttpException e) {
                requestIndexIncrement();
            // 无返回体从头开始查询
            } catch (GSNoneRespException e) {
                System.err.println("重置页数, 用时: " + time.intervalPretty());
                requestIndexReset();
                // e.printStackTrace();
            }
        }
    }

    /**
     * 页数递增
     */
    private void requestIndexIncrement () {
        gsRequest.setPageIndex(gsRequest.getPageIndex() + 1);
        maxPage ++;
    }

    /**
     * 页数重置
     */
    private void requestIndexReset () {
        gsRequest.setPageIndex(1);
        maxPage = 1;
    }

    /**
     * 响应体解析
     * @param body body
     * @return resp
     */
    private GSResponse bodyResolver (String body) {
        if (StrUtil.isBlank(body)) {
            throw new GSNoneRespException("响应体为空");
        }

        int first = StrUtil.indexOf(body,'{');
        int last  = StrUtil.lastIndexOfIgnoreCase(body,"}") + 1;
        String respJson = StrUtil.sub(body,first,last);
        //System.out.println("resp:" + respJson);
        if (StrUtil.isBlank(respJson)) {
            throw new GSNoneRespException("响应体为空");
        }

        GSResponse response = JSONUtil.toBean(respJson,GSResponse.class);
        if (response.getResult().getComments() == null || response.getResult().getComments().length == 0) {
            throw new GSNoneRespException("响应体为空");
        }
        return response;
    }
}
