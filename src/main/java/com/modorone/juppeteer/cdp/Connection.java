package com.modorone.juppeteer.cdp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.exception.WebSocketCreationException;
import com.modorone.juppeteer.util.StringUtil;
import com.modorone.juppeteer.util.SystemUtil;
import okhttp3.*;
import okhttp3.Request;
import okio.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

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

    private final Map<Integer, BlockingCell<String>> mContinuationMap = new HashMap<>();
    private AtomicInteger mId = new AtomicInteger(1);


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

        testRPC();

    }

    private void testRPC() {
        try {

            JSONObject reply1 = doCall(new JSONObject() {{
                put("id", mId);
                put("method", "Target.getTargets");
            }});
            JSONArray jsonArray = reply1.getJSONObject("result").getJSONArray("targetInfos");
            String targetId = "";
            for (Object o : jsonArray) {
                JSONObject j = (JSONObject) o;
                if (StringUtil.equals("page", j.getString("type"))) {
                    targetId = j.getString("targetId");
                }
            }
//            // 每个 target 对应一个 domain，必须 attach 后 获取 sessionId，才能开始操作该 domain
            SystemUtil.sleep(2000);

            String finalTargetId = targetId;
            JSONObject reply2 = doCall(new JSONObject() {{
                put("id", mId);
                put("method", "Target.attachToTarget");
                put("params", new JSONObject() {{
                    put("targetId", finalTargetId);
                    put("flatten", true);
                }});
            }});
            SystemUtil.sleep(2000);
            String sessionId = reply2.getJSONObject("result").getString("sessionId");
            doCall(new JSONObject() {{
                put("sessionId", sessionId);
                put("id", mId);
                put("method", "Page.navigate");
                put("params", new JSONObject() {{
                    put("url", "https://www.baidu.com");
                }});
            }});
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public String doCall(String text) throws TimeoutException {
        BlockingCell<String> k = new BlockingCell<>();
        int id;
        synchronized (mContinuationMap) {
            id = mId.getAndIncrement();
            mContinuationMap.put(id, k);
        }
        mWebSocket.send(text);
        String reply;
        try {
            reply = k.uninterruptibleGet(5000);
        } catch (TimeoutException ex) {
            // Avoid potential leak.  This entry is no longer needed by caller.
            mContinuationMap.remove(id);
            throw ex;
        }
        logger.debug("doCall: \n\trequest={}\n\tresponse={}", text, reply);
        return reply;
    }

    public JSONObject doCall(JSONObject json) throws TimeoutException {
        String reply = doCall(json.toJSONString());
        return JSON.parseObject(reply);
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

        JSONObject json = JSON.parseObject(text);
        if (Objects.isNull(json.getInteger("id"))) {      // event
            logger.debug("onMessage: event={}", text);
        } else {    // rpc reply
            logger.debug("onMessage: reply={}", text);
            synchronized (mContinuationMap) {
                Integer id = json.getInteger("id");
                BlockingCell<String> blocker = mContinuationMap.remove(id);
                if (blocker == null) {
                    // Entry should have been removed if request timed out,
                    // log a warning nevertheless.
                    logger.warn("No outstanding request for correlation ID {}", id);
                } else {
                    blocker.set(text);
                }
            }
        }
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
