package com.modorone.juppeteer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.cdp.Connection;
import com.modorone.juppeteer.component.Page;
import com.modorone.juppeteer.component.Target;
import com.modorone.juppeteer.exception.IllegalTargetException;
import com.modorone.juppeteer.protocol.BrowserDomain;
import com.modorone.juppeteer.protocol.TargetDomain;
import com.modorone.juppeteer.util.StringUtil;
import com.modorone.juppeteer.util.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.modorone.juppeteer.Constants.INFINITY;

/**
 * author: Shawn
 * time  : 1/13/20 2:50 PM
 * desc  :
 * update: Shawn 1/13/20 2:50 PM
 */
public class Browser {

    private static final Logger logger = LoggerFactory.getLogger(Browser.class);
    private Process mProcess;
    private Connection mConnection;

    private final Set<String> mContexts = new HashSet<>();
    private final Map<String, Target> mTargets = new HashMap<>();
    private final Target.TargetListener mTargetListener = new Target.TargetListener() {

        @Override
        public void onCreate(Target.TargetInfo targetInfo, Supplier<CDPSession> sessionSupplier) {
            Target target = Target.create(Browser.this, targetInfo, sessionSupplier);
            mTargets.put(targetInfo.getTargetId(), target);
            logger.debug("onCreate: targets={}", mTargets);
        }

        @Override
        public void onChange(Target.TargetInfo targetInfo) {
            Target target = mTargets.get(targetInfo.getTargetId());
            if (Objects.isNull(target)) {
                throw new IllegalTargetException("no valid target which id is " + targetInfo.getTargetId());
            }
            target.setTargetInfo(targetInfo);
            // TODO: 2/16/20 添加 browser listener？
        }

        @Override
        public void onDestroy(String targetId) {
            mTargets.remove(targetId);
        }
    };


    public static Browser create(Process process, Connection connection) throws TimeoutException {
        Browser browser = new Browser(process, connection);
        connection.doCall(TargetDomain.setDiscoverTargetsCommand, new JSONObject() {{
            put("discover", true);
        }});
        return browser;
    }

    public Browser(Process process, Connection connection) {
        mProcess = process;
        mConnection = connection;
        mConnection.setTargetListener(mTargetListener);
    }

    public Page newPage() throws TimeoutException {
        return newPageInContext(null);
    }

    public Page newIncognitoPage() throws TimeoutException {
        JSONObject json = mConnection.doCall(TargetDomain.createBrowserContextCommand);
        String contextId = json.getJSONObject("result").getString("browserContextId");
        mContexts.add(contextId);
        return newPageInContext(contextId);
    }

    private Page newPageInContext(String contextId) throws TimeoutException {
        JSONObject json = mConnection.doCall(TargetDomain.createTargetCommand, new JSONObject() {{
            put("url", "about:blank");
            put("browserContextId", contextId);
        }});
        String targetId = json.getJSONObject("result").getString("targetId");
        return mTargets.get(targetId).getPage();
    }

    public String getWSEndPoint() {
        return mConnection.getUrl();
    }

    public Process getProcess() {
        return mProcess;
    }

    public Set<String> getContexts() {
        return mContexts;
    }

    public List<Target> getTargets() {
        List<Target> targets = new ArrayList<>();
        mTargets.forEach((targetId, target) -> targets.add(target));
        return targets;
    }

    public Target getTarget() {
        Set<Map.Entry<String, Target>> entries = mTargets.entrySet();
        for (Map.Entry<String, Target> entry : entries) {
            if (StringUtil.equals("browser", entry.getValue().getTargetInfo().getType())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public List<Page> getPages() {
        List<Page> pages = new ArrayList<>();
        mTargets.forEach((targetId, target) -> {
            if (Objects.nonNull(target) && StringUtil.equals("page", target.getTargetInfo().getType())) {
                try {
                    pages.add(target.getPage());
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        });
        return pages;
    }

    public boolean disposeContext(String contextId) throws TimeoutException {
        mConnection.doCall(TargetDomain.disposeBrowserContextCommand, new JSONObject() {{
            put("browserContextId", contextId);
        }});
        return mContexts.remove(contextId);
    }

    public BrowserVersion getVersion() throws TimeoutException {
        JSONObject json = mConnection.doCall(BrowserDomain.getVersionCommand);
        return JSON.parseObject(json.getJSONObject("result").toJSONString(), BrowserVersion.class);
    }

    public String getUserAgent() throws TimeoutException {
        JSONObject json = mConnection.doCall(BrowserDomain.getVersionCommand);
        return json.getJSONObject("result").getString("userAgent");
    }

    public Target waitForTarget(Predicate<Target.TargetInfo> predicate, long timeout) throws InterruptedException, TimeoutException {
        if (timeout == INFINITY) {
            return getTargetByPredicate(predicate, () -> true);
        }

        if (timeout < 0) throw new IllegalArgumentException("Timeout cannot be less than zero");

        long start = System.currentTimeMillis();
        return getTargetByPredicate(predicate, () -> System.currentTimeMillis() - start < timeout);
    }

    private Target getTargetByPredicate(Predicate<Target.TargetInfo> predicate, Supplier<Boolean> supplier) throws TimeoutException {
        do {
            Optional<Target.TargetInfo> first = mTargets.values().stream()
                    .map(Target::getTargetInfo)
                    .filter(predicate)
                    .findFirst();
            if (first.isPresent()) {
                return mTargets.get(first.get().getTargetId());
            } else {
                SystemUtil.sleep(100);
            }
        } while (supplier.get());

        throw new TimeoutException("timeout to wait for target");
    }

    public void close() throws TimeoutException {
        mConnection.doCall(BrowserDomain.closeCommand);
        mConnection.close();
        mProcess.destroy();
    }

    public boolean isAlive() {
        return mConnection.isAlive();
    }

    public static class BrowserVersion {

        /**
         * protocolVersion : 1.3
         * product : HeadlessChrome/80.0.3987.0
         * revision : @65d20b8e6b1e34d2687f4367477b92e89867c6f5
         * userAgent : Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) HeadlessChrome/80.0.3987.0 Safari/537.36
         * jsVersion : 8.0.426.1
         */

        @JSONField(name = "protocolVersion")
        private String protocolVersion;
        @JSONField(name = "product")
        private String product;
        @JSONField(name = "revision")
        private String revision;
        @JSONField(name = "userAgent")
        private String userAgent;
        @JSONField(name = "jsVersion")
        private String jsVersion;

        public String getProtocolVersion() {
            return protocolVersion;
        }

        public void setProtocolVersion(String protocolVersion) {
            this.protocolVersion = protocolVersion;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public String getRevision() {
            return revision;
        }

        public void setRevision(String revision) {
            this.revision = revision;
        }

        public String getUserAgent() {
            return userAgent;
        }

        public void setUserAgent(String userAgent) {
            this.userAgent = userAgent;
        }

        public String getJsVersion() {
            return jsVersion;
        }

        public void setJsVersion(String jsVersion) {
            this.jsVersion = jsVersion;
        }

        @Override
        public String toString() {
            return "BrowserVersion{" +
                    "protocolVersion='" + protocolVersion + '\'' +
                    ", product='" + product + '\'' +
                    ", revision='" + revision + '\'' +
                    ", userAgent='" + userAgent + '\'' +
                    ", jsVersion='" + jsVersion + '\'' +
                    '}';
        }
    }
}
