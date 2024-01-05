/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.lxg.wschat.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * GetAccessTokenUtil
 *
 * @author linxugeng
 * @since 2024/1/2
 */
@Component
public class GetAccessTokenUtil {
    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    public static String clientId;

    public static String clientSecret;


    @Value("${baidu.clientSecret}")
    private void setClientSecret(String secret) {
        GetAccessTokenUtil.clientSecret = secret;
    }


    @Value("${baidu.clientId}")
    private  void setClientId(String id) {
        GetAccessTokenUtil.clientId = id;
    }





//    private static String clientId = "7d8B4sjXqzFIZSItGjzMBPoE";
//
//    private static String clientSecret = "fMyp1YHdcvI0NrlwfRzKGp740eEkuqVn";


    public static String getToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");

        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token?client_id="+clientId+"&client_secret="+clientSecret+"&grant_type=client_credentials")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
//        System.out.println(response.body().string());
        //只返回access_token
        String s = response.body().string();
        //转为json对象
        JSONObject jsonObject = JSON.parseObject(s);
        //获取access_token
        String access_token = jsonObject.getString("access_token");
        return access_token;
    }


    public static void main(String []args) throws IOException{
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"messages\":[{\"role\":\"user\",\"content\":\"111\"}],\"disable_search\":false,\"enable_citation\":false}");
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions?access_token=" + "24.4a9dad57f2dab744ed9ba76646e67948.2592000.1706844125.282335-46245809")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        System.out.println(response.body().string());

    }
}
