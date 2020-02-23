package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.DeviceDescriptor;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.exception.JuppeteerException;
import com.modorone.juppeteer.cdp.RuntimeDomain;
import com.modorone.juppeteer.util.StringUtil;

import java.math.BigInteger;
import java.util.*;
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
            session.doCall(RuntimeDomain.releaseObjectCommand, new JSONObject() {{
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

    private static Set<String> mSupportedMetrics;
    private static JSONObject mPaperFormats;

    public static Set<String> getSupportedMetrics() {
        if (Objects.isNull(mSupportedMetrics)) {
            synchronized (Helper.class) {
                if (Objects.isNull(mSupportedMetrics)) {
                    mSupportedMetrics = new HashSet<String>() {{
                        add("Timestamp");
                        add("Documents");
                        add("Frames");
                        add("JSEventListeners");
                        add("Nodes");
                        add("LayoutCount");
                        add("RecalcStyleCount");
                        add("LayoutDuration");
                        add("RecalcStyleDuration");
                        add("ScriptDuration");
                        add("TaskDuration");
                        add("JSHeapUsedSize");
                        add("JSHeapTotalSize");
                    }};
                }
            }
        }
        return mSupportedMetrics;
    }

    public static JSONObject getPaperFormats() {
        if (Objects.isNull(mPaperFormats)) {
            synchronized (Helper.class) {
                if (Objects.isNull(mPaperFormats)) {
                    mPaperFormats = new JSONObject() {{
                        put("letter", new JSONObject() {{
                            put("width", 8.5);
                            put("height", 11);
                        }});
                        put("legal", new JSONObject() {{
                            put("width", 8.5);
                            put("height", 14);
                        }});
                        put("tabloid", new JSONObject() {{
                            put("width", 11);
                            put("height", 17);
                        }});
                        put("ledger", new JSONObject() {{
                            put("width", 17);
                            put("height", 11);
                        }});
                        put("a0", new JSONObject() {{
                            put("width", 33.1);
                            put("height", 46.8);
                        }});
                        put("a1", new JSONObject() {{
                            put("width", 23.4);
                            put("height", 33.1);
                        }});
                        put("a2", new JSONObject() {{
                            put("width", 16.54);
                            put("height", 23.4);
                        }});
                        put("a3", new JSONObject() {{
                            put("width", 11.7);
                            put("height", 16.54);
                        }});
                        put("a4", new JSONObject() {{
                            put("width", 8.27);
                            put("height", 11.7);
                        }});
                        put("a5", new JSONObject() {{
                            put("width", 5.83);
                            put("height", 8.27);
                        }});
                        put("a6", new JSONObject() {{
                            put("width", 4.13);
                            put("height", 5.83);
                        }});
                    }};
                }
            }
        }
        return mPaperFormats;
    }

    private static Map<String, Float> mUnit2PixelMap;

    public static Float getPixelByUnit(String unit) {
        if (Objects.isNull(mUnit2PixelMap)) {
            synchronized (DeviceDescriptor.class) {
                mUnit2PixelMap = new HashMap<String, Float>() {{
                    put("px", 1f);
                    put("in", 96f);
                    put("cm", 37.8f);
                    put("mm", 3.78f);
                }};
            }
        }
        return mUnit2PixelMap.get(unit);
    }
}
