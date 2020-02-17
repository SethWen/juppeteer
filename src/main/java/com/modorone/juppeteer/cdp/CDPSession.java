package com.modorone.juppeteer.cdp;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.component.Frame;
import com.modorone.juppeteer.component.network.NetworkListener;
import com.modorone.juppeteer.component.network.NetworkManager;

import java.util.concurrent.TimeoutException;

import static com.modorone.juppeteer.Constants.DEFAULT_RPC_TIMEOUT;

/**
 * author: Shawn
 * time  : 2/13/20 11:44 PM
 * desc  :
 * update: Shawn 2/13/20 11:44 PM
 */
public class CDPSession {

    private Connection mConnection;
    private String mSessionId;
    private String mTargetType;
//     this._callbacks = new Map();


    public CDPSession(Connection connection, String sessionId, String targetType) {
        mConnection = connection;
        mSessionId = sessionId;
        mTargetType = targetType;
    }

    public void setFrameListener(Frame.FrameListener listener) {
        mConnection.setFrameListener(listener);
    }
    public void setNetworkListener(NetworkListener listener) {
        mConnection.setNetworkListener(listener);
    }

    public JSONObject doCall(String method) throws TimeoutException {
        return doCall(method, DEFAULT_RPC_TIMEOUT);
    }

    public JSONObject doCall(String method, int timeout) throws TimeoutException {
        return mConnection.doCall(new JSONObject() {{
            put("sessionId", mSessionId);
            put("method", method);
        }}, timeout);
    }

    public JSONObject doCall(String method, JSONObject params) throws TimeoutException {
        return doCall(method, params, DEFAULT_RPC_TIMEOUT);
    }

    public JSONObject doCall(String method, JSONObject params, int timeout) throws TimeoutException {
        return mConnection.doCall(new JSONObject() {{
            put("sessionId", mSessionId);
            put("method", method);
            put("params", params);
        }}, timeout);
    }

    public void close() {
        mConnection = null;
    }
}
