package com.modorone.juppeteer.component;

import com.modorone.juppeteer.Browser;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.util.BlockingCell;
import com.modorone.juppeteer.util.StringUtil;
import jdk.nashorn.internal.runtime.regexp.joni.constants.TargetInfo;
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

    private Browser mBrowser;
    private TargetInfo mTargetInfo;
    private Supplier<CDPSession> mSessionSupplier;

    private BlockingCell<Boolean> mInitWaiter = new BlockingCell<>();
    private BlockingCell<Boolean> mCloseWaiter = new BlockingCell<>();
    private boolean mIsInit;

    public static Target create(Browser browser, TargetInfo targetInfo, Supplier<CDPSession> sessionSupplier) {
        Target target = new Target(browser, targetInfo, sessionSupplier);
        target.init();
        return target;
    }

    private Target(Browser browser, TargetInfo targetInfo, Supplier<CDPSession> sessionSupplier) {
        mBrowser = browser;
        mTargetInfo = targetInfo;
        mSessionSupplier = sessionSupplier;
    }

    private void init() {
        // TODO: 2/16/20 涉及到 openerId，暂时没发现该字段，暂时不实现
        mIsInit = !StringUtil.equals("page", mTargetInfo.getType()) || !StringUtil.equals("", mTargetInfo.getUrl());
        System.out.println("target is init: " + mIsInit);
        if (mIsInit) mInitWaiter.setIfUnset(true);
    }

    public Browser getBrowser() {
        return mBrowser;
    }

    public Page getPage() throws TimeoutException {
        Page page = null;
        if (StringUtil.equals("page", mTargetInfo.getType())
                || StringUtil.equals("background_page", mTargetInfo.getType())) {
            CDPSession apply = mSessionSupplier.get();
            logger.debug("newPage: apply={}", apply);
            page = Page.create(apply, this);
        }
        return page;
    }

    public void setTargetInfo(TargetInfo targetInfo) {
        mTargetInfo = targetInfo;
    }

    public TargetInfo getTargetInfo() {
        return mTargetInfo;
    }

    public BlockingCell<Boolean> getInitWaiter() {
        return mInitWaiter;
    }

    public BlockingCell<Boolean> getCloseWaiter() {
        return mCloseWaiter;
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
        private String url;
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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
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