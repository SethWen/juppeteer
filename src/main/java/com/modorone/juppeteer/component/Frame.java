package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.cdp.CDPSession;

import java.util.List;

/**
 * author: Shawn
 * time  : 2/14/20 5:57 PM
 * desc  :
 * update: Shawn 2/14/20 5:57 PM
 */
public class Frame {

    private CDPSession mSession;
    private FrameManager mFrameManager;
    private Frame mParentFrame;
    private String mId;


    public Frame(CDPSession mSession, FrameManager mFrameManager, Frame parentFrame, String id) {
        mSession = mSession;
        mFrameManager = mFrameManager;
        mParentFrame = parentFrame;
        mId = id;
    }

    public void waitForNavigation(/*option*/) {

    }

    public void navigate(String url) {

    }

    public void click(/*selector, options = {}*/) {
    }

    public void tap(/*selector*/) {
    }

    public void type(/*selector, text, options*/) {
//        return this._mainWorld.type(selector, text, options);
    }

    public void focus(/*selector*/) {
//        return this._mainWorld.focus(selector);
    }

    public void hover(/*selector*/) {
    }

    private void detach() {
//        this._parentFrame._children.delete(this);
//        this._parentFrame = null;
//        this._detached = true;
//        this._mainWorld._detach();
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

    public String getContent() {
//        return this._mainWorld.content();
        return "";
    }

    public void setContent(String html) {
//        return this._mainWorld.setContent(html);
    }

    public void evaluate(/*pageFunction, ...args*/) {
//        return this._mainWorld.evaluate(pageFunction, ...args);
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

    public String getTitle() {
        return "";
    }

    public String getName() {
        return "";
    }

    public boolean isDetached() {
        return false;
    }

    public List<Frame> getChildFrames() {
        return null;
    }

    public Frame getParentFrame() {
        return null;
    }

    public String getUrl() {
        return "";
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

        void onFrameAttached(JSONObject json);

        void onFrameNavigated();

        void onFrameNavigatedWithinDocument();

        void onFrameDetached();

        void onFrameStoppedLoading();

        void onExecutionContextCreated();

        void onExecutionContextDestroyed();

        void onExecutionContextsCleared();

        void onLifecycleEvent();
    }
}
