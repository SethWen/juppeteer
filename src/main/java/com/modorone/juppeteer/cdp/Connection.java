package com.modorone.juppeteer.cdp;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.exception.WebSocketCreationException;
import com.modorone.juppeteer.util.SystemUtil;
import okhttp3.*;
import okhttp3.Request;
import okio.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * author: Shawn
 * time  : 2/13/20 3:41 PM
 * desc  : WebSocket client
 * update: Shawn 2/13/20 3:41 PM
 */
public class Connection extends WebSocketListener {

    private static final Logger logger = LoggerFactory.getLogger(Connection.class);
    private WebSocket mWebSocket;
    private boolean mIsAlive;

    public static Connection create(String url) {
        return new Connection(url);
    }

    private Connection(String url) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .pingInterval(Integer.MAX_VALUE, TimeUnit.MICROSECONDS) // 禁用 ping/pong, 因为服务端未实现
                .build();
        Request request = new Request.Builder().url(url).build();
        mWebSocket = client.newWebSocket(request, this);
        waitForCreated();
        SystemUtil.sleep(2000);

        // Target.setDiscoverTargets', {discover: true}
        send("{\"id\":123,\"method\":\"Target.setDiscoverTargets\",\"params\":{\"discover\":true}}");
        SystemUtil.sleep(2000);
//        send("{\"id\":15,\"method\":\"Target.createTarget\",\"params\":{\"url\":\"https://www.baidu.com\"}}}");
//        send("{\"id\":16,\"method\":\"Runtime.enable\"}");
//        send("{\"id\":123,\"method\":\"Page.navigate\",\"params\":{\"url\":\"https://www.baidu.com\",\"age\":8}}");
//        send(new JSONObject() {{
//            put("id", 232);
//            put("method", "Target.createTarget");
//            put("params", new JSONObject(){{
//                put("url", "https://www.baidu.com");
//            }});
//        }});

//        send(new JSONObject() {{
//            put("id", 233);
//            put("method", "Storage.getCookies");
//        }});
        send(new JSONObject() {{
            put("id", 1);
            put("method", "Target.getTargets");
        }});
        // 每个 target 对应一个 domain，必须 attach 后 获取 sessionId，才能开始操作该 domain
        SystemUtil.sleep(2000);
        String targetId = "";
        send(new JSONObject() {{
            put("id", 2);
            put("method", "Target.attachToTarget");
            put("params", new JSONObject() {{
                put("targetId", targetId);
                put("flatten", true);
            }});
        }});
        SystemUtil.sleep(2000);
        String sessionId = "";
        send(new JSONObject() {{
            put("sessionId", sessionId);
            put("id", 3);
            put("method", "Page.navigate");
            put("params", new JSONObject() {{
                put("url", "https://www.baidu.com");
            }});
        }});

    }

    private void waitForCreated() {
        long start = System.currentTimeMillis();
        while (!mIsAlive) {
            if (System.currentTimeMillis() - start > 30 * 1000) {
                throw new WebSocketCreationException("connect to browser timeout");
            }
            SystemUtil.sleep(100);
        }
    }

    public boolean send(String text) {
        return mWebSocket.send(text);
    }

    public boolean send(JSONObject json) {
        return mWebSocket.send(json.toJSONString());
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        logger.debug("onOpen: ");
        mIsAlive = true;
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        logger.debug("onMessage: text={}", text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);
        logger.debug("onMessage: bytes={}", bytes);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
        logger.debug("onClosing: ");
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        logger.debug("onClosed: code={},reason={}", code, reason);
        mIsAlive = false;
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        logger.error("onFailure: ", t);
        mIsAlive = false;
    }
}
