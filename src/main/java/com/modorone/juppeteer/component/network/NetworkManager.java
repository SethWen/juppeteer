package com.modorone.juppeteer.component.network;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.component.Frame;
import com.modorone.juppeteer.component.FrameManager;
import com.modorone.juppeteer.protocol.FetchDomain;
import com.modorone.juppeteer.protocol.NetWorkDomain;
import com.modorone.juppeteer.protocol.SecurityDomain;
import com.modorone.juppeteer.util.Pair;
import com.modorone.juppeteer.util.StringUtil;

import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * author: Shawn
 * time  : 2/14/20 5:59 PM
 * desc  :
 * update: Shawn 2/14/20 5:59 PM
 */
public class NetworkManager implements NetworkListener {

    private CDPSession mSession;
    private FrameManager mFrameManager;
    private boolean mIgnoreHTTPSErrors;
    private Pair<String, String> mAuthenticate;
    private boolean mUserRequestInterceptionEnabled;
    private boolean mProtocolRequestInterceptionEnabled;
    private boolean mUserCacheDisabled;
    private Map<String, String> mExtraHTTPHeaders;
    private boolean mOffline;
    private Map<String, String> mRequestId2InterceptionIdMap = new HashMap<>();
    private Map<String, JSONObject> mRequestId2RequestWillBeSentEventMap = new HashMap<>();
    private Map<String, Request> mRequestId2RequestMap = new HashMap<>();

    public NetworkManager(CDPSession session, FrameManager frameManager, boolean ignoreHTTPSErrors) {
        mSession = session;
        mFrameManager = frameManager;
        mIgnoreHTTPSErrors = ignoreHTTPSErrors;
        mSession.setNetworkListener(this);
    }

    public void init() throws TimeoutException {
        mSession.doCall(NetWorkDomain.enableCommand);
        if (mIgnoreHTTPSErrors) {
            mSession.doCall(SecurityDomain.setIgnoreCertificateErrorsCommand, new JSONObject() {{
                put("ignore", true);
            }});
        }
    }

    // FIXME: 2/17/20 浏览器中没看到该 header
    public void setExtraHTTPHeaders(Map<String, String> headers) throws TimeoutException {
        mExtraHTTPHeaders = headers;
        mSession.doCall(NetWorkDomain.setExtraHTTPHeadersCommand, new JSONObject() {{
            put("headers", headers);
        }});
    }

    public Map<String, String> getExtraHTTPHeaders() {
        return mExtraHTTPHeaders;
    }

    public void setAuthenticate(String username, String password) throws TimeoutException {
        mAuthenticate = Pair.of(username, password);
        updateProtocolRequestInterception();
    }

    public void setOfflineMode(boolean offline) throws TimeoutException {
        if (Objects.equals(mOffline, offline)) return;

        mOffline = offline;
        mSession.doCall(NetWorkDomain.emulateNetworkConditionsCommand, new JSONObject() {{
            put("offline", mOffline);
            // values of 0 remove any active throttling. crbug.com/456324#c9
            put("latency", 0);
            put("downloadThroughput", -1);
            put("uploadThroughput", -1);
        }});
    }

    public void setUserAgent(String userAgent) throws TimeoutException {
        mSession.doCall(NetWorkDomain.setUserAgentOverrideCommand, new JSONObject() {{
            put("userAgent", userAgent);
        }});
    }

    public void setCacheEnabled(boolean enabled) throws TimeoutException {
        mUserCacheDisabled = !enabled;
        updateProtocolCacheDisabled();
    }

    public void setRequestInterception(boolean enabled) throws TimeoutException {
        mUserRequestInterceptionEnabled = enabled;
        updateProtocolRequestInterception();
    }

    private void updateProtocolRequestInterception() throws TimeoutException {
        boolean enabled = mUserRequestInterceptionEnabled || Objects.nonNull(mAuthenticate);
        if (Objects.equals(mProtocolRequestInterceptionEnabled, enabled)) return;

        mProtocolRequestInterceptionEnabled = enabled;
        updateProtocolCacheDisabled();
        if (enabled) {
            mSession.doCall(FetchDomain.enableCommand, new JSONObject() {{
                put("handleAuthRequests", true);
                put("patterns", new JSONArray(Collections.singletonList(new JSONObject() {{
                    put("urlPattern", "*");
                }})));
            }});
        } else {
            mSession.doCall(FetchDomain.disableCommand);
        }
    }

    private void updateProtocolCacheDisabled() throws TimeoutException {
        mSession.doCall(NetWorkDomain.setCacheDisabledCommand, new JSONObject() {{
            put("cacheDisabled", mUserCacheDisabled || mProtocolRequestInterceptionEnabled);
        }});

    }

    @Override
    public void onRequestPaused() {

    }

    @Override
    public void onAuthRequired() {

    }

    @Override
    public void onRequestWillBeSent(JSONObject event) {
        String url = event.getJSONObject("request").getString("url");
        // Request interception doesn't happen for data URLs with Network Service.
        if (mProtocolRequestInterceptionEnabled && StringUtil.startsWith(url, "data:")) {
            String requestId = event.getString("requestId");
            String interceptionId = mRequestId2InterceptionIdMap.get(requestId);
            if (!StringUtil.isEmpty(interceptionId)) {
                onRequest(event, interceptionId);
                mRequestId2InterceptionIdMap.remove(requestId);
            } else {
                mRequestId2RequestWillBeSentEventMap.put(requestId, event);
            }
        } else {
            onRequest(event, null);
        }
//        // Request interception doesn't happen for data URLs with Network Service.
//        if (this._protocolRequestInterceptionEnabled && !event.request.url.startsWith('data:')) {
//      const requestId = event.requestId;
//      const interceptionId = this._requestIdToInterceptionId.get(requestId);
//            if (interceptionId) {
//                this._onRequest(event, interceptionId);
//                this._requestIdToInterceptionId.delete(requestId);
//            } else {
//                this._requestIdToRequestWillBeSentEvent.set(event.requestId, event);
//            }
//            return;
//        }
//        this._onRequest(event, null);
    }

    public void onRequest(JSONObject event, String interceptionId) {
        List<Request> redirectChain = new ArrayList<>();
        if (Objects.nonNull(event.getJSONObject("redirectResponse"))) {
            Request request = mRequestId2RequestMap.get(event.getString("requestId"));
            if (Objects.nonNull(request)) {
                handleRequestRedirect(request, event.getJSONObject("redirectResponse"));
                redirectChain = request.getRedirectChain();
            }
        }
        String frameId = event.getString("frameId");
        Frame frame = StringUtil.isEmpty(frameId) ? null : mFrameManager.getFrame(frameId);
        Request request = new Request(mSession, frame, interceptionId, mUserRequestInterceptionEnabled, event, redirectChain);
        mRequestId2RequestMap.put(event.getString("requestId"), request);

//        let redirectChain = [];
//        if (event.redirectResponse) {
//      const request = this._requestIdToRequest.get(event.requestId);
//            // If we connect late to the target, we could have missed the requestWillBeSent event.
//            if (request) {
//                this._handleRequestRedirect(request, event.redirectResponse);
//                redirectChain = request._redirectChain;
//            }
//        }
//    const frame = event.frameId ? this._frameManager.frame(event.frameId) : null;
//    const request = new Request(this._client, frame, interceptionId, this._userRequestInterceptionEnabled, event, redirectChain);
//        this._requestIdToRequest.set(event.requestId, request);
//        this.emit(Events.NetworkManager.Request, request);
    }

    private void handleRequestRedirect(Request request, JSONObject redirectResponse) {
// const response = new Response(this._client, request, responsePayload);
//        request._response = response;
//        request._redirectChain.push(request);
//        response._bodyLoadedPromiseFulfill.call(null, new Error('Response body is unavailable for redirect responses'));
//        this._requestIdToRequest.delete(request._requestId);
//        this._attemptedAuthentications.delete(request._interceptionId);
//        this.emit(Events.NetworkManager.Response, response);
//        this.emit(Events.NetworkManager.RequestFinished, request);
    }

    @Override
    public void onRequestServedFromCache() {

    }

    @Override
    public void onResponseReceived() {

    }

    @Override
    public void onLoadingFinished() {

    }

    @Override
    public void onLoadingFailed() {

    }
}
