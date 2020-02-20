package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.Browser;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.component.input.Keyboard;
import com.modorone.juppeteer.component.input.Mouse;
import com.modorone.juppeteer.component.input.Touchscreen;
import com.modorone.juppeteer.pojo.Device;
import com.modorone.juppeteer.pojo.Viewport;
import com.modorone.juppeteer.protocol.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
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
    private Target mTarget;
    private FrameManager mFrameManager;
    private boolean mJavascriptEnabled = true;

    private Viewport mViewport;
    private Keyboard mKeyboard;
    private Mouse mMouse;
    private Touchscreen mTouchscreen;
    private Coverage mCoverage;
    private Tracing mTracing;
    private Accessibility mAccessibility;
    private EmulationManager mEmulationManager;

    public static Page create(CDPSession session, Target target) throws TimeoutException {
        Page page = new Page(session, target);
        page.init();
        return page;
    }

    private Page(CDPSession session, Target target) {
        mSession = session;
        mTarget = target;

        mFrameManager = new FrameManager(session, this);
        mKeyboard = new Keyboard(session);
        mMouse = new Mouse(session, mKeyboard);
        mTouchscreen = new Touchscreen(session, mKeyboard);
        mCoverage = new Coverage(session);
        mTracing = new Tracing(session);
        mAccessibility = new Accessibility(session);
        mEmulationManager = new EmulationManager(session);
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

    public String getUrl() {
        return getMainFrame().getUrl();
    }

    public String getContent() {
        return getMainFrame().getContent();
    }

    public Browser getBrowser() {
        return mTarget.getBrowser();
    }

    public Target getTarget() {
        return mTarget;
    }

    public Frame getMainFrame() {
        return mFrameManager.getMainFrame();
    }

    public Keyboard getKeyboard() {
        return mKeyboard;
    }

    public Mouse getMouse() {
        return mMouse;
    }

    public Touchscreen getTouchscreen() {
        return mTouchscreen;
    }

    public Coverage getCoverage() {
        return mCoverage;
    }

    public Tracing getTracing() {
        return mTracing;
    }

    public Accessibility getAccessibility() {
        return mAccessibility;
    }

    public List<Frame> getFrames() {
        return mFrameManager.getFrames();
    }

    public void setRequestInterception(boolean enabled) throws TimeoutException {
        mFrameManager.getNetworkManager().setRequestInterception(enabled);
    }

    public void setOfflineMode(boolean enabled) throws TimeoutException {
        mFrameManager.getNetworkManager().setOfflineMode(enabled);
    }

    public void setDefaultNavigationTimeout(int timeout) {
//        this._timeoutSettings.setDefaultNavigationTimeout(timeout);
    }

    public void setDefaultTimeout(int timeout) {
//        this._timeoutSettings.setDefaultTimeout(timeout);
    }

    public void setAuthenticate(String username, String password) throws TimeoutException {
        mFrameManager.getNetworkManager().setAuthenticate(username, password);
    }

    public void setExtraHTTPHeaders(Map<String, String> headers) throws TimeoutException {
        mFrameManager.getNetworkManager().setExtraHTTPHeaders(headers);
    }

    public void setUserAgent(String userAgent) throws TimeoutException {
        mFrameManager.getNetworkManager().setUserAgent(userAgent);
    }

    public void setViewPort(Viewport viewPort) throws TimeoutException {
        boolean reloadNeeded = mEmulationManager.emulateViewport(viewPort);
        mViewport = viewPort;
        if (reloadNeeded) reload();

    }

    public void emulate(Device device) throws TimeoutException {
        setUserAgent(device.getUserAgent());
        setViewPort(device.getViewport());
    }


    public boolean isJavascriptEnabled() {
        return mJavascriptEnabled;
    }

    public void setJavaScriptEnabled(boolean enabled) throws TimeoutException {
        if (mJavascriptEnabled == enabled) return;

        mJavascriptEnabled = enabled;
        mSession.doCall(EmulationDomain.setScriptExecutionDisabledCommand, new JSONObject() {{
            put("value", !enabled);
        }});
    }

    public void navigate(String url) throws TimeoutException {
        mFrameManager.getMainFrame().navigate(url);
    }

    public void reload() {
        // TODO: 2/20/20
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

    public Object evaluateCodeBlock4Value(String pageFunction) throws TimeoutException, InterruptedException {
        return mFrameManager.getMainFrame().evaluateCodeBlock4Value(pageFunction);
    }

    public Object evaluateCodeBlock4Handle(String pageFunction) throws TimeoutException, InterruptedException {
        return mFrameManager.getMainFrame().evaluateCodeBlock4Handle(pageFunction);
    }

    public Object evaluateFunction4Value(String pageFunction, Object... args) throws TimeoutException, InterruptedException {
        return mFrameManager.getMainFrame().evaluateFunction4Value(pageFunction, args);
    }

    public Object evaluateFunction4Handle(String pageFunction, Object... args) throws TimeoutException, InterruptedException {
        return mFrameManager.getMainFrame().evaluateFunction4Handle(pageFunction, args);
    }

    public void hover(String selector) throws TimeoutException, InterruptedException {
        getMainFrame().hover(selector);
    }

    public void click(String selector, JSONObject options) throws TimeoutException, InterruptedException {
        getMainFrame().click(selector, options);
    }
}
