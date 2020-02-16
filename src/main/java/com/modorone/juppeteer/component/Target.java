package com.modorone.juppeteer.component;

import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

/**
 * author: Shawn
 * time  : 2/14/20 2:22 PM
 * desc  :
 * update: Shawn 2/14/20 2:22 PM
 */
public class Target {

    private static final Logger logger = LoggerFactory.getLogger(Target.class);

    private TargetInfo mTargetInfo;
    private Supplier<CDPSession> mSessionSupplier;

    public static Target create(TargetInfo targetInfo, Supplier<CDPSession> sessionSupplier) {
        return new Target(targetInfo, sessionSupplier);
    }

    private Target(TargetInfo targetInfo, Supplier<CDPSession> sessionSupplier) {
        mTargetInfo = targetInfo;
        mSessionSupplier = sessionSupplier;
    }

    private void init() {
        // TODO: 2/16/20 涉及到 openerId，暂时没发现该字段，暂时不实现
    }

    public Page getPage() throws TimeoutException {
        Page page = null;
        if (StringUtil.equals("page", mTargetInfo.getType())
                || StringUtil.equals("background_page", mTargetInfo.getType())) {
            CDPSession apply = mSessionSupplier.get();
            logger.debug("newPage: apply={}", apply);
            page = Page.create(apply);
        }
        return page;
    }

    public void setTargetInfo(TargetInfo targetInfo) {
        mTargetInfo = targetInfo;
    }

    public TargetInfo getTargetInfo() {
        return mTargetInfo;
    }

    public interface TargetListener {
        void onCreate(TargetInfo targetInfo, Supplier<CDPSession> sessionSupplier);

        void onChange(TargetInfo targetInfo);

        void onDestroy(String targetId);
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