package com.jiyehoo.informationentry.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @author JiyeHoo
 * @date 21-1-1 下午12:58
 */
public class HttpUtil {
    public static void sendOkHttpRequest(String url, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
