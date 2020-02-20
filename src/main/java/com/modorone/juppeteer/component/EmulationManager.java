package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.pojo.Viewport;
import com.modorone.juppeteer.protocol.EmulationDomain;

import java.util.concurrent.TimeoutException;

/**
 * author: Shawn
 * time  : 2/19/20 10:51 PM
 * desc  :
 * update: Shawn 2/19/20 10:51 PM
 */
public class EmulationManager {

    private CDPSession mSession;
    private boolean mEmulatingMobile = false;
    private boolean mHasTouch = false;

    public EmulationManager(CDPSession session) {
        mSession = session;
    }

    public boolean emulateViewport(Viewport viewport) throws TimeoutException {
        boolean isMobile = viewport.isMobile();
        int width = viewport.getWidth();
        int height = viewport.getHeight();
        int deviceScaleFactor = viewport.getDeviceScaleFactor();
        boolean hasTouch = viewport.hasTouch();
        JSONObject screenOrientation = viewport.isLandscape() ? new JSONObject() {{
            put("angle", 90);
            put("type", "landscapePrimary");
        }} : new JSONObject() {{
            put("angle", 0);
            put("type", "portraitPrimary");
        }};
        mSession.doCall(EmulationDomain.setDeviceMetricsOverrideCommand, new JSONObject() {{
            put("mobile", isMobile);
            put("width", width);
            put("height", height);
            put("deviceScaleFactor", deviceScaleFactor);
            put("screenOrientation", screenOrientation);

        }});
        mSession.doCall(EmulationDomain.setTouchEmulationEnabledCommand, new JSONObject() {{
            put("enabled", hasTouch);
        }});

        boolean reloadNeeded = (mEmulatingMobile != isMobile || mHasTouch != hasTouch);
        mEmulatingMobile = isMobile;
        mHasTouch = hasTouch;
        return reloadNeeded;
    }
}
