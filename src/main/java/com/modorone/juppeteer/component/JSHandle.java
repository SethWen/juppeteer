package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.cdp.RuntimeDomain;
import com.modorone.juppeteer.util.StringUtil;

import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * author: Shawn
 * time  : 2/14/20 6:09 PM
 * desc  :
 * update: Shawn 2/14/20 6:09 PM
 */
public class JSHandle {

    protected ExecutionContext mContext;
    protected CDPSession mSession;
    protected JSONObject mRemoteObject;
    protected boolean mDisposed;

    public static JSHandle create(ExecutionContext context, JSONObject remoteObject) {
        Frame frame = context.getFrame();
        if (StringUtil.equals("node", remoteObject.getString("subtype")) && Objects.nonNull(frame)) {
            return new ElementHandle(context, context.getSession(), remoteObject, frame.getFrameManager());
        }
        return new JSHandle(context, context.getSession(), remoteObject);
    }

    public JSHandle(ExecutionContext context, CDPSession session, JSONObject remoteObject) {
        mContext = context;
        mSession = session;
        mRemoteObject = remoteObject;

    }

    public ExecutionContext getContext() {
        return mContext;
    }

    public JSONObject getRemoteObject() {
        return mRemoteObject;
    }

    public Object evaluateCodeBlock4Value(String pageFunction) throws TimeoutException {
        return mContext.evaluateCodeBlock4Value(pageFunction);
    }

    public JSHandle evaluateCodeBlock4Handle(String pageFunction) throws TimeoutException {
        return (JSHandle) mContext.evaluateCodeBlock4Handle(pageFunction);
    }

    public Object evaluateFunction4Value(String pageFunction, Object... args) throws TimeoutException {
        return mContext.evaluateFunction4Value(pageFunction, this, args);
    }

    public JSHandle evaluateFunction4Handle(String pageFunction, Object... args) throws TimeoutException {
        return mContext.evaluateFunction4Handle(pageFunction, this, args);
    }

    public Object getJsonValue() {
        try {
            if (StringUtil.isEmpty(mRemoteObject.getString("objectId"))) {
                JSONObject json = mSession.doCall(RuntimeDomain.callFunctionOnCommand, new JSONObject() {{
                    put("functionDeclaration", "function() { return this; }");
                    put("objectId", mRemoteObject.getString("objectId"));
                    put("returnByValue", true);
                    put("awaitPromise", true);
                }});
                return Helper.parseValueFromRemoteObject(json.getJSONObject("result").getJSONObject("result"));
            }
        } catch (TimeoutException ignore) {
        }
        return Helper.parseValueFromRemoteObject(mRemoteObject);
    }

    public JSHandle getProperty(String propertyName) throws TimeoutException {
        return getProperties().get(propertyName);

        // fixme 原 puppeteer 这段代码有问题看起来
//    async getProperty(propertyName) {
//    const objectHandle = await this.evaluateHandle((object, propertyName) => {
//      const result = {__proto__: null};
//            result[propertyName] = object[propertyName];
//            return result;
//        }, propertyName); // 少传一个 object 参数啊！！！
//    const properties = await objectHandle.getProperties();
//    const result = properties.get(propertyName) || null;
//        await objectHandle.dispose();
//        return result;
//    }
    }


    public Map<String, JSHandle> getProperties() throws TimeoutException {
        JSONObject json = mSession.doCall(RuntimeDomain.getPropertiesCommand, new JSONObject() {{
            put("objectId", mRemoteObject.getString("objectId"));
            put("ownProperties", true);
        }});
        JSONArray results = json.getJSONObject("result").getJSONArray("result");
        Map<String, JSHandle> properties = new HashMap<>();
        results.forEach(o -> {
            JSONObject property = (JSONObject) o;
            if (property.getBoolean("enumerable")) {
                properties.put(property.getString("name"), create(mContext, property.getJSONObject("value")));
            }
        });
        return properties;
    }

    public JSHandle asElement() {
        return null;
    }

    public void dispose() {
        if (mDisposed) return;

        mDisposed = true;
        Helper.releaseObject(mSession, mRemoteObject.getString("objectId"));
    }

    public boolean isDisposed() {
        return mDisposed;
    }

    @Override
    public String toString() {
        if (StringUtil.isEmpty(mRemoteObject.getString("objectId"))) {
            return "JSHandle:" + Helper.parseValueFromRemoteObject(mRemoteObject);
        } else {
            String type = Objects.nonNull(mRemoteObject.getString("subtype")) ?
                    mRemoteObject.getString("subtype") : mRemoteObject.getString("type");
            return "JSHandle@" + type;
        }
    }
}
