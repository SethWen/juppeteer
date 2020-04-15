package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.CommandOptions;
import com.modorone.juppeteer.Constants;
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

    private ElementHandle geDocument() throws TimeoutException {
        if (Objects.nonNull(mDocument)) return mDocument;
        ElementHandle document = (ElementHandle) getContextWaiter().uninterruptibleGet().evaluateCodeBlock4Handle("document");
        mDocument = document;
        return document;
    }

    public Set<WaitTask> getWaitTasks() {
        return mWaitTasks;
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

    public Object evaluateCodeBlock4Value(String jsCodeBlock) throws TimeoutException {
        return getContextWaiter().uninterruptibleGet().evaluateCodeBlock4Value(jsCodeBlock);
    }

    public JSHandle evaluateCodeBlock4Handle(String jsCodeBlock) throws TimeoutException {
        return getContextWaiter().uninterruptibleGet().evaluateCodeBlock4Handle(jsCodeBlock);
    }

    public Object evaluateFunction4Value(String pageFunction, Object... args) throws TimeoutException {
        return getContextWaiter().uninterruptibleGet().evaluateFunction4Value(pageFunction, args);
    }

    public JSHandle evaluateFunction4Handle(String pageFunction, Object... args) throws TimeoutException {
        return getContextWaiter().uninterruptibleGet().evaluateFunction4Handle(pageFunction, args);
    }

    public String getTitle() throws TimeoutException {
        return (String) evaluateFunction4Value("() => document.title");
    }

    public String getContent() throws TimeoutException {
        return (String) evaluateFunction4Value("() => {\n" +
                "    let retVal = '';\n" +
                "    if (document.doctype)\n" +
                "        retVal = new XMLSerializer().serializeToString(document.doctype);\n" +
                "    if (document.documentElement)\n" +
                "        retVal += document.documentElement.outerHTML;\n" +
                "    return retVal;\n" +
                "}");
    }

    public void setContent(String html, CommandOptions options) throws TimeoutException {
        evaluateFunction4Value("html => {\n" +
                "    document.open();\n" +
                "    document.write(html);\n" +
                "    document.close();\n" +
                "}", html);
        LifecycleWatcher watcher = new LifecycleWatcher(mFrame, options.getWaitUntil());
        watcher.getLifecycleWaiter().uninterruptibleGet(options.getTimeout());
        watcher.dispose();
    }

    public void hover(String selector) throws TimeoutException, ElementNotFoundException {
        ElementHandle elem = $(selector);
        if (Objects.isNull(elem)) throw new ElementNotFoundException("No node found for selector: " + selector);
        elem.hover();
        elem.dispose();
    }

    public void focus(String selector) throws TimeoutException, ElementNotFoundException {
        ElementHandle elem = $(selector);
        if (Objects.isNull(elem)) throw new ElementNotFoundException("No node found for selector: " + selector);
        elem.focus();
        elem.dispose();
    }

    public void click(String selector, JSONObject options) throws TimeoutException, ElementNotFoundException {
        ElementHandle elem = $(selector);
        if (Objects.isNull(elem)) throw new ElementNotFoundException("No node found for selector: " + selector);
        elem.click(options);
        elem.dispose();
    }

    public void type(String selector, String text, JSONObject options) throws TimeoutException, ElementNotFoundException {
        ElementHandle elem = $(selector);
        if (Objects.isNull(elem)) throw new ElementNotFoundException("No node found for selector: " + selector);
        elem.type(text, options);
        elem.dispose();
    }

    public void tap(String selector) throws TimeoutException, ElementNotFoundException {
        ElementHandle elem = $(selector);
        if (Objects.isNull(elem)) throw new ElementNotFoundException("No node found for selector: " + selector);
        elem.tap();
        elem.dispose();
    }

    public void press(String selector, String key, JSONObject options) throws TimeoutException, ElementNotFoundException {
        ElementHandle elem = $(selector);
        if (Objects.isNull(elem)) throw new ElementNotFoundException("No node found for selector: " + selector);
        elem.press(key, options);
        elem.dispose();
    }

    public ElementHandle $(String selector) throws TimeoutException {
        ElementHandle document = geDocument();
        return document.$(selector);
    }

    public List<ElementHandle> $$(String selector) throws TimeoutException {
        ElementHandle document = geDocument();
        return document.$$(selector);
    }

    public Object $eval(String selector, String pageFunction, Object... args) throws TimeoutException {
        ElementHandle document = geDocument();
        return document.$eval(selector, pageFunction, args);
    }

    public Object $$eval(String selector, String pageFunction, Object... args) throws TimeoutException {
        ElementHandle document = geDocument();
        return document.$$eval(selector, pageFunction, args);
    }

    public List<ElementHandle> $x(String expression) throws TimeoutException {
        ElementHandle document = geDocument();
        return document.$x(expression);
    }

    public JSHandle addScriptTag(HtmlTag scriptTag) throws TimeoutException, JuppeteerException {
        // {url|path|content}/type
        if (StringUtil.nonEmpty(scriptTag.getUrl())) {
            try {
                return getContextWaiter().uninterruptibleGet().evaluateFunction4Handle("async(url, type) => {\n" +
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
            return getContextWaiter().uninterruptibleGet().evaluateFunction4Handle("(content, type = 'text/javascript') => {\n" +
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

    public JSHandle addStyleTag(HtmlTag styleTag) throws TimeoutException, JuppeteerException {
        // {url|path|content}
        if (StringUtil.nonEmpty(styleTag.getUrl())) {
            try {
                return getContextWaiter().uninterruptibleGet().evaluateFunction4Handle("async(url) => {\n" +
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
            return getContextWaiter().uninterruptibleGet().evaluateFunction4Handle("async(content) => {\n" +
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

    public JSHandle waitForFunction(String function, CommandOptions options, Object... args) throws TimeoutException {
        String polling = "raf";
        long timeout = options.getTimeout();
        return new WaitTask(this, function, "function", polling, timeout, args).getResult();
    }

    public ElementHandle waitForSelector(String selector, CommandOptions options) throws TimeoutException {
        return waitForSelectorOrXPath(selector, false, options);
    }

    public ElementHandle waitForXPath(String selector, CommandOptions options) throws TimeoutException {
        return waitForSelectorOrXPath(selector, true, options);
    }

    private ElementHandle waitForSelectorOrXPath(String selectorOrXPath, boolean isXPath, CommandOptions options) throws TimeoutException {
        boolean waitForVisible = options.isVisible();
        boolean waitForHidden = options.isHidden();
        long timeout = options.getTimeout();
        String polling = waitForVisible || waitForHidden ? "raf" : "mutation";
        String title = (isXPath ? "XPath" : "selector") + "'" + selectorOrXPath + "'" + (waitForHidden ? "to be hidden" : "");
        String predicate = Constants.PREDICATE_FUNCTION;
        WaitTask waitTask = new WaitTask(this, predicate, title, polling, timeout, selectorOrXPath, isXPath, waitForVisible, waitForHidden);
        JSHandle handle = waitTask.getResult();
        if (Objects.isNull(handle)) return null;

        if (Objects.isNull(handle.asElement())) {
            handle.dispose();
            return null;
        }

        return (ElementHandle) handle.asElement();
    }


    private static class WaitTask {

        private DomWorld mWorld;
        private String mFunction;
        private String mTitle;
        private int mRunCount;
        private String mPolling;
        private long mTimeout;
        private Object[] mArgs;
        private BlockingCell<JSHandle> mWaiter;
        private boolean mTerminated;

        public WaitTask(DomWorld world, String function, String title, String polling, long timeout, Object... args) {
            mWorld = world;
            mFunction = "return (" + function + ")(...args)";
            mTitle = title;
            mPolling = polling;
            mTimeout = timeout;
            mArgs = args;
            mRunCount = 0;
            mWorld.getWaitTasks().add(this);
            mWaiter = new BlockingCell<>();
            rerun();
        }

        public void terminate() {
            mTerminated = true;
            mWaiter.setIfUnset(null);
            cleanup();
        }

        private void cleanup() {
            mWorld.getWaitTasks().remove(this);
        }

        public void rerun() {
            int runCount = ++mRunCount;
            JSHandle success = null;
            Exception error = null;
            try {
                ExecutionContext context = mWorld.getContextWaiter().get();
                success = context.evaluateFunction4Handle(Constants.WAIT_FUNCTION, mFunction, mPolling, mTimeout, mArgs);
            } catch (Exception e) {
                error = e;
            }

            if (mTerminated || runCount != mRunCount) {
                if (Objects.nonNull(success)) {
                    success.dispose();
                    return;
                }
            }

            // Ignore timeouts in pageScript - we track timeouts ourselves.
            // If the frame's execution context has already changed, `frame.evaluate` will
            // throw an error - ignore this predicate run altogether.
            if (Objects.isNull(error)) {
                boolean flag;
                try {
                    flag = (boolean) mWorld.evaluateFunction4Value("s => !s", success);
                } catch (Exception e) {
                    flag = true;
                }

                if (flag) {
                    success.dispose();
                    return;
                }
            }

            // When the page is navigated, the promise is rejected.
            // We will try again in the new execution context.
            if (Objects.nonNull(error) && StringUtil.contains(error.getMessage(), "Execution context was destroyed"))
                return;

            // We could have tried to evaluate in a context which was already
            // destroyed.
            if (Objects.nonNull(error) && StringUtil.contains(error.getMessage(), "Cannot find context with specified id"))
                return;

            if (Objects.nonNull(error))
                mWaiter.setIfUnset(null);
            else
                mWaiter.setIfUnset(success);

            cleanup();
        }

        public JSHandle getResult() throws TimeoutException {
            try {
                return mWaiter.uninterruptibleGet(mTimeout);
            } catch (TimeoutException e) {
                terminate();
                throw new TimeoutException("waiting for " + mTitle + "failed: timeout " + mTimeout + "ms exceeded");
            }
        }
    }
}
