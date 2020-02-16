package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.Browser;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.protocol.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeoutException;

/**
 * author: Shawn
 * time  : 2/14/20 2:22 PM
 * desc  :
 * update: Shawn 2/14/20 2:22 PM
 */
public class Page {

    private static final Logger logger = LoggerFactory.getLogger(Page.class);

    private String mUrl;
    private CDPSession mSession;
    private FrameManager mFrameManager;

    public static Page create(CDPSession session) throws TimeoutException {
        Page page = new Page(session);
        page.init();
        return page;
    }

    private Page(CDPSession session) {
        mSession = session;

        mFrameManager = new FrameManager(session, this);

    }

    private void init() throws TimeoutException {
//        await Promise.all([
//                this._frameManager.initialize(),
//                this._client.send('Target.setAutoAttach', {autoAttach: true, waitForDebuggerOnStart: false, flatten: true}),
//        this._client.send('Performance.enable', {}),
//                this._client.send('Log.enable', {}),
//                this._client.send('Page.setInterceptFileChooserDialog', {enabled: true}).catch(e => {
//                this._fileChooserInterceptionIsDisabled = true;
//      }),
//    ]);
        mFrameManager.init();
        mSession.doCall(TargetDomain.setAutoAttachCommand, new JSONObject() {{
            put("autoAttach", true);
            put("waitForDebuggerOnStart", false);
            put("flatten", true);
        }});
        mSession.doCall(PerformanceDomain.enableCommand, new JSONObject());
        mSession.doCall(LogDomain.enableCommand, new JSONObject());
        try {
            mSession.doCall(PageDomain.setInterceptFileChooserDialogCommand, new JSONObject() {{
                put("enabled", true);
            }});
        } catch (Exception e) {
            // TODO: 2/14/20
        }
    }

    private String getUrl() {
        return mUrl;
    }

    public Browser getBrowser() {
        return null;
    }

    public Target getTarget() {
        return null;
    }

    public Frame getMainFrame() {
        Frame frame = mFrameManager.getMainFrame();
        return frame;
    }

    public void navigate(String url) {
        mFrameManager.getMainFrame().navigate(url);
    }

    public void setGeolocation(double longitude, double latitude, double accuracy) throws TimeoutException {
        if (longitude < -180 || longitude > 180)
            throw new IllegalArgumentException("Invalid longitude(" + longitude + "): precondition -180 <= LONGITUDE <= 180 failed.");
        if (latitude < -90 || latitude > 90)
            throw new IllegalArgumentException("Invalid latitude(" + latitude + "): precondition - 90 <= LATITUDE <= 90 failed.");
        if (accuracy < 0)
            throw new IllegalArgumentException("Invalid accuracy(" + accuracy + ") :precondition 0 <= ACCURACY failed.");

        mSession.doCall(EmulationDomain.setGeolocationOverrideCommand, new JSONObject() {{
            put("longitude", longitude);
            put("latitude", latitude);
            put("accuracy", accuracy);

        }});
    }
}
