package com.clark.mvpdemo.api;

import com.clark.mvpdemo.GlobalConfig;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by clark on 2016/11/18.
 */

public class HttpServiceManager {
    private static String base_url = GlobalConfig.baseUrl;

    public static String httpPost(String url, String param) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String result = "{errcode:" + -100 + ",'errmsg': '创建网络请求失败'}";
        if (null == client) {
            return result;
        }
        String httpPost = base_url + url;
        MediaType type = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(type, param);
        Request request = new Request.Builder()
                .url(httpPost)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            result = response.body().string();
        }
        return result;
    }

    public static String httpGet(String url) throws Exception {
        OkHttpClient client = new OkHttpClient();
        String result = "{errcode:" + -100 + ",'errmsg': '创建网络请求失败'}";
        if (null == client) {
            return result;
        }
        String httpGet = base_url + url;
        Request request = new Request.Builder().url(httpGet).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            result = response.body().string();
        } else {
            result = "{errcode:" + response.code() + ",'errmsg': '" + response.message() + "'}";
        }
        return result;
    }
//
//    /**
//     * okhttp做的兼容https请求地址
//     *
//     * @return
//     */
//    public static OkHttpClient getTLSHttpClient() {
//
//        OkHttpClient client = null;
//        try {
//            OkHttpClient.Builder builder = new OkHttpClient.Builder();
//            builder.connectTimeout(10, TimeUnit.SECONDS);
//            builder.readTimeout(10, TimeUnit.SECONDS);
//            builder.writeTimeout(10, TimeUnit.SECONDS);
//            builder.retryOnConnectionFailure(false);
//            client = builder.build();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return client;
//    }
}
