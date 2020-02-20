package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.exception.JuppeteerException;
import com.modorone.juppeteer.protocol.RuntimeDomain;
import com.modorone.juppeteer.util.StringUtil;

import java.math.BigInteger;
import java.util.concurrent.TimeoutException;

/**
 * author: Shawn
 * time  : 2/19/20 6:36 PM
 * desc  :
 * update: Shawn 2/19/20 6:36 PM
 */
public class Helper {

    public static void releaseObject(CDPSession session, String objectId) {
        if (StringUtil.isEmpty(objectId)) return;

        try {
            session.doCall(RuntimeDomain.releaseObjectCommand, new JSONObject(){{
                put("objectId", objectId);
            }});
        } catch (TimeoutException e) {
            // Exceptions might happen in case of a page been navigated or closed.
            // Swallow these since they are harmless and we don't leak anything in this case.
            e.printStackTrace();
            // TODO: 2/19/20 debug error
        }
    }

    public static Object parseValueFromRemoteObject(JSONObject remoteObject) {
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
                    return null;
//                    throw new JuppeteerException("Unsupported unserializable value: " + unserializableValue);
            }
        }
        return remoteObject.get("value");
    }
}
