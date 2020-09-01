package com.jasmine.Other.写着玩的小工具.gamersky;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;

/**
 * 读取
 */
public class GSReadPage extends GSDownQueue {
    // 游戏路径
    private final String url;
    private final GSRequest gsRequest;
    private final String pathName;

    private int maxPage = 1;

    public GSReadPage(String url, GSRequest gsRequest) {
        this.url = url;
        this.gsRequest = gsRequest;
        this.gsRequest.setPageIndex(1);
        this.pathName = this.gsRequest.getArticleId();
    }

    public void read () {
        System.out.println("=========> 开始下载");

        for (;;) {
            String requestJson = GSUtil.requestWrapper(gsRequest);
            try {
                GSResponse response = bodyResolver(HttpRequest.get(url + requestJson).execute().body());
//                System.out.println(
//                    String.format("[%s] 文章:%s,页数 [%s] 响应体: %s",
//                        Thread.currentThread().getName(),gsRequest.getArticleId(), gsRequest.getPageIndex(),response)
//                );

                /**
                 * 将每页留言中包含的图片放入队列
                 */
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
                System.err.println("重置页数");
                requestIndexReset();
                // e.printStackTrace();
            }
        }
    }

    private void requestIndexIncrement () {
        gsRequest.setPageIndex(gsRequest.getPageIndex() + 1);
        maxPage++;
    }

    private void requestIndexReset () {
        gsRequest.setPageIndex(1);
        maxPage = 1;
    }

    private GSResponse bodyResolver (String body) {
        if (StrUtil.isBlank(body)) {
            throw new GSNoneRespException("响应体为空");
        }

        int first = StrUtil.indexOf(body,'{');
        int last  = StrUtil.lastIndexOfIgnoreCase(body,"}") + 1;
        String respJson = StrUtil.sub(body,first,last);
        if (StrUtil.isBlank(respJson)) {
            throw new GSNoneRespException("响应体为空");
        }
        GSResponse response = JSONUtil.toBean(respJson,GSResponse.class);
        // System.out.println("body: " + response);

        if (response.getResult().getComments() == null || response.getResult().getComments().length == 0) {
            throw new GSNoneRespException("响应体为空");
        }
        return response;
    }
}
