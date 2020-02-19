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
        return parseValueFromRemoteObject(json.getJSONObject("result").getJSONObject("result"));

        // TODO: 2/19/20
        // run Runtime.
    }

    // {"type":"number","value":7,"description":"7"}
    private Object parseValueFromRemoteObject(JSONObject remoteObject) {
        if (!StringUtil.isEmpty(remoteObject.getString("objectId"))) {
            throw new JuppeteerException("Cannot extract value when objectId is given");
        }

        String unserializableValue = remoteObject.getString("unserializableValue");
        if (!StringUtil.isEmpty(unserializableValue)) {
            if (StringUtil.equals("bigint", remoteObject.getString("type"))) {
                return new BigInteger(unserializableValue.replace("n", ""));
            }

            switch (unserializableValue) {
                case "-0":
                    return 0;
                case "NaN":
                case "Infinity":
                case "-Infinity":
                default:
                    throw new JuppeteerException("Unsupported unserializable value: " + unserializableValue);
            }
        }
        return remoteObject.get("value");
    }
}
