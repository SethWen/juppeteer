package com.modorone.juppeteer.component;

import com.modorone.juppeteer.cdp.BlockingCell;
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

    private BlockingCell<String> mDocumentWaiter;
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
            mDocumentWaiter = null;
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

    public boolean hasContext() {
        return mHasContext;
    }

    private void detach() {
        mDetached = true;
        for (WaitTask waitTask : mWaitTasks) {
            waitTask.terminate();
        }

//        this._detached = true;
//        for (const waitTask of this._waitTasks)
//        waitTask.terminate(new Error('waitForFunction failed: frame got detached.'));
    }

    public Object evaluateHandle(String pageFunction) throws InterruptedException, TimeoutException {
        return getContextWaiter().get().evaluateHandle(pageFunction);
    }

    public Object evaluate(String pageFunction) throws InterruptedException, TimeoutException {
        return getContextWaiter().get().evaluate(pageFunction);
    }


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
                Object o = mWorld.getContextWaiter().get().evaluateHandle("");
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
