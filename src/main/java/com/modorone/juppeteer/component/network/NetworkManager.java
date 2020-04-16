package com.modorone.juppeteer.component.network;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.component.Frame;
import com.modorone.juppeteer.component.FrameManager;
import com.modorone.juppeteer.cdp.FetchDomain;
import com.modorone.juppeteer.cdp.NetWorkDomain;
import com.modorone.juppeteer.util.Pair;
import com.modorone.juppeteer.util.StringUtil;

import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

/**
 * author: Shawn
 * time  : 2/14/20 5:59 PM
 * desc  :
 * update: Shawn 2/14/20 5:59 PM
 */
public class NetworkManager implements NetworkListener {

    private CDPSession mSession;
    private FrameManager mFrameManager;
    /**
     * first: username, second: password
     */
    private Pair<String, String> mAuthenticate;
    private boolean mUserRequestInterceptionEnabled;
    private boolean mProtocolRequestInterceptionEnabled;
    private boolean mUserCacheDisabled;
    private Map<String, String> mExtraHTTPHeaders;
    private boolean mOffline;
    private Map<String, String> mRequestId2InterceptionIdMap = new HashMap<>();
    private Map<String, JSONObject> mRequestId2RequestWillBeSentEventMap = new HashMap<>();
    private Map<String, Request> mRequestId2RequestMap = new HashMap<>();
    private Set<String> mAttemptedAuthentications = new HashSet<>();

    private Set<Consumer<Request>> mRequestConsumers = new HashSet<>();
    private Set<Consumer<Response>> mResponseConsumers = new HashSet<>();

    public NetworkManager(CDPSession session, FrameManager frameManager) {
        mSession = session;
        mFrameManager = frameManager;
        mSession.setNetworkListener(this);
    }

    public void init() throws TimeoutException {
        mSession.doCall(NetWorkDomain.enableCommand);
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

    public void addRequestConsumer(Consumer<Request> consumer) {
        if (Objects.nonNull(consumer)) mRequestConsumers.add(consumer);
    }

    public boolean removeRequestConsumer(Consumer<Request> consumer) {
        if (Objects.nonNull(consumer)) return mRequestConsumers.remove(consumer);
        return false;
    }

    public void addResponseConsumer(Consumer<Response> consumer) {
        if (Objects.nonNull(consumer)) mResponseConsumers.add(consumer);
    }

    public boolean removeResponseConsumer(Consumer<Response> consumer) {
        if (Objects.nonNull(consumer)) return mResponseConsumers.remove(consumer);
        return false;
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
    public void onRequestPaused(JSONObject event) {
        if (!mUserRequestInterceptionEnabled && mProtocolRequestInterceptionEnabled) {
            // FIXME: 2/18/20 存在递归调用，如何处理？
//            try {
//                mSession.doCall(FetchDomain.continueRequestCommand, new JSONObject() {{
//                    put("requestId", event.getString("requestId"));
//                }});
//            } catch (Exception ignore) {
//            }
        }
        String requestId = event.getString("networkId");
        String interceptionId = event.getString("requestId");
        if (!StringUtil.isEmpty(requestId) && mRequestId2RequestWillBeSentEventMap.containsKey(requestId)) {
            JSONObject willBeSentEvent = mRequestId2RequestWillBeSentEventMap.get(requestId);
            onRequest(willBeSentEvent, interceptionId);
            mRequestId2RequestWillBeSentEventMap.remove(requestId);
        } else {
            mRequestId2InterceptionIdMap.put(requestId, interceptionId);
        }
    }

    @Override
    public void onAuthRequired(JSONObject event) {
        // {"Default"|"CancelAuth"|"ProvideCredentials"}
        String response = "Default";
        if (mAttemptedAuthentications.contains(event.getString("requestId"))) {
            response = "CancelAuth";
        } else if (Objects.nonNull(mAuthenticate)) {
            response = "ProvideCredentials";
            mAttemptedAuthentications.add(event.getString("requestId"));
        }

        try {
            // FIXME: 2/18/20 存在递归调用，如何处理？
            String finalResponse = response;
            mSession.doCall(FetchDomain.continueWithAuthCommand, new JSONObject() {{
                put("requestId", event.getString("requestId"));
                put("authChallengeResponse", new JSONObject() {{
                    put("response", finalResponse);
                    put("username", Objects.nonNull(mAuthenticate) ? mAuthenticate.getFirst() : null);
                    put("password", Objects.nonNull(mAuthenticate) ? mAuthenticate.getSecond() : null);
                }});
            }});
        } catch (Exception ignore) {
        }
    }

    @Override
    public void onRequestWillBeSent(JSONObject event) {
        String url = event.getJSONObject("request").getString("url");
        // Request interception doesn't happen for data URLs with Network Service.
        if (mProtocolRequestInterceptionEnabled && StringUtil.startsWith(url, "data:")) {
            String requestId = event.getString("requestId");
            String interceptionId = mRequestId2InterceptionIdMap.get(requestId);
            if (StringUtil.nonEmpty(interceptionId)) {
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
            // If we connect late to the target, we could have missed the requestWillBeSent event.
            if (Objects.nonNull(request)) {
                handleRequestRedirect(request, event.getJSONObject("redirectResponse"));
                redirectChain = request.getRedirectChain();
            }
        }
        String frameId = event.getString("frameId");
        Frame frame = StringUtil.isEmpty(frameId) ? null : mFrameManager.getFrame(frameId);
        Request request = new Request(mSession, frame, interceptionId, mUserRequestInterceptionEnabled, event, redirectChain);
        mRequestId2RequestMap.put(event.getString("requestId"), request);

        mRequestConsumers.forEach(requestConsumer -> requestConsumer.accept(request));
    }

    private void handleRequestRedirect(Request request, JSONObject redirectResponse) {
        Response response = new Response(mSession, request, redirectResponse);
        request.setResponse(response);
        request.getRedirectChain().add(request);
        // TODO: 2/17/20
        response.terminateWaiting(Pair.of(true, ""));
        mRequestId2RequestMap.remove(request.getRequestId());
        mAttemptedAuthentications.remove(request.getInterceptionId());

//        this.emit(Events.NetworkManager.Response, response);
//        this.emit(Events.NetworkManager.RequestFinished, request);
    }

    @Override
    public void onRequestServedFromCache(JSONObject event) {
        Request request = mRequestId2RequestMap.get(event.getString("requestId"));
        if (Objects.nonNull(request)) request.setFromMemoryCache(true);
    }

    @Override
    public void onResponseReceived(JSONObject event) {
        Request request = mRequestId2RequestMap.get(event.getString("requestId"));
        // FileUpload sends a response without a matching request.
        if (Objects.isNull(request)) return;

        Response response = new Response(mSession, request, event.getJSONObject("response"));
        request.setResponse(response);

        mResponseConsumers.forEach(responseConsumer -> responseConsumer.accept(response));
    }

    @Override
    public void onLoadingFinished(JSONObject event) {
        Request request = mRequestId2RequestMap.get(event.getString("requestId"));
        // For certain requestIds we never receive requestWillBeSent event.
        // @see https://crbug.com/750469
        if (Objects.isNull(request)) return;

        // Under certain conditions we never get the Network.responseReceived
        // event from protocol. @see https://crbug.com/883475
        Response response = request.getResponse();
        if (Objects.nonNull(response)) {
            // TODO: 2/17/20
            response.terminateWaiting(Pair.of(true, ""));
        }
        mRequestId2RequestMap.remove(request.getRequestId());
        mAttemptedAuthentications.remove(request.getInterceptionId());
    }

    @Override
    public void onLoadingFailed(JSONObject event) {
        Request request = mRequestId2RequestMap.get(event.getString("requestId"));
        // For certain requestIds we never receive requestWillBeSent event.
        // @see https://crbug.com/750469
        if (Objects.isNull(request)) return;
        request.setFailureText(event.getString("errorText"));
        Response response = request.getResponse();
        if (Objects.nonNull(response)) {
            // TODO: 2/17/20
            response.terminateWaiting(Pair.of(false, event.getString("errorText")));
        }
        mRequestId2RequestMap.remove(request.getRequestId());
        mAttemptedAuthentications.remove(request.getInterceptionId());
    }
}
