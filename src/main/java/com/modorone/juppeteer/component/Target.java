package com.modorone.juppeteer.component;

import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.util.StringUtil;

import java.util.concurrent.TimeoutException;

/**
 * author: Shawn
 * time  : 2/14/20 2:22 PM
 * desc  :
 * update: Shawn 2/14/20 2:22 PM
 */
public class Target {

    private TargetInfo mTargetInfo;
    private CDPSession mSession;

    public static Target create(TargetInfo targetInfo, CDPSession session) {
        return new Target(targetInfo, session);
    }

    private Target(TargetInfo targetInfo, CDPSession session) {
        mTargetInfo = targetInfo;
        mSession = session;
    }

    public Page newPage() throws TimeoutException {
        Page page = null;
        if (StringUtil.equals("page", mTargetInfo.getType())
                || StringUtil.equals("background_page", mTargetInfo.getType())) {
            page = Page.create(mSession);
        }
        return page;
    }


    public interface TargetListener {
        void onCreate(TargetInfo targetInfo);

        void onAttach(TargetInfo targetInfo, CDPSession session);

        void onChange(TargetInfo targetInfo);

        void onDetach(TargetInfo targetInfo, CDPSession session);

        void onDestroy(TargetInfo targetInfo);
    }

    public static class TargetInfo {
        private String targetId;
        private String type;
        private String title;
        private boolean attached;
        private String browserContextId;

        public String getTargetId() {
            return targetId;
        }

        public void setTargetId(String targetId) {
            this.targetId = targetId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isAttached() {
            return attached;
        }

        public void setAttached(boolean attached) {
            this.attached = attached;
        }

        public String getBrowserContextId() {
            return browserContextId;
        }

        public void setBrowserContextId(String browserContextId) {
            this.browserContextId = browserContextId;
        }

        @Override
        public String toString() {
            return "TargetInfo{" +
                    "targetId='" + targetId + '\'' +
                    ", type='" + type + '\'' +
                    ", title='" + title + '\'' +
                    ", attached=" + attached +
                    ", browserContextId='" + browserContextId + '\'' +
                    '}';
        }
    }
}