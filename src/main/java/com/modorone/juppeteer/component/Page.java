package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.CommandOptions;
import com.modorone.juppeteer.MediaType;
import com.modorone.juppeteer.cdp.*;
import com.modorone.juppeteer.component.input.Keyboard;
import com.modorone.juppeteer.component.input.Mouse;
import com.modorone.juppeteer.component.input.Touchscreen;
import com.modorone.juppeteer.component.network.Response;
import com.modorone.juppeteer.exception.JuppeteerException;
import com.modorone.juppeteer.exception.RequestException;
import com.modorone.juppeteer.pojo.*;
import com.modorone.juppeteer.util.StringUtil;
import com.modorone.juppeteer.util.SystemUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


/**
 * author: Shawn
 * time  : 2/14/20 2:22 PM
 * desc  :
 * update: Shawn 2/14/20 2:22 PM
 */
public class Page {

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

    private boolean mIsClosed;

    public static Page create(CDPSession session, Target target) throws Exception {
        Page page = new Page(session, target);
        page.init();
        page.setViewport(new Viewport());
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

    public String getTitle() throws TimeoutException, InterruptedException {
        return getMainFrame().getTitle();
    }

    public String getContent() throws TimeoutException, InterruptedException {
        return getMainFrame().getContent();
    }

    /**
     * @param {string}     html
     * @param {!{timeout?: number, waitUntil?: string|!Array<string>}=} options
     */
    public void setContent(String html, CommandOptions options) throws TimeoutException, InterruptedException {
        getMainFrame().setContent(html, options);
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

    Keyboard getKeyboard() {
        return mKeyboard;
    }

    Mouse getMouse() {
        return mMouse;
    }

    Touchscreen getTouchscreen() {
        return mTouchscreen;
    }

    Coverage getCoverage() {
        return mCoverage;
    }

    Tracing getTracing() {
        return mTracing;
    }

    Accessibility getAccessibility() {
        return mAccessibility;
    }

    public List<Frame> getFrames() {
        return mFrameManager.getFrames();
    }

    public JSONObject getMetrics() throws TimeoutException {
        JSONObject result = mSession.doCall(PerformanceDomain.getMetricsCommand).getJSONObject("result");
        return buildMetricsObject(result.getJSONArray("metrics"));
    }

    private JSONObject buildMetricsObject(JSONArray metrics) {
        if (Objects.isNull(metrics)) return new JSONObject();

        JSONObject result = new JSONObject();
        for (Object metric : metrics) {
            JSONObject m = (JSONObject) metric;
            if (Helper.getSupportedMetrics().contains(m.getString("name"))) {
                result.put(m.getString("name"), m.getString("value"));
            }
        }
        return result;
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

    public void setIgnoreHTTPSErrors(boolean ignore) throws TimeoutException {
        mSession.doCall(SecurityDomain.setIgnoreCertificateErrorsCommand, new JSONObject() {{
            put("ignore", ignore);
        }});
    }

    public void setUserAgent(String userAgent) throws TimeoutException {
        mFrameManager.getNetworkManager().setUserAgent(userAgent);
    }

    public Viewport getViewport() {
        return mViewport;
    }

    public void setViewport(Viewport viewport) throws TimeoutException, ExecutionException, InterruptedException {
        boolean reloadNeeded = mEmulationManager.emulateViewport(viewport);
        mViewport = viewport;
        if (reloadNeeded) reload(null);

    }

    public void emulate(Device device) throws TimeoutException, ExecutionException, InterruptedException {
        setUserAgent(device.getUserAgent());
        setViewport(device.getViewport());
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

    public void setCacheEnabled(boolean enabled) throws TimeoutException {
        mFrameManager.getNetworkManager().setCacheEnabled(enabled);
    }

    public void setBypassCSP(boolean enabled) throws TimeoutException {
        mSession.doCall(PageDomain.setBypassCSPCommand, new JSONObject() {{
            put("enabled", enabled);
        }});
    }

    public void emulateMediaType(MediaType mediaType) throws TimeoutException {
        mSession.doCall(EmulationDomain.setEmulatedMediaCommand, new JSONObject() {{
            put("media", mediaType.getType());
        }});
    }

    public void emulateMediaFeatures(List<MediaFeature> features) throws TimeoutException {
        if (Objects.isNull(features)) {
            mSession.doCall(EmulationDomain.setEmulatedMediaCommand, new JSONObject() {{
                put("features", null);
            }});
            return;
        }

        mSession.doCall(EmulationDomain.setEmulatedMediaCommand, new JSONObject() {{
            put("features", features);
        }});
    }

    /**
     * @param timezoneId
     * @throws TimeoutException
     * @throws IllegalArgumentException
     * @link https://docs.oracle.com/middleware/1221/wcs/tag-ref/MISC/TimeZones.html
     * @see java.util.TimeZone
     */
    public void emulateTimezone(String timezoneId) throws TimeoutException, IllegalArgumentException {
        JSONObject result = mSession.doCall(EmulationDomain.setTimezoneOverrideCommand, new JSONObject() {{
            put("timezoneId", Objects.nonNull(timezoneId) ? timezoneId : "");
        }});

        if (Objects.nonNull(result.getJSONObject("error")) && Objects.nonNull(result.getJSONObject("error").getString("message"))
                && result.getJSONObject("error").getString("message").contains("Invalid timezone")) {
            throw new IllegalArgumentException("Invalid timezone ID: " + timezoneId);
        }
    }

    public Response navigate(String url, CommandOptions options) throws RequestException {
        return mFrameManager.getMainFrame().navigate(url, options);
    }

    public Response goBack(CommandOptions options) throws TimeoutException, ExecutionException, InterruptedException {
        return go(-1, options);
    }

    public Response goForward(CommandOptions options) throws TimeoutException, ExecutionException, InterruptedException {
        return go(+1, options);
    }

    private Response go(int delta, CommandOptions options) throws TimeoutException, ExecutionException, InterruptedException {
        JSONObject history = mSession.doCall(PageDomain.getNavigationHistoryCommand).getJSONObject("result");
        JSONArray entries = history.getJSONArray("entries");
        JSONObject entry = entries.getJSONObject(history.getIntValue("currentIndex") + delta);
        if (Objects.isNull(entry)) return null;

        mSession.doCall(PageDomain.navigateToHistoryEntryCommand, new JSONObject() {{
            put("entryId", entry.getIntValue("id"));
        }});
        return waitForNavigation(options);
    }

    public void bringToFront() throws TimeoutException {
        mSession.doCall(PageDomain.bringToFrontCommand);
    }

    // timeout|waitUntil
    public Response reload(CommandOptions options) throws TimeoutException, ExecutionException, InterruptedException {
        mSession.doCall(PageDomain.reloadCommand);
        return waitForNavigation(options);
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
        return getMainFrame().evaluateCodeBlock4Value(pageFunction);
    }

    public Object evaluateCodeBlock4Handle(String pageFunction) throws TimeoutException, InterruptedException {
        return getMainFrame().evaluateCodeBlock4Handle(pageFunction);
    }

    public Object evaluateFunction4Value(String pageFunction, Object... args) throws TimeoutException, InterruptedException {
        return getMainFrame().evaluateFunction4Value(pageFunction, args);
    }

    public Object evaluateFunction4Handle(String pageFunction, Object... args) throws TimeoutException, InterruptedException {
        return getMainFrame().evaluateFunction4Handle(pageFunction, args);
    }

    public void waitFor(long timeout) {
        SystemUtil.sleep(timeout);
    }

    public Response waitForNavigation(CommandOptions options) throws InterruptedException, ExecutionException, TimeoutException {
        return getMainFrame().waitForNavigation(options);
    }

    public ElementHandle waitForSelector(String selector, CommandOptions options) {
        return getMainFrame().waitForSelector(selector, options);
    }

    public void waitForXpath(String xpath, JSONObject options) {

    }

    public void waitForFunction(String function, JSONObject options) {

    }

    public void waitForRequest() {

    }

    public void waitForResponse() {

    }

    public void waitForFileChooser() {

    }

    public void hover(String selector) throws TimeoutException, InterruptedException {
        getMainFrame().hover(selector);
    }

    public void click(String selector, JSONObject options) throws TimeoutException, InterruptedException {
        getMainFrame().click(selector, options);
    }

    public void type(String selector, String text, JSONObject options) throws TimeoutException, InterruptedException {
        getMainFrame().type(selector, text, options);
    }

    public void tap(String selector) throws TimeoutException, InterruptedException {
        getMainFrame().tap(selector);
    }

    public void press(String selector, String key, JSONObject options) throws TimeoutException, InterruptedException {
        getMainFrame().press(selector, key, options);
    }

    public ElementHandle $(String selector) throws TimeoutException, InterruptedException {
        return getMainFrame().$(selector);
    }

    public List<ElementHandle> $$(String selector) throws TimeoutException, InterruptedException {
        return getMainFrame().$$(selector);
    }

    public Object $eval(String selector, String pageFunction, Object... args) throws TimeoutException, InterruptedException {
        return getMainFrame().$eval(selector, pageFunction, args);
    }

    public Object $$eval(String selector, String pageFunction, Object... args) throws TimeoutException, InterruptedException {
        return getMainFrame().$$eval(selector, pageFunction, args);
    }

    public List<ElementHandle> $x(String expression) throws TimeoutException, InterruptedException {
        return getMainFrame().$x(expression);
    }

    public List<Cookie> getCookies(String... urls) throws TimeoutException {
        JSONArray urlArray = new JSONArray();
        if (urls.length == 0) {
            urlArray.add(getUrl());
        } else {
            Collections.addAll(urlArray, urls);
        }
        JSONArray cookies = mSession.doCall(NetWorkDomain.getCookiesCommand, new JSONObject() {{
            put("urls", urlArray);
        }}).getJSONObject("result").getJSONArray("cookies");
        return cookies.toJavaList(Cookie.class);
    }

    /**
     * @param cookies name/{domain|url}
     * @throws TimeoutException
     * @throws IllegalArgumentException
     */
    public void deleteCookie(Cookie... cookies) throws TimeoutException, IllegalArgumentException {
        String pageUrl = getUrl();
        for (Cookie cookie : cookies) {
            if (Objects.isNull(cookie)) throw new IllegalArgumentException("cookie can not be null");
            if (StringUtil.isEmpty(cookie.getName()))
                throw new IllegalArgumentException("cookie name can not be empty");
            if (StringUtil.isEmpty(cookie.getUrl()) && StringUtil.isEmpty(cookie.getDomain()))
                throw new IllegalArgumentException("at least one of the url and domain needs to be specified");

            if (StringUtil.nonEmpty(cookie.getUrl()) && StringUtil.startsWith(pageUrl, "http")) {
                cookie.setUrl(pageUrl);
            }

            mSession.doCall(NetWorkDomain.deleteCookiesCommand, new JSONObject() {{
                put("name", cookie.getName());
                put("domain", cookie.getDomain());
                put("url", cookie.getUrl());
            }});
        }
    }

    public void setCookie(Cookie... cookies) throws TimeoutException {
        String pageUrl = getUrl();
        boolean startsWithHTTP = StringUtil.startsWith(pageUrl, "http");
        for (Cookie cookie : cookies) {
            if (StringUtil.nonEmpty(cookie.getUrl()) && startsWithHTTP) {
                cookie.setUrl(pageUrl);
            }
            if (StringUtil.equals("about:blank", cookie.getUrl())) {
                throw new IllegalArgumentException("Blank page can not have cookie " + cookie.getName());
            }

            if (StringUtil.startsWith(cookie.getUrl(), "data:")) {
                throw new IllegalArgumentException("Data URL page can not have cookie " + cookie.getName());
            }
        }

        JSONArray cookieArray = new JSONArray();
        Collections.addAll(cookieArray, cookies);
        mSession.doCall(NetWorkDomain.setCookiesCommand, new JSONObject() {{
            put("cookies", cookieArray);
        }});
    }

    public JSHandle addScriptTag(HtmlTag scriptTag) throws TimeoutException, InterruptedException {
        return getMainFrame().addScriptTag(scriptTag);
    }

    public JSHandle addStyleTag(HtmlTag styleTag) throws TimeoutException, InterruptedException {
        return getMainFrame().addStyleTag(styleTag);
    }

    public boolean isClosed() {
        return mIsClosed;
    }

    public void close(boolean runBeforeUnload) throws TimeoutException, JuppeteerException {
        if (Objects.isNull(mSession) || Objects.isNull(mSession.getConnection()))
            throw new JuppeteerException("Protocol error: Connection closed. Most likely the page has been closed.");

        if (runBeforeUnload) {
            // FIXME: 2/23/20 {"error":{"code":-32601,"message":"'Page.close'' wasn't found"},"id":16,"sessionId":"C77E2BE1A9E97EDF853465A23CA12462"}
            mSession.doCall(PageDomain.closeCommand, new JSONObject());
        } else {
            mSession.doCall(TargetDomain.closeTargetCommand, new JSONObject() {{
                put("targetId", mTarget.getTargetInfo().getTargetId());
            }});
            mTarget.getCloseWaiter().uninterruptibleGet();
        }
        mIsClosed = true;
        //  {"sessionId":"9B2079F6293DDD1CF9CC409C96F37EDB","method":"Page.close","params":{},"id":16}
    }
}
