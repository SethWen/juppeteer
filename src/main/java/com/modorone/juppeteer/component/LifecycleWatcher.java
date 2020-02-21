package com.modorone.juppeteer.component;

import com.modorone.juppeteer.WaitUntil;
import com.modorone.juppeteer.util.BlockingCell;
import com.modorone.juppeteer.component.network.Request;
import com.modorone.juppeteer.component.network.Response;
import com.modorone.juppeteer.util.StringUtil;

import java.util.Objects;

/**
 * author: Shawn
 * time  : 2/18/20 12:37 PM
 * desc  :
 * update: Shawn 2/18/20 12:37 PM
 */
public class LifecycleWatcher {

    private FrameManager mFrameManager;
    private Frame mFrame;
    private int mTimeout;
    private String mInitialLoaderId;
    private Request mNavigationRequest;
    private WaitUntil mWaitUntil;
    private boolean mHasSameDocumentNavigation;

    private BlockingCell<Boolean> mSameDocumentNavigationWaiter = new BlockingCell<>();
    private BlockingCell<Boolean> mLifecycleWaiter = new BlockingCell<>();
    private BlockingCell<Boolean> mNewDocumentNavigationWaiter = new BlockingCell<>();
    private BlockingCell<Boolean> mTerminationWaiter = new BlockingCell<>();

    private LifecycleListener mLifeCycleListener = new LifecycleListener() {
        @Override
        public void onTerminate() {
            mTerminationWaiter.set(true);
        }

        @Override
        public void onCheckLifecycleComplete() {
            // We expect navigation to commit.
            if (!checkLifecycle(mFrame, mWaitUntil)) return;

            mLifecycleWaiter.set(true);
            if (StringUtil.equals(mFrame.getFrameInfo().getLoaderId(), mInitialLoaderId)
                    && !mHasSameDocumentNavigation) return;

            if (mHasSameDocumentNavigation) mSameDocumentNavigationWaiter.set(true);

            if (!StringUtil.equals(mFrame.getFrameInfo().getLoaderId(), mInitialLoaderId))
                mNewDocumentNavigationWaiter.set(true);
        }

        @Override
        public void onNavigatedWithinDocument(Frame frame) {
            if (!Objects.equals(frame, mFrame)) return;

            mHasSameDocumentNavigation = true;
            onCheckLifecycleComplete();
        }

        @Override
        public void onFrameDetached(Frame frame) {
            if (Objects.equals(mFrame, frame)) {
                mTerminationWaiter.set(true);
                return;
            }
            onCheckLifecycleComplete();
        }

        @Override
        public void onRequest(Request request) {
            if (!Objects.equals(mFrame, request.getFrame()) && !request.isNavigationRequest()) return;
            mNavigationRequest = request;
        }

        public boolean checkLifecycle(Frame frame, WaitUntil waitUntil) {
            if (Objects.isNull(waitUntil) || !frame.getLifecycleEvents().contains(waitUntil.getValue()))
                return false;

            for (Frame childFrame : frame.getChildFrames()) {
                if (!checkLifecycle(childFrame, waitUntil)) return false;
            }

            return true;
        }
    };

    public LifecycleWatcher(Frame frame, WaitUntil waitUntil) {
        mFrameManager = frame.getFrameManager();
        mFrame = frame;
        mWaitUntil = waitUntil;
        mInitialLoaderId = frame.getFrameInfo().getLoaderId();

        mFrameManager.addLifecycleListener(mLifeCycleListener);
        mLifeCycleListener.onCheckLifecycleComplete();
    }

    public BlockingCell<Boolean> getSameDocumentNavigationWaiter() {
        return mSameDocumentNavigationWaiter;
    }

    public BlockingCell<Boolean> getLifecycleWaiter() {
        return mLifecycleWaiter;
    }

    public BlockingCell<Boolean> getNewDocumentNavigationWaiter() {
        return mNewDocumentNavigationWaiter;
    }

    public BlockingCell<Boolean> getTerminationWaiter() {
        return mTerminationWaiter;
    }

    public Response navigationResponse() {
        return Objects.nonNull(mNavigationRequest) ? mNavigationRequest.getResponse() : null;
    }

    public void dispose() {
        mFrameManager.removeLifecycleListener(mLifeCycleListener);
        mSameDocumentNavigationWaiter = null;
        mLifecycleWaiter = null;
        mNewDocumentNavigationWaiter = null;
        mTerminationWaiter = null;
    }

    public interface LifecycleListener {
        void onTerminate();

        void onCheckLifecycleComplete();

        void onNavigatedWithinDocument(Frame frame);

        void onFrameDetached(Frame frame);

        void onRequest(Request request);
    }

}
