package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.exception.IllegalFrameException;
import com.modorone.juppeteer.protocol.PageDomain;
import com.modorone.juppeteer.protocol.RuntimeDomain;
import com.modorone.juppeteer.util.StringUtil;

import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * author: Shawn
 * time  : 2/14/20 5:53 PM
 * desc  :
 * update: Shawn 2/14/20 5:53 PM
 */
public class FrameManager implements Frame.FrameListener {

    private Map<String, Frame> mFrames = new HashMap<>();
    private CDPSession mSession;
    private Page mPage;
    private NetWorkManager mNetWorkManager;

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
        handleFrameTree(json);
        mSession.doCall(PageDomain.setLifecycleEventsEnabledCommand, new JSONObject() {{
            put("enabled", true);
        }});
        mSession.doCall(RuntimeDomain.enableCommand);
        ensureIsolatedWorld("juppeteer_world"); // FIXME: 2/14/20
        mNetWorkManager.init();
    }

    private void handleFrameTree(JSONObject json) {
        // TODO: 2/14/20
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

    public Frame frame(String frameId) {
//        return this._frames.get(frameId);
        return null;
    }

    public Frame mainFrame() {
//        return this._mainFrame;
        return null;
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
    public void onFrameAttached(JSONObject json) {
//        if (this._frames.has(frameId))
//            return;
//        assert(parentFrameId);
//    const parentFrame = this._frames.get(parentFrameId);
//    const frame = new Frame(this, this._client, parentFrame, frameId);
//        this._frames.set(frame._id, frame);
//        this.emit(Events.FrameManager.FrameAttached, frame);
        if (mFrames.containsKey(json.getString("frameId"))) return;

        String parentFrameId = json.getString("parentFrameId");
        if (StringUtil.isEmpty(parentFrameId))
            throw new IllegalFrameException("parentFrameId is null");

        Frame parentFrame = mFrames.get(parentFrameId);
        String frameId = json.getString("frameId");
        Frame frame = new Frame(mSession, this, parentFrame, frameId);
        mFrames.put(frameId, frame);

        // TODO: 2/14/20
//        this.emit(Events.FrameManager.FrameAttached, frame);
    }

    @Override
    public void onFrameNavigated() {

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
