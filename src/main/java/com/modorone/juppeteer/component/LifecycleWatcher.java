package com.modorone.juppeteer.component;

import com.modorone.juppeteer.component.network.Request;

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

    private LifecycleListener mLifeCycleListener = new LifecycleListener() {
        @Override
        public void onTerminate() {

        }

        @Override
        public void onCheckLifecycleComplete() {
//// We expect navigation to commit.
//            if (!checkLifecycle(this._frame, this._expectedLifecycle))
//                return;
//            this._lifecycleCallback();
//            if (this._frame._loaderId === this._initialLoaderId && !this._hasSameDocumentNavigation)
//                return;
//            if (this._hasSameDocumentNavigation)
//                this._sameDocumentNavigationCompleteCallback();
//            if (this._frame._loaderId !== this._initialLoaderId)
//                this._newDocumentNavigationCompleteCallback();
//
//            /**
//             * @param {!Puppeteer.Frame} frame
//             * @param {!Array<string>} expectedLifecycle
//             * @return {boolean}
//             */
//            function checkLifecycle(frame, expectedLifecycle) {
//                for (const event of expectedLifecycle) {
//                    if (!frame._lifecycleEvents.has(event))
//                        return false;
//                }
//                for (const child of frame.childFrames()) {
//                    if (!checkLifecycle(child, expectedLifecycle))
//                        return false;
//                }
//                return true;
//            }
        }

//        public boolean checkLifecycle(Frame frame, ) {
//
//        }

        @Override
        public void onNavigatedWithinDocument() {

        }

        @Override
        public void onFrameDetached() {

        }

        @Override
        public void onRequest() {

        }
    };

    // TODO: 2/18/20 add until and timeout
    public LifecycleWatcher(FrameManager frameManager, Frame frame) {
        mFrameManager = frameManager;
        mFrame = frame;
        mInitialLoaderId = frame.getFrameInfo().getLoaderId();
    }

    public interface LifecycleListener {
        void onTerminate();

        void onCheckLifecycleComplete();

        void onNavigatedWithinDocument();

        void onFrameDetached();

        void onRequest();
    }


}
