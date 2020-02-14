package com.modorone.juppeteer.cdp;

import com.alibaba.fastjson.JSONObject;

import java.util.concurrent.TimeoutException;

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

    public JSONObject doCall(String method) throws TimeoutException {
        return mConnection.doCall(new JSONObject() {{
            put("sessionId", mSessionId);
            put("method", method);
        }});
    }

    public JSONObject doCall(String method, JSONObject params) throws TimeoutException {
        return mConnection.doCall(new JSONObject() {{
            put("sessionId", mSessionId);
            put("method", method);
            put("params", params);
        }});
    }

    public void close() {
        mConnection = null;
    }
}
