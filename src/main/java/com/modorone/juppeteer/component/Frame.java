package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.modorone.juppeteer.CommandOptions;
import com.modorone.juppeteer.util.BlockingCell;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.component.network.Response;
import com.modorone.juppeteer.exception.RequestException;
import com.modorone.juppeteer.util.StringUtil;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeoutException;

/**
 * author: Shawn
 * time  : 2/14/20 5:57 PM
 * desc  :
 * update: Shawn 2/14/20 5:57 PM
 */
public class Frame {

    private CDPSession mSession;
    private FrameManager mFrameManager;
    private Set<Frame> mChildFrames = new HashSet<>();
    private Set<String> mLifecycleEvents = new HashSet<>();
    private FrameInfo mFrameInfo;

    private DomWorld mMainWorld;
    private DomWorld mSecondaryWorld;


    public Frame(CDPSession session, FrameManager frameManager, FrameInfo frameInfo) {
        mSession = session;
        mFrameManager = frameManager;
        mFrameInfo = frameInfo;

        Frame parentFrame = getParentFrame();
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
        mChildFrames.add(frame);
    }

    public void removeChildFrame(Frame frame) {
        mChildFrames.remove(frame);
    }

    public void waitForNavigation(/*option*/) {

    }

    public Response navigate(String url, CommandOptions options) throws RequestException {
        return mFrameManager.navigateFrame(this, url, options);

//        {"result":{"frameId":"5BF35E2DDCE3A76E506283D505690927","loaderId":"B88468FB682EFF0E86F170FA2EC53E05"},"id":13,"sessionId":"552B9FEF3C4ECC110FEC21EDC6149EE2"}
//        {"result":{"errorText":"net::ERR_ABORTED","frameId":"75521C56E71CBAB0B6106F596ADD4E99","loaderId":"65B60665621A7266D2125A476C94041F"},"id":13,"sessionId":"885EA32C6AD1C33EF8DC7BF40889ABCA"}
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
        mLifecycleEvents.add(name);
    }

    public void navigatedWithinDocument(String url) {
        mFrameInfo.setUrl(url);
    }

    public void stopLoading() {
        mLifecycleEvents.add("DOMContentLoaded");
        mLifecycleEvents.add("load");
    }

    public void detach() {
//         this._detached = true;
//    this._mainWorld._detach();
//    this._secondaryWorld._detach();
//    if (this._parentFrame)
//      this._parentFrame._childFrames.delete(this);
//    this._parentFrame = null;
        // TODO: 2/16/20 other logic
        if (Objects.nonNull(getParentFrame())) {
            getParentFrame().removeChildFrame(this);
        }
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

    public void waitForSelector(/*selector, options*/) {
//        return this._mainWorld.waitForSelector(selector, options);
    }

    public void waitForXPath(/*xpath, options*/) {
    }

    public BlockingCell<ExecutionContext> getContentWaiter() {
//        return this._mainWorld.content();
        return mMainWorld.getContextWaiter();
    }

    public void setContent(String html) {
//        return this._mainWorld.setContent(html);
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

    public void $(/*selector*/) {
//        return this._mainWorld.$(selector);
    }

    public void $$(/*selector*/) {
//        return this._mainWorld.$$(selector);
    }

    public void $eval(/*selector, pageFunction, ...args*/) {
//        return this._mainWorld.$eval(selector, pageFunction, ...args);
    }

    public void $$eval(/*selector, pageFunction, ...args*/) {
//        return this._mainWorld.$$eval(selector, pageFunction, ...args);
    }

    public void $x(/*expression*/) {
//        return this._mainWorld.$x(expression);
    }

    public void evaluateHandle(/*pageFunction, ...args*/) {
//        return this._mainWorld.evaluateHandle(pageFunction, ...args);
    }

    public void addScriptTag(/*options*/) {
//        return this._mainWorld.addScriptTag(options);
    }

    public void addStyleTag(/*options*/) {
//        return this._mainWorld.addStyleTag(options);
    }

    public String getTitle() throws TimeoutException, InterruptedException {
        return mSecondaryWorld.getTitle();
    }

    public String getName() {
        return mFrameInfo.getName();
    }

    public boolean isDetached() {
        return false;
    }

    public Set<Frame> getChildFrames() {
        return mChildFrames;
    }

    public Frame getParentFrame() {
        return mFrameManager.getFrame(mFrameInfo.getParentId());
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

    public static class FrameInfo {

        /**
         * securityOrigin : chrome-search://most-visited
         * loaderId : 572BDFDF2604EFE662C0BE4E4F68F496
         * name : custom-links-edit
         * id : 928ADFF8EE2A1010693140D686E27B6C
         * mimeType : text/html
         * parentId : 16EB95E8F0EE66874D98EFD7DF315C51
         * url : chrome-search://most-visited/edit.html?addTitle=Add%20shortcut&editTitle=Edit%20shortcut&nameField=Name&urlField=URL&linkRemove=Remove&linkCancel=Cancel&linkDone=Done&invalidUrl=Type%20a%20valid%20URL
         */

        @JSONField(name = "securityOrigin")
        private String securityOrigin;
        @JSONField(name = "loaderId")
        private String loaderId;
        @JSONField(name = "name")
        private String name;
        @JSONField(name = "id")
        private String id;
        @JSONField(name = "mimeType")
        private String mimeType;
        @JSONField(name = "parentId")
        private String parentId;
        @JSONField(name = "url")
        private String url;

        public String getSecurityOrigin() {
            return securityOrigin;
        }

        public void setSecurityOrigin(String securityOrigin) {
            this.securityOrigin = securityOrigin;
        }

        public String getLoaderId() {
            return loaderId;
        }

        public void setLoaderId(String loaderId) {
            this.loaderId = loaderId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMimeType() {
            return mimeType;
        }

        public void setMimeType(String mimeType) {
            this.mimeType = mimeType;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "FrameInfo{" +
                    "securityOrigin='" + securityOrigin + '\'' +
                    ", loaderId='" + loaderId + '\'' +
                    ", name='" + name + '\'' +
                    ", id='" + id + '\'' +
                    ", mimeType='" + mimeType + '\'' +
                    ", parentId='" + parentId + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    public interface FrameListener {
//         this._client.on('Page.frameAttached', event => this._onFrameAttached(event.frameId, event.parentFrameId));
//    this._client.on('Page.frameNavigated', event => this._onFrameNavigated(event.frame));
//    this._client.on('Page.navigatedWithinDocument', event => this._onFrameNavigatedWithinDocument(event.frameId, event.url));
//    this._client.on('Page.frameDetached', event => this._onFrameDetached(event.frameId));
//    this._client.on('Page.frameStoppedLoading', event => this._onFrameStoppedLoading(event.frameId));
//    this._client.on('Runtime.executionContextCreated', event => this._onExecutionContextCreated(event.context));
//    this._client.on('Runtime.executionContextDestroyed', event => this._onExecutionContextDestroyed(event.executionContextId));
//    this._client.on('Runtime.executionContextsCleared', event => this._onExecutionContextsCleared());
//    this._client.on('Page.lifecycleEvent', event => this._onLifecycleEvent(event));

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
