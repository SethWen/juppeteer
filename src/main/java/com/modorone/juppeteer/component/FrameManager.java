package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.component.network.NetworkManager;
import com.modorone.juppeteer.component.network.Response;
import com.modorone.juppeteer.exception.IllegalFrameException;
import com.modorone.juppeteer.protocol.PageDomain;
import com.modorone.juppeteer.protocol.RuntimeDomain;
import com.modorone.juppeteer.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.java2d.loops.XORComposite;

import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * author: Shawn
 * time  : 2/14/20 5:53 PM
 * desc  :
 * update: Shawn 2/14/20 5:53 PM
 */
public class FrameManager implements Frame.FrameListener {

    private static final Logger logger = LoggerFactory.getLogger(FrameManager.class);

    private Map<String, Frame> mFrames = new HashMap<>();
    private CDPSession mSession;
    private Page mPage;
    private NetworkManager mNetWorkManager;
    private Frame mMainFrame;
    private LifecycleWatcher.LifecycleListener mLifecycleListener;

    public FrameManager(CDPSession session, Page page) {
        mSession = session;
        mPage = page;
        mNetWorkManager = new NetworkManager(session, this, true); // TODO: 2/14/20 ignore https error
    }

    public void init() throws TimeoutException {
        mSession.setFrameListener(this);
        mSession.doCall(PageDomain.enableCommand);
        JSONObject json = mSession.doCall(PageDomain.getFrameTreeCommand);
        handleFrameTree(json.getJSONObject("result").getJSONObject("frameTree"));
        mSession.doCall(PageDomain.setLifecycleEventsEnabledCommand, new JSONObject() {{
            put("enabled", true);
        }});
        mSession.doCall(RuntimeDomain.enableCommand);
        ensureIsolatedWorld("juppeteer_world"); // FIXME: 2/14/20
        mNetWorkManager.init();
    }

    public void setLifecycleListener(LifecycleWatcher.LifecycleListener listener) {
        mLifecycleListener = listener;
    }

    private void handleFrameTree(JSONObject frameTree) {
        logger.debug("handleFrameTree: frameTree={}", frameTree);
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

    private void ensureIsolatedWorld(String name) {
//        if (this._isolatedWorlds.has(name))
//            return;
//        this._isolatedWorlds.add(name);
//        await this._client.send('Page.addScriptToEvaluateOnNewDocument', {
//                source: `//# sourceURL=${EVALUATION_SCRIPT_URL}`,
//        worldName: name,
//    }),
//        await Promise.all(this.frames().map(frame => this._client.send('Page.createIsolatedWorld', {
//                frameId: frame._id,
//                grantUniveralAccess: true,
//                worldName: name,
//    }).catch(debugError))); // frames might be removed before we send this
    }


    public Frame getMainFrame() {
        return mMainFrame;
    }

    public Frame getFrame(String frameId) {
        return mFrames.get(frameId);
    }

    public List<Frame> frames() {
//        /** @type {!Array<!Frame>} */
//        let frames = [];
//        collect(this._mainFrame);
//        return frames;
//
//        function collect(frame) {
//                frames.push(frame);
//        for (const subframe of frame._children)
//        collect(subframe);
        return new ArrayList<>();
    }

    public void navigateFrame(Frame frame, String url) {
        Response navigateResponse = mNetWorkManager.getNavigateResponse();
        try {
            logger.debug("navigateFrame: navigateResponse={}", navigateResponse);
//            navigateResponse.getBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFrameAttached(Frame.FrameInfo frameInfo) {
        if (mFrames.containsKey(frameInfo.getId())) return;

        if (StringUtil.isEmpty(frameInfo.getParentId()))
            throw new IllegalFrameException("parentFrameId is null");

        Frame frame = new Frame(mSession, this, frameInfo);
        mFrames.put(frameInfo.getId(), frame);

        // TODO: 2/14/20
//        this.emit(Events.FrameManager.FrameAttached, frame);
    }

    @Override
    public void onFrameNavigated(Frame.FrameInfo frameInfo) {
        logger.debug("onFrameNavigated: frameInfo={}", frameInfo);
        boolean isMainFrame = StringUtil.isEmpty(frameInfo.getParentId());
        Frame frame = isMainFrame ? getMainFrame() : mFrames.get(frameInfo.getId());
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
                mFrames.remove(frame.getFrameInfo().getId());
                frame.setFrameInfo(frameInfo);
            } else {
                // Initial main frame navigation.
                frame = new Frame(mSession, this, frameInfo);
            }
            mFrames.put(frameInfo.getId(), frame);
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
        mFrames.remove(frame.getFrameInfo().getId());
        // TODO: 2/16/20 listener
//        this.emit(Events.FrameManager.FrameDetached, frame);
    }

    @Override
    public void onFrameNavigatedWithinDocument(JSONObject event) {
        Frame frame = mFrames.get(event.getString("frameId"));
        if (Objects.isNull(frame)) return;

        frame.navigatedWithinDocument(event.getString("url"));

//        this.emit(Events.FrameManager.FrameNavigatedWithinDocument, frame);
//        this.emit(Events.FrameManager.FrameNavigated, frame);
    }

    @Override
    public void onFrameDetached(JSONObject event) {
        Frame frame = mFrames.get(event.getString("frameId"));
        if (Objects.nonNull(frame)) removeFramesRecursively(frame);
    }

    @Override
    public void onFrameStoppedLoading(JSONObject event) {
        Frame frame = mFrames.get(event.getString("frameId"));
        if (Objects.nonNull(frame)) {
            frame.stopLoading();
//        this.emit(Events.FrameManager.LifecycleEvent, frame);
        }
    }

    @Override
    public void onExecutionContextCreated(JSONObject context) {
        logger.debug("onExecutionContextCreated: context={}", context);
        String frameId = null;
        JSONObject auxData = context.getJSONObject("auxData");
        if (Objects.nonNull(auxData)) {
            frameId = auxData.getString("frameId");
        }
        Frame frame = mFrames.get(frameId);
        String world;
        if (Objects.nonNull(frame)) {
            if (Objects.nonNull(auxData) && auxData.getBoolean("isDefault")) {
//                world = frame
//            } else if (StringUtil.equals(context.getString("name"), "")) {
            }
        }
// const frameId = contextPayload.auxData ? contextPayload.auxData.frameId : null;
//    const frame = this._frames.get(frameId) || null;
//        let world = null;
//        if (frame) {
//            if (contextPayload.auxData && !!contextPayload.auxData['isDefault']) {
//                world = frame._mainWorld;
//            } else if (contextPayload.name === UTILITY_WORLD_NAME && !frame._secondaryWorld._hasContext()) {
//                // In case of multiple sessions to the same target, there's a race between
//                // connections so we might end up creating multiple isolated worlds.
//                // We can use either.
//                world = frame._secondaryWorld;
//            }
//        }
//        if (contextPayload.auxData && contextPayload.auxData['type'] === 'isolated')
//            this._isolatedWorlds.add(contextPayload.name);
//        /** @type {!ExecutionContext} */
//    const context = new ExecutionContext(this._client, contextPayload, world);
//        if (world)
//            world._setContext(context);
//        this._contextIdToContext.set(contextPayload.id, context);
    }

    @Override
    public void onExecutionContextDestroyed() {

    }

    @Override
    public void onExecutionContextsCleared() {

    }

    @Override
    public void onLifecycleEvent(JSONObject event) {
        logger.debug("onLifecycleEvent: event={}", event);
        Frame frame = mFrames.get(event.getString("frameId"));
        if (Objects.isNull(frame)) return;

        frame.proceedLifecycle(event.getString("loaderId"), event.getString("name"));
    }
}
