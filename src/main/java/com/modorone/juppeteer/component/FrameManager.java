package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.exception.IllegalFrameException;
import com.modorone.juppeteer.protocol.PageDomain;
import com.modorone.juppeteer.protocol.RuntimeDomain;
import com.modorone.juppeteer.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private NetWorkManager mNetWorkManager;
    private Frame mMainFrame;

    public FrameManager(CDPSession session, Page page) {
        mSession = session;
        mPage = page;
        mNetWorkManager = new NetWorkManager(session, this, true); // TODO: 2/14/20 ignore https error
    }

    public void init() throws TimeoutException {
//          const [,{frameTree}] = await Promise.all([
//                this._client.send('Page.enable'),
//                this._client.send('Page.getFrameTree'),
//    ]);
//        this._handleFrameTree(frameTree);
//        await Promise.all([
//                this._client.send('Page.setLifecycleEventsEnabled', { enabled: true }),
//        this._client.send('Runtime.enable', {}).then(() => this._ensureIsolatedWorld(UTILITY_WORLD_NAME)),
//        this._networkManager.initialize(),
//    ]);
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

    @Override
    public void onFrameAttached(Frame.FrameInfo frameInfo) {
//        if (this._frames.has(frameId))
//            return;
//        assert(parentFrameId);
//    const parentFrame = this._frames.get(parentFrameId);
//    const frame = new Frame(this, this._client, parentFrame, frameId);
//        this._frames.set(frame._id, frame);
//        this.emit(Events.FrameManager.FrameAttached, frame);

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
    public void onFrameNavigatedWithinDocument() {

    }

    @Override
    public void onFrameDetached() {

    }

    @Override
    public void onFrameStoppedLoading() {

    }

    @Override
    public void onExecutionContextCreated() {

    }

    @Override
    public void onExecutionContextDestroyed() {

    }

    @Override
    public void onExecutionContextsCleared() {

    }

    @Override
    public void onLifecycleEvent() {

    }
}
