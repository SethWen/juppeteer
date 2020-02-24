package com.modorone.juppeteer;

import okhttp3.*;
import okhttp3.MediaType;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * author: Shawn
 * time  : 2/23/20 10:31 PM
 * desc  :
 * update: Shawn 2/23/20 10:31 PM
 */
public class HttpRunner implements IRunner {

    private String bid;
    private OkHttpClient mClient = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .build();

    public HttpRunner() {
    }

    @Override
    public String run(List<String> args) throws IOException {
        // 发起创建浏览器的请求，获取到 wsEndPoint

        Request request = new Request.Builder()
                .url("https://ip.cn")           // create api
//                .post(RequestBody.create(MediaType.parse("application/json;charset=utf-8"), args.toString()))
                .build();
        Call call = mClient.newCall(request);
        Response response = call.execute();
        String wsEndPoint = null;
        if (response.isSuccessful()) {
            String string = response.body().string();
            wsEndPoint = string;
            bid = "hhh";
        } else {

        }
        // TODO: 2/24/20 是否要轮训检查浏览器状态
        return wsEndPoint;
    }

    @Override
    public void terminate() {
        // 发起创建浏览器的请求，获取到 wsEndPoint
//        mClient.newCall()
    }
}
