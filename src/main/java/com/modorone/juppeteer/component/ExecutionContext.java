package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.Constants;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.exception.JSEvaluationException;
import com.modorone.juppeteer.exception.JuppeteerException;
import com.modorone.juppeteer.cdp.RuntimeDomain;
import com.modorone.juppeteer.util.StringUtil;

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

    public Object evaluateCodeBlock4Value(String jsCodeBlock) throws TimeoutException {
        return evaluateCodeBlock(true, jsCodeBlock);
    }

    public Object evaluateCodeBlock4Handle(String jsCodeBlock) throws TimeoutException {
        return evaluateCodeBlock(false, jsCodeBlock);
    }

    private Object evaluateCodeBlock(boolean returnByValue, String jsCodeBlock) throws TimeoutException {
        String suffix = "\n" + Constants.EVALUATION_SCRIPT_URL + "\n";
        String expression = (jsCodeBlock + suffix);
        System.out.println("evaluateCodeBlock: " + expression);
        JSONObject json = mSession.doCall(RuntimeDomain.evaluateCommand, new JSONObject() {{
            put("expression", expression);
            put("contextId", mContextId);
            put("returnByValue", returnByValue);
            put("awaitPromise", true);
            put("userGesture", true);
        }}).getJSONObject("result");
        if (returnByValue) {
            return Helper.parseValueFromRemoteObject(json.getJSONObject("result"));
        } else {
            return JSHandle.create(this, json.getJSONObject("result"));
        }
    }

    public Object evaluateFunction4Value(String pageFunction, Object... args) throws TimeoutException {
        return evaluateFunction(pageFunction, true, args);
    }

    public Object evaluateFunction4Handle(String pageFunction, Object... args) throws TimeoutException {
        return evaluateFunction(pageFunction, false, args);
    }

    private Object evaluateFunction(String pageFunction, boolean returnByValue, Object... args) throws TimeoutException {
        String suffix = "\n" + Constants.EVALUATION_SCRIPT_URL + "\n";
        pageFunction += suffix;
        System.out.println("evaluateFunction: \n" + pageFunction);

        JSONArray arguments = new JSONArray();
        for (Object arg : args) {
            if (Objects.nonNull(arg) && arg instanceof JSHandle) {
                JSHandle handle = (JSHandle) arg;
                if (handle.getContext() != this)
                    throw new JuppeteerException("JSHandle can be evaluated only in the context they were created!");
                if (handle.isDisposed()) throw new JuppeteerException("JSHandle is disposed!");

                if (StringUtil.nonEmpty(handle.getRemoteObject().getString("unserializableValue"))) {
                    arguments.add(new JSONObject() {{
                        put("unserializableValue", handle.getRemoteObject().getString("unserializableValue"));
                    }});
                    break;
                }

                if (StringUtil.isEmpty(handle.getRemoteObject().getString("objectId"))) {
                    arguments.add(new JSONObject() {{
                        put("value", handle.getRemoteObject().getString("value"));
                    }});
                    break;
                }

                arguments.add(new JSONObject() {{
                    put("objectId", handle.getRemoteObject().getString("objectId"));
                }});
            } else {
                arguments.add(new JSONObject(){{
                    put("value", arg);
                }});
            }
        }

        String functionText = pageFunction;
        JSONObject json = mSession.doCall(RuntimeDomain.callFunctionOnCommand, new JSONObject() {{
            put("functionDeclaration", functionText);
            put("executionContextId", mContextId);
            put("arguments", arguments);
            put("returnByValue", returnByValue);
            put("awaitPromise", true);
            put("userGesture", true);
        }}).getJSONObject("result");
        JSONObject exceptionDetails = json.getJSONObject("exceptionDetails");
        if (Objects.nonNull(exceptionDetails)) {
            throw new JSEvaluationException("Evaluation failed: " + exceptionDetails.toJSONString());
        }

        if (returnByValue) {
            return Helper.parseValueFromRemoteObject(json.getJSONObject("result"));
        } else {
            return JSHandle.create(this, json.getJSONObject("result"));
        }
    }
}
