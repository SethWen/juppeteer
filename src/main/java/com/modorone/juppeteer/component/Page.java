package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.protocol.LogDomain;
import com.modorone.juppeteer.protocol.PageDomain;
import com.modorone.juppeteer.protocol.PerformanceDomain;
import com.modorone.juppeteer.protocol.TargetDomain;

import java.util.concurrent.TimeoutException;

/**
 * author: Shawn
 * time  : 2/14/20 2:22 PM
 * desc  :
 * update: Shawn 2/14/20 2:22 PM
 */
public class Page {

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
}
