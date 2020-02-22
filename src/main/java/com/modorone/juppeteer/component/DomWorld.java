package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.util.BlockingCell;
import com.modorone.juppeteer.exception.ElementNotFoundException;
import com.modorone.juppeteer.exception.JuppeteerException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeoutException;

/**
 * author: Shawn
 * time  : 2/18/20 7:04 PM
 * desc  :
 * update: Shawn 2/18/20 7:04 PM
 */
public class DomWorld {

    private FrameManager mFrameManager;
    private Frame mFrame;
    private String mTimeoutSetting;

    private boolean mDetached;
    private Set<WaitTask> mWaitTasks = new HashSet<>();

    private JSHandle.ElementHandle mDocument;
    private BlockingCell<ExecutionContext> mContextWaiter;
    private boolean mHasContext = false;

//    constructor(frameManager, frame, timeoutSettings) {
//        this._frameManager = frameManager;
//        this._frame = frame;
//        this._timeoutSettings = timeoutSettings;
//
//        /** @type {?Promise<!Puppeteer.ElementHandle>} */
//        this._documentPromise = null;
//        /** @type {!Promise<!Puppeteer.ExecutionContext>} */
//        this._contextPromise;
//        this._contextResolveCallback = null;
//        this._setContext(null);
//
//        /** @type {!Set<!WaitTask>} */
//        this._waitTasks = new Set();
//        this._detached = false;
//    }


    public DomWorld(FrameManager frameManager, Frame frame, String timeoutSetting) {
        mFrameManager = frameManager;
        mFrame = frame;
        mTimeoutSetting = timeoutSetting;

        setContext(null);
    }

    public void setContext(ExecutionContext context) {
        if (Objects.nonNull(context)) {
            mContextWaiter.set(context);
            mHasContext = true;
            for (WaitTask waitTask : mWaitTasks) {
                waitTask.rerun();
            }
        } else {
            mDocument = null;
            mContextWaiter = new BlockingCell<>();
            mHasContext = false;
        }
    }

    public BlockingCell<ExecutionContext> getContextWaiter() {
        if (mDetached) {
            throw new JuppeteerException("Execution Context is not available in detached frame " + mFrame.getUrl()
                    + " (are you trying to evaluate?)");
        }
        return mContextWaiter;
    }

    public Frame getFrame() {
        return mFrame;
    }

    private JSHandle.ElementHandle geDocument() throws InterruptedException, TimeoutException {
        if (Objects.nonNull(mDocument)) return mDocument;
        JSHandle.ElementHandle document = (JSHandle.ElementHandle) getContextWaiter().get().evaluateCodeBlock4Handle("document");
        mDocument = document;
        return document;
    }

    public boolean hasContext() {
        return mHasContext;
    }

    public void detach() {
        mDetached = true;
        for (WaitTask waitTask : mWaitTasks) {
            waitTask.terminate();
        }

//        this._detached = true;
//        for (const waitTask of this._waitTasks)
//        waitTask.terminate(new Error('waitForFunction failed: frame got detached.'));
    }

    public Object evaluateCodeBlock4Value(String pageFunction) throws InterruptedException, TimeoutException {
        return getContextWaiter().get().evaluateCodeBlock4Value(pageFunction);
    }

    public Object evaluateCodeBlock4Handle(String pageFunction) throws InterruptedException, TimeoutException {
        return getContextWaiter().get().evaluateCodeBlock4Handle(pageFunction);
    }

    public Object evaluateFunction4Value(String pageFunction, Object... args) throws InterruptedException, TimeoutException {
        return getContextWaiter().get().evaluateFunction4Value(pageFunction, args);
    }

    public Object evaluateFunction4Handle(String pageFunction, Object... args) throws InterruptedException, TimeoutException {
        return getContextWaiter().get().evaluateFunction4Handle(pageFunction, args);
    }

    public String getTitle() throws TimeoutException, InterruptedException {
        return (String) evaluateFunction4Value("() => document.title");
    }

    public String getContent() throws TimeoutException, InterruptedException {
        return (String) evaluateFunction4Value("() => {\n" +
                "    let retVal = '';\n" +
                "    if (document.doctype)\n" +
                "        retVal = new XMLSerializer().serializeToString(document.doctype);\n" +
                "    if (document.documentElement)\n" +
                "        retVal += document.documentElement.outerHTML;\n" +
                "    return retVal;\n" +
                "}");
    }

    public void hover(String selector) throws TimeoutException, InterruptedException, ElementNotFoundException {
        JSHandle.ElementHandle elem = $(selector);
        if (Objects.isNull(elem)) throw new ElementNotFoundException("No node found for selector: " + selector);
        elem.hover();
        elem.dispose();
    }

    public void click(String selector, JSONObject options) throws TimeoutException, InterruptedException, ElementNotFoundException {
        JSHandle.ElementHandle elem = $(selector);
        if (Objects.isNull(elem)) throw new ElementNotFoundException("No node found for selector: " + selector);
        elem.click(options);
        elem.dispose();
    }

    public void type(String selector, String text, JSONObject options) throws TimeoutException, InterruptedException, ElementNotFoundException {
        JSHandle.ElementHandle elem = $(selector);
        if (Objects.isNull(elem)) throw new ElementNotFoundException("No node found for selector: " + selector);
        elem.type(text, options);
        elem.dispose();
    }

    public void tap(String selector) throws TimeoutException, InterruptedException, ElementNotFoundException {
        JSHandle.ElementHandle elem = $(selector);
        if (Objects.isNull(elem)) throw new ElementNotFoundException("No node found for selector: " + selector);
        elem.tap();
        elem.dispose();
    }

    public void press(String selector, String key, JSONObject options) throws TimeoutException, InterruptedException, ElementNotFoundException {
        JSHandle.ElementHandle elem = $(selector);
        if (Objects.isNull(elem)) throw new ElementNotFoundException("No node found for selector: " + selector);
        elem.press(key, options);
        elem.dispose();
    }

    public JSHandle.ElementHandle $(String selector) throws TimeoutException, InterruptedException {
        JSHandle.ElementHandle document = geDocument();
        return document.$(selector);
    }


    private static class WaitTask {

        private int mRunCount;
        private DomWorld mWorld;

        public WaitTask(DomWorld world, int timeout) {
            mWorld = world;
        }

        public void terminate() {

        }

        public void rerun() {
            int runCount = ++mRunCount;
            boolean success;
            String error;
            try {
                Object o = mWorld.getContextWaiter().get().evaluateCodeBlock4Handle("");
            } catch (TimeoutException | InterruptedException e) {
                e.printStackTrace();
            }
// const runCount = ++this._runCount;
//            /** @type {?Puppeteer.JSHandle} */
//            let success = null;
//            let error = null;
//            try {
//                success = await (await this._domWorld.executionContext()).evaluateHandle(waitForPredicatePageFunction, this._predicateBody, this._polling, this._timeout, ...this._args);
//            } catch (e) {
//                error = e;
//            }
//
//            if (this._terminated || runCount !== this._runCount) {
//                if (success)
//                    await success.dispose();
//                return;
//            }
//
//            // Ignore timeouts in pageScript - we track timeouts ourselves.
//            // If the frame's execution context has already changed, `frame.evaluate` will
//            // throw an error - ignore this predicate run altogether.
//            if (!error && await this._domWorld.evaluate(s => !s, success).catch(e => true)) {
//                await success.dispose();
//                return;
//            }
//
//            // When the page is navigated, the promise is rejected.
//            // We will try again in the new execution context.
//            if (error && error.message.includes('Execution context was destroyed'))
//                return;
//
//            // We could have tried to evaluate in a context which was already
//            // destroyed.
//            if (error && error.message.includes('Cannot find context with specified id'))
//                return;
//
//            if (error)
//                this._reject(error);
//            else
//                this._resolve(success);
//
//            this._cleanup();
        }

    }
}
