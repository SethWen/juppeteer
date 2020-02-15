package com.modorone.juppeteer;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.cdp.Connection;
import com.modorone.juppeteer.component.Page;
import com.modorone.juppeteer.component.Target;
import com.modorone.juppeteer.protocol.TargetDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

/**
 * author: Shawn
 * time  : 1/13/20 2:50 PM
 * desc  :
 * update: Shawn 1/13/20 2:50 PM
 */
public class Browser {

    private static final Logger logger = LoggerFactory.getLogger(Browser.class);
    private Connection mConnection;
    private String mUrl;

    private final Map<String, Target> mTargets = new HashMap<>();
    private final Target.TargetListener targetListener = new Target.TargetListener() {

        @Override
        public void onCreate(Target.TargetInfo targetInfo, Function<String, CDPSession> sessionFactory) {
            Target target = Target.create(targetInfo, sessionFactory);
            mTargets.put(targetInfo.getTargetId(), target);
            logger.debug("onCreate: targets={}", mTargets);
        }

        @Override
        public void onChange(Target.TargetInfo targetInfo) {

        }

        @Override
        public void onDestroy(Target.TargetInfo targetInfo) {

        }
    };


    public static Browser create(Connection connection) throws TimeoutException {
        Browser browser = new Browser(connection);
        connection.doCall(TargetDomain.setDiscoverTargetsCommand, new JSONObject() {{
            put("discover", true);
        }});
        return browser;
    }

    public Browser(Connection connection) {
        mConnection = connection;
        mConnection.setTargetListener(targetListener);
    }

    public Page newPage() throws TimeoutException {
//        const {targetId} = await this._connection.send('Target.createTarget', {url: 'about:blank', browserContextId: contextId || undefined});
//    const target = await this._targets.get(targetId);
//        assert(await target._initializedPromise, 'Failed to create target for page');
//    const page = await target.page();
        return createPageInContext(null);
    }

    private Page createPageInContext(String contextId) throws TimeoutException {
        JSONObject json = mConnection.doCall(TargetDomain.createTargetCommand, new JSONObject() {{
            put("url", "about:blank");
            put("browserContextId", contextId);
        }});
        String targetId = json.getJSONObject("result").getString("targetId");
        return mTargets.get(targetId).newPage();
    }

    public String getUrl() {
        return mUrl;
    }

    // chromium process
    public void getProcess() {

    }

    public void createIncognitoBrowserContext() {
    }

    public void getBrowserContexts() {
//        return [this._defaultContext, ...Array.from(this._contexts.values())];
    }

    public void getDefaultBrowserContext() {
//        return this._defaultContext;
    }

    public void onTargetCreate() {

    }

    public void onTargetChange() {

    }

    public void onTargetDestroy() {

    }

    public void getWsEndpoint() {

    }

    public String getVersion() {
        return "";
    }

    public String getUserAgent() {
        return "";
    }

    public void close() {

    }

    public boolean isConnected() {
        return false;
    }

    public void disconnect() {

    }
}
