package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.CommandOptions;
import com.modorone.juppeteer.MediaType;
import com.modorone.juppeteer.cdp.*;
import com.modorone.juppeteer.component.input.Keyboard;
import com.modorone.juppeteer.component.input.Mouse;
import com.modorone.juppeteer.component.input.Touchscreen;
import com.modorone.juppeteer.component.network.Request;
import com.modorone.juppeteer.component.network.Response;
import com.modorone.juppeteer.exception.JuppeteerException;
import com.modorone.juppeteer.exception.RequestException;
import com.modorone.juppeteer.pojo.*;
import com.modorone.juppeteer.util.BlockingCell;
import com.modorone.juppeteer.util.FileUtil;
import com.modorone.juppeteer.util.StringUtil;
import com.modorone.juppeteer.util.SystemUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;


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

    private Consumer<ConsoleMessage> mConsoleConsumer;
    private Consumer<Dialog> mDialogConsumer;
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

        mSession.setPageListener(new PageListener() {
            @Override
            public void onConsoleMessage(JSONObject message) {
                ConsoleMessage consoleMessage = new ConsoleMessage();
                consoleMessage.setType(message.getString("type"));
                String text = message.getJSONArray("args").stream()
                        .map(kv -> ((JSONObject) kv).getString("value"))
                        .collect(Collectors.joining(","));
                consoleMessage.setText(text);
                consoleMessage.setArgs(message.getJSONArray("args"));
                if (Objects.nonNull(mConsoleConsumer)) {
                    mConsoleConsumer.accept(consoleMessage);
                }
            }

            @Override
            public void onDialog(JSONObject dialog) {
                Dialog dg = new Dialog();
                dg.setSession(mSession);
                dg.setType(dialog.getString("type"));
                dg.setMessage(dialog.getString("message"));
                System.out.println(dg);
                if (Objects.nonNull(mDialogConsumer)) {
                    mDialogConsumer.accept(dg);
                }
            }
        });
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

    public void emulateTimezone(String timezoneId) throws TimeoutException, IllegalArgumentException {
        JSONObject result = mSession.doCall(EmulationDomain.setTimezoneOverrideCommand, new JSONObject() {{
            put("timezoneId", Objects.nonNull(timezoneId) ? timezoneId : "");
        }});

        if (Objects.nonNull(result.getJSONObject("error")) && Objects.nonNull(result.getJSONObject("error").getString("message"))
                && result.getJSONObject("error").getString("message").contains("Invalid timezone")) {
            throw new IllegalArgumentException("Invalid timezone ID: " + timezoneId);
        }
    }

    public void setConsoleConsumer(Consumer<ConsoleMessage> consumer) {
        mConsoleConsumer = consumer;
    }

    public void setDialogConsumer(Consumer<Dialog> consumer) {
        mDialogConsumer = consumer;
    }

    public void addRequestInterceptor(Consumer<Request> consumer) {
        mFrameManager.getNetworkManager().addRequestConsumer(consumer);
    }

    public boolean removeRequestInterceptor(Consumer<Request> consumer) {
        return mFrameManager.getNetworkManager().removeRequestConsumer(consumer);
    }

    public void addResponseInterceptor(Consumer<Response> consumer) {
        mFrameManager.getNetworkManager().removeResponseConsumer(consumer);
    }

    public boolean removeResponseInterceptor(Consumer<Response> consumer) {
        return mFrameManager.getNetworkManager().removeResponseConsumer(consumer);
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

    public Object evaluateCodeBlock4Value(String jsCodeBlock) throws TimeoutException, InterruptedException {
        return getMainFrame().evaluateCodeBlock4Value(jsCodeBlock);
    }

    public JSHandle evaluateCodeBlock4Handle(String jsCodeBlock) throws TimeoutException, InterruptedException {
        return getMainFrame().evaluateCodeBlock4Handle(jsCodeBlock);
    }

    public Object evaluateFunction4Value(String pageFunction, Object... args) throws TimeoutException, InterruptedException {
        return getMainFrame().evaluateFunction4Value(pageFunction, args);
    }

    public JSHandle evaluateFunction4Handle(String pageFunction, Object... args) throws TimeoutException, InterruptedException {
        return getMainFrame().evaluateFunction4Handle(pageFunction, args);
    }

    public void evaluateOnNewDocument(String jsCodeBlock, Object... args) throws TimeoutException {
        String params = Arrays.stream(args).map(JSON::toJSONString).collect(Collectors.joining(","));
        String source = String.format("(%s)(%s)", jsCodeBlock, params);
        mSession.doCall(PageDomain.addScriptToEvaluateOnNewDocumentCommand, new JSONObject() {{
            put("source", source);
        }});
    }

    public void waitFor(long timeout) {
        SystemUtil.sleep(timeout);
    }

    public Response waitForNavigation(CommandOptions options) throws InterruptedException, ExecutionException, TimeoutException {
        return getMainFrame().waitForNavigation(options);
    }

    public ElementHandle waitForSelector(String selector, CommandOptions options) throws TimeoutException {
        return getMainFrame().waitForSelector(selector, options);
    }

    public ElementHandle waitForXpath(String xpath, CommandOptions options) throws TimeoutException {
        return getMainFrame().waitForXPath(xpath, options);
    }

    public JSHandle waitForFunction(String function, CommandOptions options, Object... args) throws TimeoutException {
        return getMainFrame().waitForFunction(function, options, args);
    }

    public boolean waitForRequest(Predicate<Request> predicate, CommandOptions options) throws TimeoutException {
        BlockingCell<Boolean> waiter = new BlockingCell<>();
        Consumer<Request> consumer = request -> {
            if (predicate.test(request)) waiter.setIfUnset(true);
        };
        mFrameManager.getNetworkManager().addRequestConsumer(consumer);
        try {
            return waiter.uninterruptibleGet(options.getTimeout());
        } finally {
            mFrameManager.getNetworkManager().removeRequestConsumer(consumer);
        }
    }

    public boolean waitForResponse(Predicate<Response> predicate, CommandOptions options) throws TimeoutException {
        BlockingCell<Boolean> waiter = new BlockingCell<>();
        Consumer<Response> consumer = response -> {
            if (predicate.test(response)) waiter.setIfUnset(true);
        };
        mFrameManager.getNetworkManager().addResponseConsumer(consumer);
        try {
            return waiter.uninterruptibleGet(options.getTimeout());
        } finally {
            mFrameManager.getNetworkManager().removeResponseConsumer(consumer);
        }
    }

    public void waitForFileChooser() {

    }

    public void hover(String selector) throws TimeoutException {
        getMainFrame().hover(selector);
    }

    public void focus(String selector) throws TimeoutException {
        getMainFrame().focus(selector);
    }

    public void click(String selector, JSONObject options) throws TimeoutException {
        getMainFrame().click(selector, options);
    }

    public void type(String selector, String text, JSONObject options) throws TimeoutException {
        getMainFrame().type(selector, text, options);
    }

    public void tap(String selector) throws TimeoutException {
        getMainFrame().tap(selector);
    }

    public void press(String selector, String key, JSONObject options) throws TimeoutException {
        getMainFrame().press(selector, key, options);
    }


    public ElementHandle $(String selector) throws TimeoutException {
        return getMainFrame().$(selector);
    }

    public List<ElementHandle> $$(String selector) throws TimeoutException {
        return getMainFrame().$$(selector);
    }

    public Object $eval(String selector, String pageFunction, Object... args) throws TimeoutException {
        return getMainFrame().$eval(selector, pageFunction, args);
    }

    public Object $$eval(String selector, String pageFunction, Object... args) throws TimeoutException {
        return getMainFrame().$$eval(selector, pageFunction, args);
    }

    public List<ElementHandle> $x(String expression) throws TimeoutException {
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

    public JSHandle addScriptTag(HtmlTag scriptTag) throws TimeoutException {
        return getMainFrame().addScriptTag(scriptTag);
    }

    public JSHandle addStyleTag(HtmlTag styleTag) throws TimeoutException {
        return getMainFrame().addStyleTag(styleTag);
    }

    /**
     * @param format  png|jpeg
     * @param path
     * @param quality [0, 100]
     * @return
     * @throws TimeoutException
     * @throws IOException
     */
    public String takeScreenshot(String format, String path, int quality) throws TimeoutException, IOException {
        if (!StringUtil.equals("png", format) && !StringUtil.equals("jpeg", format)) {
            throw new JuppeteerException("invalid type, type should be png or jpeg");
        }

        if (quality > 100 || quality < 0) {
            throw new JuppeteerException("invalid quality, quality should between 0 and 100");
        }

        mSession.doCall(TargetDomain.activeTargetCommand, new JSONObject() {{
            put("targetId", mTarget.getTargetInfo().getTargetId());
        }});

        JSONObject metrics = mSession.doCall(PageDomain.getLayoutMetricsCommand).getJSONObject("result");
        int width = (int) Math.ceil(metrics.getJSONObject("contentSize").getIntValue("width"));
        int height = (int) Math.ceil(metrics.getJSONObject("contentSize").getIntValue("height"));

        JSONObject screenOrientation = mViewport.isLandscape() ? new JSONObject() {{
            put("angle", 90);
            put("type", "landscapePrimary");
        }} : new JSONObject() {{
            put("angle", 0);
            put("type", "portraitPrimary");
        }};
        mSession.doCall(EmulationDomain.setDeviceMetricsOverrideCommand, new JSONObject() {{
            put("mobile", mViewport.isMobile());
            put("width", width);
            put("height", height);
            put("deviceScaleFactor", mViewport.getDeviceScaleFactor());
            put("screenOrientation", screenOrientation);
        }});

        JSONObject clip = new JSONObject() {{
            put("x", 0);
            put("y", 0);
            put("width", width);
            put("height", height);
            put("scale", mViewport.getDeviceScaleFactor());
        }};
        JSONObject result = mSession.doCall(PageDomain.captureScreenshotCommand, new JSONObject() {{
            put("format", format);
            put("quality", quality);
            put("clip", clip);
        }}).getJSONObject("result");

        byte[] bytes = Base64.getDecoder().decode(result.getString("data"));
        if (Objects.nonNull(path)) {
            String fileName = String.format("%d.%s", System.currentTimeMillis(), format);
            FileUtil.writeByteArrayToFile(new File(fileName), bytes);
        }
        return result.getString("data");
    }

    /**
     * 低版本 chromium 未实现改功能
     *
     * @param format {@link Helper#getPaperFormats}
     * @param path
     * @return
     * @throws TimeoutException
     * @throws IOException
     */
    public String takePDF(String format, String path) throws TimeoutException, IOException {
        JSONObject formatJson = Helper.getPaperFormats().getJSONObject(format);
        if (Objects.isNull(format)) {
            throw new JuppeteerException("invalid format, see Helper#getPaperFormats");
        }

        int width = formatJson.getIntValue("width");
        int height = formatJson.getIntValue("height");
        JSONObject result = mSession.doCall(PageDomain.printToPDFCommand, new JSONObject() {{
            put("transferMode", "ReturnAsBase64");
            put("landscape", false);
            put("displayHeaderFooter", false);
            put("printBackground", false);
            put("scale", 1);
            put("paperWidth", width);
            put("paperHeight", height);
            put("marginTop", 0);
            put("marginBottom", 0);
            put("marginLeft", 0);
            put("marginRight", 0);
            put("preferCSSPageSize", false);
        }}).getJSONObject("result");
        byte[] bytes = Base64.getDecoder().decode(result.getString("data"));
        if (Objects.nonNull(path)) {
            String fileName = String.format("%d.pdf", System.currentTimeMillis());
            FileUtil.writeByteArrayToFile(new File(fileName), bytes);
        }
        return result.getString("data");
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
    }

    public interface PageListener {

        void onConsoleMessage(JSONObject message);

        void onDialog(JSONObject dialog);
    }
}
