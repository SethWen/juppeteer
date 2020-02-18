package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.cdp.BlockingCell;
import com.modorone.juppeteer.exception.JuppeteerException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
    private BlockingCell<JSONObject> mContextWaiter;


    public DomWorld(FrameManager frameManager, Frame frame, String timeoutSetting) {
        mFrameManager = frameManager;
        mFrame = frame;
        mTimeoutSetting = timeoutSetting;

        setContext(null);
    }

    public void setContext(JSONObject context) {
        if (Objects.nonNull(context)) {
            mContextWaiter.set(context);
            for (WaitTask waitTask : mWaitTasks) {
                waitTask.rerun();
            }
        } else {
            mDocumentWaiter = null;
            mContextWaiter = new BlockingCell<>();
        }
    }

    public BlockingCell<JSONObject> getContext() {
        if (mDetached) {
            throw new JuppeteerException("Execution Context is not available in detached frame " + mFrame.getUrl()
                    + " (are you trying to evaluate?)");
        }
        return mContextWaiter;
    }

    public Frame getFrame() {
        return mFrame;
    }

    private boolean hasContext() {
        return Objects.isNull(mContextWaiter);
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

        public void terminate() {

        }

        public void rerun() {

        }

    }
}
