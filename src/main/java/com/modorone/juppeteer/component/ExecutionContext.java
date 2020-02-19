package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.Constants;
import com.modorone.juppeteer.Juppeteer;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.exception.JuppeteerException;
import com.modorone.juppeteer.protocol.RuntimeDomain;
import com.modorone.juppeteer.util.StringUtil;

import java.math.BigInteger;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

/**
 * author: Shawn
 * time  : 2/18/20 11:42 PM
 * desc  :
 * update: Shawn 2/18/20 11:42 PM
 */
public class ExecutionContext {

    private CDPSession mSession;
    private DomWorld mWorld;
    private int mContextId;

    public ExecutionContext(CDPSession session, DomWorld world, JSONObject contextPayload) {
        mSession = session;
        mWorld = world;
        mContextId = contextPayload.getInteger("id");
    }

    public Frame getFrame() {
        if (Objects.nonNull(mWorld)) {
            return mWorld.getFrame();
        } else {
            return null;
        }
    }

    public CDPSession getSession() {
        return mSession;
    }

    public DomWorld getWorld() {
        return mWorld;
    }

    public void setWorld(DomWorld world) {
        mWorld = world;
    }

    public Object evaluate(String pageFunction) throws TimeoutException {
        return evaluateInternal(true, pageFunction);
    }

    public Object evaluateHandle(String pageFunction) throws TimeoutException {
        return evaluateInternal(false, pageFunction);
    }

    private Object evaluateInternal(boolean returnByValue, String pageFunction) throws TimeoutException {
        String suffix = "\n" + Constants.EVALUATION_SCRIPT_URL;
        String expression = (pageFunction + suffix);
        System.out.println("fun: " + expression);
        JSONObject json = mSession.doCall(RuntimeDomain.evaluateCommand, new JSONObject() {{
            put("expression", expression);
            put("contextId", mContextId);
            put("returnByValue", returnByValue);
            put("awaitPromise", true);
            put("userGesture", true);
        }});
        if (returnByValue) {
            return Helper.parseValueFromRemoteObject(json.getJSONObject("result").getJSONObject("result"));
        } else {
            return JSHandle.create(this, json.getJSONObject("result").getJSONObject("result"));
        }

        // TODO: 2/19/20
        // run Runtime.
    }
}
