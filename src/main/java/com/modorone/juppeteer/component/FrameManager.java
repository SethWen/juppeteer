package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.CommandOptions;
import com.modorone.juppeteer.Constants;
import com.modorone.juppeteer.WaitUntil;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.component.network.NetworkManager;
import com.modorone.juppeteer.component.network.Response;
import com.modorone.juppeteer.exception.IllegalFrameException;
import com.modorone.juppeteer.exception.RequestException;
import com.modorone.juppeteer.cdp.PageDomain;
import com.modorone.juppeteer.cdp.RuntimeDomain;
import com.modorone.juppeteer.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

/**
 * author: Shawn
 * time  : 2/14/20 5:53 PM
 * desc  :
 * update: Shawn 2/14/20 5:53 PM
 */
public class FrameManager implements Frame.FrameListener {

    private static final Logger logger = LoggerFactory.getLogger(FrameManager.class);

    private static final String UTILITY_WORLD_NAME = "__juppeteer_utility_world__";

    private Map<String, Frame> mFrames = new ConcurrentHashMap<>();
    private Map<Integer, ExecutionContext> mContexts = new ConcurrentHashMap<>();
    private CDPSession mSession;
    private Page mPage;
    private NetworkManager mNetworkManager;
    private Frame mMainFrame;
    private Set<LifecycleWatcher.LifecycleListener> mLifecycleListeners = ConcurrentHashMap.newKeySet();
    //    private Set<String> mIsolatedWorlds = Collections.newSetFromMap(new ConcurrentHashMap<>());
//    private Set<String> mIsolatedWorlds = ConcurrentHashMap.newKeySet();
    private Set<String> mIsolatedWorlds = new HashSet<>();


    public FrameManager(CDPSession session, Page page) {
        mSession = session;
        mPage = page;
        mNetworkManager = new NetworkManager(session, this, true); // TODO: 2/14/20 ignore https error
    }

    public void init() throws TimeoutException {
        mSession.setFrameListener(this);
        mSession.doCall(PageDomain.enableCommand);
        JSONObject json = mSession.doCall(PageDomain.getFrameTreeCommand).getJSONObject("result");
        handleFrameTree(json.getJSONObject("frameTree"));
        mSession.doCall(PageDomain.setLifecycleEventsEnabledCommand, new JSONObject() {{
            put("enabled", true);
        }});
        mSession.doCall(RuntimeDomain.enableCommand);
        ensureIsolatedWorld(UTILITY_WORLD_NAME);
        mNetworkManager.init();
    }

    public Page getPage() {
        return mPage;
    }

    public void addLifecycleListener(LifecycleWatcher.LifecycleListener listener) {
        if (Objects.nonNull(listener)) {
            mLifecycleListeners.add(listener);
            mNetworkManager.addRequestConsumer(listener::onRequest);
        }
    }

    public boolean removeLifecycleListener(LifecycleWatcher.LifecycleListener listener) {
        if (Objects.nonNull(listener)) {
            return mLifecycleListeners.remove(listener)
                    && mNetworkManager.removeRequestConsumer(listener::onRequest);
        }
        return false;
    }

    private void handleFrameTree(JSONObject frameTree) {
//        logger.debug("handleFrameTree: frameTree={}", frameTree);
        Frame.FrameInfo frameInfo = frameTree.getJSONObject("frame").toJavaObject(Frame.FrameInfo.class);
        if (!StringUtil.isEmpty(frameInfo.getParentId())) {
            onFrameAttached(frameInfo);
        }
        onFrameNavigated(frameInfo);

        if (Objects.isNull(frameTree.getJSONArray("childFrames"))) return;

        for (Object childFrame : frameTree.getJSONArray("childFrames")) {
            handleFrameTree((JSONObject) childFrame);
        }
    }

    private void ensureIsolatedWorld(String name) throws TimeoutException {
        if (mIsolatedWorlds.contains(name)) return;

        mIsolatedWorlds.add(name);
        mSession.doCall(PageDomain.addScriptToEvaluateOnNewDocumentCommand, new JSONObject() {{
            put("source", Constants.EVALUATION_SCRIPT_URL);
            put("worldName", name);
        }});
        mFrames.forEach((frameId, frame) -> {
            try {
                mSession.doCall(PageDomain.createIsolatedWorldCommand, new JSONObject() {{
                    put("frameId", frameId);
                    put("grantUniveralAccess", true);
                    put("worldName", name);
                }});
            } catch (TimeoutException ignore) {
            }
        });
    }


    public Frame getMainFrame() {
        return mMainFrame;
    }

    public Frame getFrame(String frameId) {
        if (Objects.isNull(frameId)) return null;
        return mFrames.get(frameId);
    }

    public List<Frame> getFrames() {
        List<Frame> frames = new ArrayList<>();
        mFrames.forEach((key, value) -> frames.add(value));
        return frames;
    }

    /**
     * @param frame
     * @param url
     * @param options referer/waitUntil/timeout
     * @throws RequestException
     */
    public Response navigateFrame(Frame frame, String url, CommandOptions options) throws RequestException {
        String referer = options.getReferer();
        if (StringUtil.isEmpty(referer) && Objects.nonNull(getNetworkManager().getExtraHTTPHeaders())) {
            referer = getNetworkManager().getExtraHTTPHeaders().get("referer");
        }
        WaitUntil waitUntil = options.getWaitUntil();
        if (Objects.isNull(waitUntil)) waitUntil = WaitUntil.LOAD;
        long timeout = options.getTimeout();

        LifecycleWatcher watcher = null;
        try {
            watcher = new LifecycleWatcher(frame, waitUntil);
            String finalReferer = referer;
            JSONObject result = mSession.doCall(PageDomain.navigateCommand, new JSONObject() {{
                put("url", url);
                put("referer", finalReferer);
                put("frameId", frame.getFrameInfo().getId());
            }}).getJSONObject("result");
            String errorText = result.getString("errorText");
            if (!StringUtil.isEmpty(errorText)) {
                throw new RequestException(errorText);
            }

            boolean ensureNewDocumentNavigation = StringUtil.nonEmpty(result.getString("loaderId"));
            if (ensureNewDocumentNavigation) {
                watcher.getNewDocumentNavigationWaiter().uninterruptibleGet(timeout);
            } else {
                watcher.getSameDocumentNavigationWaiter().uninterruptibleGet(timeout);
            }
            return watcher.navigationResponse();
        } catch (TimeoutException e) {
            throw new RequestException("request: " + url + " is timeout for " + timeout + "ms");
        } finally {
            if (Objects.nonNull(watcher)) watcher.dispose();
        }
    }

    @Override
    public void onFrameAttached(Frame.FrameInfo frameInfo) {
        if (Objects.nonNull(frameInfo.getId()) && mFrames.containsKey(frameInfo.getId())) return;

        if (StringUtil.isEmpty(frameInfo.getParentId()))
            throw new IllegalFrameException("parentFrameId is null");

        Frame frame = new Frame(this, mFrames.get(frameInfo.getParentId()), frameInfo);
        if (Objects.nonNull(frameInfo.getId())) mFrames.put(frameInfo.getId(), frame);

        // TODO: 2/14/20
//        this.emit(Events.FrameManager.FrameAttached, frame);
    }

    @Override
    public void onFrameNavigated(Frame.FrameInfo frameInfo) {
        boolean isMainFrame = StringUtil.isEmpty(frameInfo.getParentId());
        Frame frame = isMainFrame ? getMainFrame() : getFrame(frameInfo.getId());
        if (!isMainFrame && Objects.isNull(frame)) {
            throw new IllegalFrameException("We either navigate top level or have old version of the navigated frame");
        }

        // Detach all child frames first.
        if (Objects.nonNull(frame)) {
            for (Frame childFrame : frame.getChildFrames()) {
                removeFramesRecursively(childFrame);
            }
        }

        // Update or create main frame.
        if (isMainFrame) {
            if (Objects.nonNull(frame)) {
                // Update frame id to retain frame identity on cross-process navigation.
                if (Objects.nonNull(frame.getFrameInfo().getId())) mFrames.remove(frame.getFrameInfo().getId());
                frame.setFrameInfo(frameInfo);
            } else {
                // Initial main frame navigation.
                frame = new Frame(this, null, frameInfo);
            }
            if (Objects.nonNull(frameInfo.getId())) mFrames.put(frameInfo.getId(), frame);
            mMainFrame = frame;
        }
        // TODO: 2/16/20 listener
//        this.emit(Events.FrameManager.FrameNavigated, frame);
    }

    private void removeFramesRecursively(Frame frame) {
        for (Frame childFrame : frame.getChildFrames()) {
            removeFramesRecursively(childFrame);
        }
        frame.detach();
        if (Objects.nonNull(frame.getFrameInfo().getId())) mFrames.remove(frame.getFrameInfo().getId());
        // TODO: 2/16/20 listener
//        this.emit(Events.FrameManager.FrameDetached, frame);
    }

    @Override
    public void onFrameNavigatedWithinDocument(JSONObject event) {
        Frame frame = getFrame(event.getString("frameId"));
        if (Objects.isNull(frame)) return;

        frame.navigatedWithinDocument(event.getString("url"));

        // TODO: 2/21/20 check lifecycle
//        this.emit(Events.FrameManager.FrameNavigatedWithinDocument, frame);
//        this.emit(Events.FrameManager.FrameNavigated, frame);
        mLifecycleListeners.forEach(lifecycleListener -> lifecycleListener.onNavigatedWithinDocument(frame));
    }

    @Override
    public void onFrameDetached(JSONObject event) {
        Frame frame = getFrame(event.getString("frameId"));
        if (Objects.nonNull(frame)) removeFramesRecursively(frame);

        mLifecycleListeners.forEach(lifecycleListener -> lifecycleListener.onFrameDetached(frame));
    }

    @Override
    public void onFrameStoppedLoading(JSONObject event) {
        Frame frame = getFrame(event.getString("frameId"));
        if (Objects.nonNull(frame)) {
            frame.stopLoading();
//        this.emit(Events.FrameManager.LifecycleEvent, frame);
        }
    }

    @Override
    public void onExecutionContextCreated(JSONObject contextPayload) {
        // {"auxData":{"isDefault":true,"frameId":"4AA1B0CC31419133EBCE07FD94056608","type":"default"},"origin":"chrome-search://most-visited","name":"","id":4}
        String frameId = null;
        JSONObject auxData = contextPayload.getJSONObject("auxData");
        if (Objects.nonNull(auxData)) {
            frameId = auxData.getString("frameId");
        }
        Frame frame = getFrame(frameId);
        DomWorld world = null;
        if (Objects.nonNull(frame)) {
            if (Objects.nonNull(auxData) && auxData.getBoolean("isDefault")) {
                world = frame.getMainWorld();
            } else if (StringUtil.equals(UTILITY_WORLD_NAME, contextPayload.getString("name"))
                    && !frame.getSecondaryWorld().hasContext()) {
                // In case of multiple sessions to the same target, there's a race between
                // connections so we might end up creating multiple isolated worlds.
                // We can use either.
                world = frame.getSecondaryWorld();
            }
        }

        if (Objects.nonNull(auxData) && StringUtil.equals("isolated", auxData.getString("type"))) {
            mIsolatedWorlds.add(contextPayload.getString("name"));
        }

        ExecutionContext context = new ExecutionContext(mSession, world, contextPayload);
        // world = null??!! fixme
        if (Objects.nonNull(world)) world.setContext(context);
        mContexts.put(contextPayload.getInteger("id"), context);
    }

    @Override
    public void onExecutionContextDestroyed(int executionContextId) {
        ExecutionContext context = mContexts.get(executionContextId);
        if (Objects.isNull(context)) return;

        mContexts.remove(executionContextId);
        if (Objects.nonNull(context.getWorld())) context.getWorld().setContext(null);
    }

    @Override
    public void onExecutionContextsCleared() {
        mContexts.forEach((contextId, context) -> {
            if (Objects.nonNull(context.getWorld())) {
                context.getWorld().setContext(null);
            }
        });
        mContexts.clear();
    }

    @Override
    public void onLifecycleEvent(JSONObject event) {
        Frame frame = getFrame(event.getString("frameId"));
        if (Objects.isNull(frame)) return;

        frame.proceedLifecycle(event.getString("loaderId"), event.getString("name"));
        mLifecycleListeners.forEach(LifecycleWatcher.LifecycleListener::onCheckLifecycleComplete);
    }

    public NetworkManager getNetworkManager() {
        return mNetworkManager;
    }
}
