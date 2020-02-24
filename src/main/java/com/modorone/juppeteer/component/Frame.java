package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.CommandOptions;
import com.modorone.juppeteer.pojo.FrameInfo;
import com.modorone.juppeteer.pojo.HtmlTag;
import com.modorone.juppeteer.util.BlockingCell;
import com.modorone.juppeteer.component.network.Response;
import com.modorone.juppeteer.exception.RequestException;
import com.modorone.juppeteer.util.StringUtil;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * author: Shawn
 * time  : 2/14/20 5:57 PM
 * desc  :
 * update: Shawn 2/14/20 5:57 PM
 */
public class Frame {

    private FrameManager mFrameManager;
    private Frame mParentFrame;
    private Set<Frame> mChildFrames = ConcurrentHashMap.newKeySet();
    private Set<String> mLifecycleEvents = ConcurrentHashMap.newKeySet();
    private FrameInfo mFrameInfo;

    private DomWorld mMainWorld;
    private DomWorld mSecondaryWorld;

    private boolean mIsDetached;


    public Frame(FrameManager frameManager, Frame parentFrame, FrameInfo frameInfo) {
        mFrameManager = frameManager;
        mParentFrame = parentFrame;
        mFrameInfo = frameInfo;
        mFrameInfo.setLoaderId("");

        if (Objects.nonNull(parentFrame)) {
            getParentFrame().addChildFrame(this);
        }

        // TODO: 2/19/20 timeoutSetting
        mMainWorld = new DomWorld(frameManager, this, null);
        mSecondaryWorld = new DomWorld(frameManager, this, null);
    }

    public FrameInfo getFrameInfo() {
        return mFrameInfo;
    }

    public Set<String> getLifecycleEvents() {
        return mLifecycleEvents;
    }

    public void setFrameInfo(FrameInfo frameInfo) {
        mFrameInfo = frameInfo;
    }

    public void addChildFrame(Frame frame) {
        if (Objects.nonNull(frame)) mChildFrames.add(frame);
    }

    public void removeChildFrame(Frame frame) {
        if (Objects.nonNull(frame)) mChildFrames.remove(frame);
    }

    public Response waitForNavigation(CommandOptions options) throws InterruptedException, ExecutionException, TimeoutException {
        return mFrameManager.waitForFrameNavigation(this, options);
    }

    public Response navigate(String url, CommandOptions options) throws RequestException {
        return mFrameManager.navigateFrame(this, url, options);
    }

    public void click(/*selector, options = {}*/) {
    }

    public void tap(String selector) throws TimeoutException, InterruptedException {
        mSecondaryWorld.tap(selector);
    }

    public void press(String selector, String key, JSONObject options) throws TimeoutException, InterruptedException {
        mSecondaryWorld.press(selector, key, options);
    }

    public void type(String selector, String text, JSONObject options) throws TimeoutException, InterruptedException {
        mMainWorld.type(selector, text, options);
    }

    public void focus(/*selector*/) {
//        return this._mainWorld.focus(selector);
    }

    public void hover(String selector) throws TimeoutException, InterruptedException {
        mSecondaryWorld.hover(selector);
    }

    public void click(String selector, JSONObject options) throws TimeoutException, InterruptedException {
        mSecondaryWorld.click(selector, options);
    }

    public void proceedLifecycle(String loaderId, String name) {
        if (StringUtil.equals("init", name)) {
            mFrameInfo.setLoaderId(loaderId);
            mLifecycleEvents.clear();
        }
        if (Objects.nonNull(name)) mLifecycleEvents.add(name);
    }

    public void navigatedWithinDocument(String url) {
        mFrameInfo.setUrl(url);
    }

    public void stopLoading() {
        mLifecycleEvents.add("DOMContentLoaded");
        mLifecycleEvents.add("load");
    }

    public void detach() {
        mIsDetached = true;
        mMainWorld.detach();
        mSecondaryWorld.detach();
        if (Objects.nonNull(mParentFrame)) mParentFrame.getChildFrames().remove(this);
        mParentFrame = null;
    }

    private void navigated(/*url, name, navigationId*/) {
//        this._url = url;
//        this._name = name;
//        this._lastCommittedNavigationId = navigationId;
//        this._firedEvents.clear();
    }

    public void select(/*selector, ...values*/) {
//        return this._mainWorld.select(selector, ...values);
    }

    public void waitFor(/*selectorOrFunctionOrTimeout, options, ...args*/) {
    }

    public void waitForFunction(/*pageFunction, options = {}, ...args*/) {
    }

    public ElementHandle waitForSelector(String selector, CommandOptions options) {
//        ElementHandle handle = mSecondaryWorld.wait();
//         const handle = await this._secondaryWorld.waitForSelector(selector, options);
//        if (!handle)
//            return null;
//    const mainExecutionContext = await this._mainWorld.executionContext();
//    const result = await mainExecutionContext._adoptElementHandle(handle);
//        await handle.dispose();
//        return result;
//        return mMainWorld.waitForSelector(selector, options);
        return null;
    }

    public void waitForXPath(/*xpath, options*/) {
    }

    public BlockingCell<ExecutionContext> getContentWaiter() {
        return mMainWorld.getContextWaiter();
    }

    public void setContent(String html, CommandOptions options) throws TimeoutException, InterruptedException {
        mSecondaryWorld.setContent(html, options);
    }

    public Object evaluateCodeBlock4Value(String pageFunction) throws TimeoutException, InterruptedException {
        return mMainWorld.evaluateCodeBlock4Value(pageFunction);
    }

    public Object evaluateCodeBlock4Handle(String pageFunction) throws TimeoutException, InterruptedException {
        return mMainWorld.evaluateCodeBlock4Handle(pageFunction);
    }

    public Object evaluateFunction4Value(String pageFunction, Object... args) throws TimeoutException, InterruptedException {
        return mMainWorld.evaluateFunction4Value(pageFunction, args);
    }

    public Object evaluateFunction4Handle(String pageFunction, Object... args) throws TimeoutException, InterruptedException {
        return mMainWorld.evaluateFunction4Handle(pageFunction, args);
    }

    public ElementHandle $(String selector) throws TimeoutException, InterruptedException {
        return mMainWorld.$(selector);
    }

    public List<ElementHandle> $$(String selector) throws TimeoutException, InterruptedException {
        return mMainWorld.$$(selector);
    }

    public Object $eval(String selector, String pageFunction, Object... args) throws TimeoutException, InterruptedException {
        return mMainWorld.$eval(selector, pageFunction, args);
    }

    public Object $$eval(String selector, String pageFunction, Object... args) throws TimeoutException, InterruptedException {
        return mMainWorld.$$eval(selector, pageFunction, args);
    }

    public List<ElementHandle> $x(String expression) throws TimeoutException, InterruptedException {
        return mMainWorld.$x(expression);
    }

    public JSHandle addScriptTag(HtmlTag scriptTag) throws TimeoutException, InterruptedException {
        return mMainWorld.addScriptTag(scriptTag);
    }

    public JSHandle addStyleTag(HtmlTag styleTag) throws TimeoutException, InterruptedException {
        return mMainWorld.addStyleTag(styleTag);
    }

    public String getTitle() throws TimeoutException, InterruptedException {
        return mSecondaryWorld.getTitle();
    }

    public String getName() {
        return mFrameInfo.getName();
    }

    public boolean isDetached() {
        return mIsDetached;
    }

    public Set<Frame> getChildFrames() {
        return mChildFrames;
    }

    public Frame getParentFrame() {
        return mParentFrame;
    }

    public String getUrl() {
        return mFrameInfo.getUrl();
    }

    public String getContent() throws TimeoutException, InterruptedException {
        return mSecondaryWorld.getContent();
    }

    public DomWorld getMainWorld() {
        return mMainWorld;
    }

    public DomWorld getSecondaryWorld() {
        return mSecondaryWorld;
    }

    public FrameManager getFrameManager() {
        return mFrameManager;
    }

    public interface FrameListener {

        void onFrameAttached(FrameInfo frameInfo);

        void onFrameNavigated(FrameInfo frameInfo);

        void onFrameNavigatedWithinDocument(JSONObject event);

        void onFrameDetached(JSONObject event);

        void onFrameStoppedLoading(JSONObject event);

        void onExecutionContextCreated(JSONObject context);

        void onExecutionContextDestroyed(int executionContextId);

        void onExecutionContextsCleared();

        void onLifecycleEvent(JSONObject event);
    }
}
