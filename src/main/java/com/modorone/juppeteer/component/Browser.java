package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.IRunner;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.cdp.Connection;
import com.modorone.juppeteer.exception.IllegalTargetException;
import com.modorone.juppeteer.cdp.BrowserDomain;
import com.modorone.juppeteer.cdp.TargetDomain;
import com.modorone.juppeteer.exception.JuppeteerException;
import com.modorone.juppeteer.pojo.BrowserVersion;
import com.modorone.juppeteer.util.StringUtil;
import com.modorone.juppeteer.util.SystemUtil;

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

    private IRunner mRunner;
    private Connection mConnection;

    private final Set<String> mContexts = new HashSet<>();
    private final Map<String, Target> mTargets = new HashMap<>();
    private final Target.TargetListener mTargetListener = new Target.TargetListener() {

        @Override
        public void onCreate(Target.TargetInfo targetInfo, Supplier<CDPSession> sessionSupplier) {
            Target target = Target.create(Browser.this, targetInfo, sessionSupplier);
            mTargets.put(targetInfo.getTargetId(), target);

            if (target.getInitWaiter().uninterruptibleGet()) {
                // TODO: 2/23/20
            }
//
//            if (await target._initializedPromise) {
//                this.emit(Events.Browser.TargetCreated, target);
//                context.emit(Events.BrowserContext.TargetCreated, target);
//            }
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
            Target target = mTargets.remove(targetId);

            target.getCloseWaiter().setIfUnset(true);
            if (target.getInitWaiter().uninterruptibleGet()) {
                // TODO: 2/23/20
            }
        }
    };


    public static Browser create(IRunner runner, Connection connection) throws TimeoutException {
        Browser browser = new Browser(runner, connection);
        connection.doCall(TargetDomain.setDiscoverTargetsCommand, new JSONObject() {{
            put("discover", true);
        }});
        return browser;
    }


    public Browser(IRunner runner, Connection connection) {
        mRunner = runner;
        mConnection = connection;
        mConnection.setTargetListener(mTargetListener);
    }

    public Page newPage() throws Exception {
        return newPageInContext(null);
    }

    public Page newIncognitoPage() throws Exception {
        JSONObject json = mConnection.doCall(TargetDomain.createBrowserContextCommand);
        String contextId = json.getJSONObject("result").getString("browserContextId");
        mContexts.add(contextId);
        return newPageInContext(contextId);
    }

    private Page newPageInContext(String contextId) throws Exception {
        JSONObject json = mConnection.doCall(TargetDomain.createTargetCommand, new JSONObject() {{
            put("url", "about:blank");
            put("browserContextId", contextId);
        }});
        String targetId = json.getJSONObject("result").getString("targetId");
        Target target = mTargets.get(targetId);
        if (!target.getInitWaiter().uninterruptibleGet()) {
            throw new JuppeteerException("Failed to create target for page");
        }
        return mTargets.get(targetId).getPage();
    }

    public String getWSEndPoint() {
        return mConnection.getUrl();
    }

    public IRunner getRunner() {
        return mRunner;
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
                } catch (Exception e) {
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

    public void close() throws Exception {
        mConnection.doCall(BrowserDomain.closeCommand);
        mConnection.close();

        if (Objects.nonNull(mRunner)) mRunner.terminate();
    }

    public boolean isAlive() {
        return mConnection.isAlive();
    }
}
