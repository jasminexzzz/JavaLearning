package com.xzzz.Other.MyUtils;

import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author : jasmineXz
 */
public class OkHttpUtil {

    private static OkHttpClient okHttpClient;
    private static CacheControl cc = new CacheControl.Builder().noCache().noStore().build();

    static {
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    public static Response get (String url) throws IOException {
        Request request = new Request.Builder()
                .cacheControl(cc)
                .url(url)
                .build();
        return okHttpClient.newCall(request).execute();
    }
}
