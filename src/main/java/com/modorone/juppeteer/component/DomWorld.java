package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.CommandOptions;
import com.modorone.juppeteer.pojo.HtmlTag;
import com.modorone.juppeteer.util.BlockingCell;
import com.modorone.juppeteer.exception.ElementNotFoundException;
import com.modorone.juppeteer.exception.JuppeteerException;
import com.modorone.juppeteer.util.StringUtil;

import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

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

    private ElementHandle mDocument;
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

    private ElementHandle geDocument() throws InterruptedException, TimeoutException {
        if (Objects.nonNull(mDocument)) return mDocument;
        ElementHandle document = (ElementHandle) getContextWaiter().get().evaluateCodeBlock4Handle("document");
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

    public JSHandle evaluateCodeBlock4Handle(String pageFunction) throws InterruptedException, TimeoutException {
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

    public void setContent(String html, CommandOptions options) throws TimeoutException, InterruptedException {
        evaluateFunction4Value("html => {\n" +
                "    document.open();\n" +
                "    document.write(html);\n" +
                "    document.close();\n" +
                "}", html);
        LifecycleWatcher watcher = new LifecycleWatcher(mFrame, options.getWaitUntil());
        watcher.getLifecycleWaiter().uninterruptibleGet(options.getTimeout());
        watcher.dispose();
    }

    public void hover(String selector) throws TimeoutException, InterruptedException, ElementNotFoundException {
        ElementHandle elem = $(selector);
        if (Objects.isNull(elem)) throw new ElementNotFoundException("No node found for selector: " + selector);
        elem.hover();
        elem.dispose();
    }

    public void click(String selector, JSONObject options) throws TimeoutException, InterruptedException, ElementNotFoundException {
        ElementHandle elem = $(selector);
        if (Objects.isNull(elem)) throw new ElementNotFoundException("No node found for selector: " + selector);
        elem.click(options);
        elem.dispose();
    }

    public void type(String selector, String text, JSONObject options) throws TimeoutException, InterruptedException, ElementNotFoundException {
        ElementHandle elem = $(selector);
        if (Objects.isNull(elem)) throw new ElementNotFoundException("No node found for selector: " + selector);
        elem.type(text, options);
        elem.dispose();
    }

    public void tap(String selector) throws TimeoutException, InterruptedException, ElementNotFoundException {
        ElementHandle elem = $(selector);
        if (Objects.isNull(elem)) throw new ElementNotFoundException("No node found for selector: " + selector);
        elem.tap();
        elem.dispose();
    }

    public void press(String selector, String key, JSONObject options) throws TimeoutException, InterruptedException, ElementNotFoundException {
        ElementHandle elem = $(selector);
        if (Objects.isNull(elem)) throw new ElementNotFoundException("No node found for selector: " + selector);
        elem.press(key, options);
        elem.dispose();
    }

    public ElementHandle $(String selector) throws TimeoutException, InterruptedException {
        ElementHandle document = geDocument();
        return document.$(selector);
    }

    public List<ElementHandle> $$(String selector) throws TimeoutException, InterruptedException {
        ElementHandle document = geDocument();
        return document.$$(selector);
    }

    public Object $eval(String selector, String pageFunction, Object... args) throws TimeoutException, InterruptedException {
        ElementHandle document = geDocument();
        return document.$eval(selector, pageFunction, args);
    }

    public Object $$eval(String selector, String pageFunction, Object... args) throws TimeoutException, InterruptedException {
        ElementHandle document = geDocument();
        return document.$$eval(selector, pageFunction, args);
    }

    public List<ElementHandle> $x(String expression) throws TimeoutException, InterruptedException {
        ElementHandle document = geDocument();
        return document.$x(expression);
    }

    public JSHandle addScriptTag(HtmlTag scriptTag) throws InterruptedException, TimeoutException {
        // {url|path|content}/type
        if (StringUtil.nonEmpty(scriptTag.getUrl())) {
            try {
                return getContextWaiter().get().evaluateFunction4Handle("async(url, type) => {\n" +
                        "    const script = document.createElement('script');\n" +
                        "    script.src = url;\n" +
                        "    if (type)\n" +
                        "        script.type = type;\n" +
                        "    const promise = new Promise((res, rej) => {\n" +
                        "        script.onload = res;\n" +
                        "        script.onerror = rej;\n" +
                        "    });\n" +
                        "    document.head.appendChild(script);\n" +
                        "    await promise;\n" +
                        "    return script;\n" +
                        "}", scriptTag.getUrl(), Objects.isNull(scriptTag.getType()) ? "" : scriptTag.getType());
            } catch (Exception e) {
                throw new JuppeteerException("Loading script from " + scriptTag.getUrl() + " failed");
            }
        }

        if (StringUtil.nonEmpty(scriptTag.getPath())) {
            // todo: read js from fs and inject it to browser runtime
        }

        if (StringUtil.nonEmpty(scriptTag.getContent())) {
            return getContextWaiter().get().evaluateFunction4Handle("(content, type = 'text/javascript') => {\n" +
                    "    const script = document.createElement('script');\n" +
                    "    script.type = type;\n" +
                    "    script.text = content;\n" +
                    "    let error = null;\n" +
                    "    script.onerror = e => error = e;\n" +
                    "    document.head.appendChild(script);\n" +
                    "    if (error)\n" +
                    "        throw error;\n" +
                    "    return script;\n" +
                    "}", scriptTag.getContent(), Objects.isNull(scriptTag.getType()) ? "" : scriptTag.getType());
        }

        throw new InvalidParameterException("Provide an object with a `url`, `path` or `content` property");
    }

    public JSHandle addStyleTag(HtmlTag styleTag) throws InterruptedException, TimeoutException {
        // {url|path|content}
        if (StringUtil.nonEmpty(styleTag.getUrl())) {
            try {
                return getContextWaiter().get().evaluateFunction4Handle("async(url) => {\n" +
                        "    const link = document.createElement('link');\n" +
                        "    link.rel = 'stylesheet';\n" +
                        "    link.href = url;\n" +
                        "    const promise = new Promise((res, rej) => {\n" +
                        "        link.onload = res;\n" +
                        "        link.onerror = rej;\n" +
                        "    });\n" +
                        "    document.head.appendChild(link);\n" +
                        "    await promise;\n" +
                        "    return link;\n" +
                        "}", styleTag.getUrl());
            } catch (Exception e) {
                throw new JuppeteerException("Loading style from " + styleTag.getUrl() + " failed");
            }
        }

        if (StringUtil.nonEmpty(styleTag.getPath())) {
            // todo: read style from fs and inject it to browser runtime
        }

        if (StringUtil.nonEmpty(styleTag.getContent())) {
            return getContextWaiter().get().evaluateFunction4Handle("async(content) => {\n" +
                    "    const style = document.createElement('style');\n" +
                    "    style.type = 'text/css';\n" +
                    "    style.appendChild(document.createTextNode(content));\n" +
                    "    const promise = new Promise((res, rej) => {\n" +
                    "        style.onload = res;\n" +
                    "        style.onerror = rej;\n" +
                    "    });\n" +
                    "    document.head.appendChild(style);\n" +
                    "    await promise;\n" +
                    "    return style;\n" +
                    "}", styleTag.getContent());
        }

        throw new InvalidParameterException("Provide an object with a `url`, `path` or `content` property");
    }

    public ElementHandle waitForSelector(String selector, CommandOptions options) {
        return null;
    }


    private Predicate<Boolean> predicate = (selectorOrXPath) -> {
//         const node = isXPath
//                    ? document.evaluate(selectorOrXPath, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue
//                    : document.querySelector(selectorOrXPath);
//            if (!node)
//                return waitForHidden;
//            if (!waitForVisible && !waitForHidden)
//                return node;
//      const element = /** @type {Element} */ (node.nodeType === Node.TEXT_NODE ? node.parentElement : node);
//
//      const style = window.getComputedStyle(element);
//      const isVisible = style && style.visibility !== 'hidden' && hasVisibleBoundingBox();
//      const success = (waitForVisible === isVisible || waitForHidden === !isVisible);
//            return success ? node : null;
//
//            /**
//             * @return {boolean}
//             */
//            function hasVisibleBoundingBox() {
//        const rect = element.getBoundingClientRect();
//                return !!(rect.top || rect.bottom || rect.width || rect.height);
        return true;
    };

    /**
     * @param {string}     selectorOrXPath
     * @param {boolean}    isXPath
     * @param {!{visible?: boolean, hidden?: boolean, timeout?: number}=} options
     * @return {!Promise<?Puppeteer.ElementHandle>}
     */
    public ElementHandle waitForSelectorOrXPath(String selectorOrXPath, boolean isXPath, CommandOptions options) {
        boolean waitForVisible = options.isVisible();
        boolean waitForHidden = options.isHidden();
        long timeout = options.getTimeout();
        String polling = waitForVisible || waitForHidden ? "raf" : "mutation";
        String title = (isXPath ? "XPath" : "selector") + "'" + selectorOrXPath + "'" + (waitForHidden ? "to be hidden" : "");
//        WaitTask waitTask = new WaitTask(this, );

//        Predicate<Boolean> predicate = (selectorOrXPath, isXPath waitForVisible, waitForHidden) -> {
//            return true;
//        }

//    const {
//            visible: waitForVisible = false,
//                    hidden: waitForHidden = false,
//                    timeout = this._timeoutSettings.timeout(),
//        } = options;
//    const polling = waitForVisible || waitForHidden ? 'raf' : 'mutation';
//    const title = `${isXPath ? 'XPath' : 'selector'} "${selectorOrXPath}"${waitForHidden ? ' to be hidden' : ''}`;
//    const waitTask = new WaitTask(this, predicate, title, polling, timeout, selectorOrXPath, isXPath, waitForVisible, waitForHidden);
//    const handle = await waitTask.promise;
//        if (!handle.asElement()) {
//            await handle.dispose();
//            return null;
//        }
//        return handle.asElement();
//
//        /**
//         * @param {string} selectorOrXPath
//         * @param {boolean} isXPath
//         * @param {boolean} waitForVisible
//         * @param {boolean} waitForHidden
//         * @return {?Node|boolean}
//         */
//        function predicate(selectorOrXPath, isXPath, waitForVisible, waitForHidden) {
//      const node = isXPath
//                    ? document.evaluate(selectorOrXPath, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue
//                    : document.querySelector(selectorOrXPath);
//            if (!node)
//                return waitForHidden;
//            if (!waitForVisible && !waitForHidden)
//                return node;
//      const element = /** @type {Element} */ (node.nodeType === Node.TEXT_NODE ? node.parentElement : node);
//
//      const style = window.getComputedStyle(element);
//      const isVisible = style && style.visibility !== 'hidden' && hasVisibleBoundingBox();
//      const success = (waitForVisible === isVisible || waitForHidden === !isVisible);
//            return success ? node : null;
//
//            /**
//             * @return {boolean}
//             */
//            function hasVisibleBoundingBox() {
//        const rect = element.getBoundingClientRect();
//                return !!(rect.top || rect.bottom || rect.width || rect.height);
//            }
        return null;
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
