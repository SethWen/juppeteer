package com.modorone.juppeteer.component.network;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.component.Frame;
import com.modorone.juppeteer.exception.RequestException;
import com.modorone.juppeteer.protocol.FetchDomain;
import com.modorone.juppeteer.util.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * author: Shawn
 * time  : 2/17/20 12:33 PM
 * desc  :
 * update: Shawn 2/17/20 12:33 PM
 */
public class Request {

    private CDPSession mSession;
    private String mRequestId;
    private boolean mIsNavigationRequest;
    private String mInterceptionId;
    private boolean mAllowInterception;
    private boolean mInterceptionHandled = false;
    private Response mResponse;
    private String mFailureText;

    private String mUrl;
    private String mResourceType;
    private String mMethod;
    private String mPostData; // FIXME: 2/17/20 type??
    private Map<String, String> mHeaders = new HashMap<>();
    private Frame mFrame;
    private List<Request> mRedirectChain;
    private boolean mFromMemoryCache = false;


    public Request(CDPSession session, Frame frame, String interceptionId, boolean allowInterception,
                   JSONObject event, List<Request> redirectChain) {
        mSession = session;
        mFrame = frame;
        mInterceptionId = interceptionId;
        mRedirectChain = redirectChain;

        mUrl = event.getJSONObject("request").getString("url");
        mRequestId = event.getString("requestId");
        mIsNavigationRequest = (StringUtil.equals(event.getString("requestId"), event.getString("loaderId"))
                && StringUtil.equals(event.getString("type"), "Document"));
        mAllowInterception = allowInterception;

        JSONObject json = event.getJSONObject("request").getJSONObject("headers");
        json.forEach((key, value) -> mHeaders.put(key.toLowerCase(), (String) value));
    }

    public String getRequestId() {
        return mRequestId;
    }

    public void setRequestId(String requestId) {
        this.mRequestId = requestId;
    }

    public String getInterceptionId() {
        return mInterceptionId;
    }

    public void setInterceptionId(String interceptionId) {
        mInterceptionId = interceptionId;
    }

    public List<Request> getRedirectChain() {
        return mRedirectChain;
    }

    public void setRedirectChain(List<Request> redirectChain) {
        mRedirectChain = redirectChain;
    }

    public String getFailureText() {
        return mFailureText;
    }

    public void setFailureText(String failureText) {
        mFailureText = failureText;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getResourceType() {
        return mResourceType;
    }

    public void setResourceType(String resourceType) {
        mResourceType = resourceType;
    }

    public String getMethod() {
        return mMethod;
    }

    public void setMethod(String method) {
        mMethod = method;
    }

    public String getPostData() {
        return mPostData;
    }

    public void setPostData(String postData) {
        mPostData = postData;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public void setHeaders(Map<String, String> headers) {
        mHeaders = headers;
    }

    public Response getResponse() {
        return mResponse;
    }

    public void setResponse(Response response) {
        mResponse = response;
    }

    public Frame getFrame() {
        return mFrame;
    }

    public void setFrame(Frame frame) {
        mFrame = frame;
    }

    public boolean isNavigationRequest() {
        return mIsNavigationRequest;
    }

    public void setNavigationRequest(boolean isNavigationRequest) {
        mIsNavigationRequest = isNavigationRequest;
    }

    public boolean isFromMemoryCache() {
        return mFromMemoryCache;
    }

    public void setFromMemoryCache(boolean fromMemoryCache) {
        mFromMemoryCache = fromMemoryCache;
    }

    public String failure() {
        return mFailureText;
    }

    public void proceed(RequestOption option) throws TimeoutException {
        // Request interception is not supported for data: urls.
        if (StringUtil.startsWith(mUrl, "data:")) return;

        if (!mAllowInterception) throw new RequestException("Request Interception is not enabled!");
        if (mInterceptionHandled) throw new RequestException("Request is already handled!");

        mInterceptionHandled = true;
        mSession.doCall(FetchDomain.continueRequestCommand, new JSONObject() {{
            put("requestId", mInterceptionId);
            put("url", option.getUrl());
            put("method", option.getMethod());
            put("headers", option.getHeaders());
        }});
    }

    public void respond(Response response) {
        // Request interception is not supported for data: urls.
        if (StringUtil.startsWith(mUrl, "data:")) return;
        if (!mAllowInterception) throw new RequestException("Request Interception is not enabled!");
        if (mInterceptionHandled) throw new RequestException("Request is already handled!");

        mInterceptionHandled = true;
//
//    const responseBody = response.body && helper.isString(response.body) ? Buffer.from(/** @type {string} */(response.body)) : /** @type {?Buffer} */(response.body || null);
//
//        /** @type {!Object<string, string>} */
//    const responseHeaders = {};
//        if (response.headers) {
//            for (const header of Object.keys(response.headers))
//            responseHeaders[header.toLowerCase()] = response.headers[header];
//        }
//        if (response.contentType)
//            responseHeaders['content-type'] = response.contentType;
//        if (responseBody && !('content-length' in responseHeaders))
//        responseHeaders['content-length'] = String(Buffer.byteLength(responseBody));
//
//        await this._client.send('Fetch.fulfillRequest', {
//                requestId: this._interceptionId,
//                responseCode: response.status || 200,
//                responsePhrase: STATUS_TEXTS[response.status || 200],
//                responseHeaders: headersArray(responseHeaders),
//                body: responseBody ? responseBody.toString('base64') : undefined,
//    }).catch(error => {
//                // In certain cases, protocol will return error if the request was already canceled
//                // or the page was closed. We should tolerate these errors.
//                debugError(error);
//    });
    }

    public void abort(String errorCode) throws TimeoutException {
        // Request interception is not supported for data: urls.
        if (StringUtil.startsWith(mUrl, "data:")) return;

        String errorReason = Mapper.ERROR_REASONS.get(errorCode);
        if (StringUtil.isEmpty(errorCode)) throw new IllegalArgumentException("Invalid error code: " + errorCode);
        if (!mAllowInterception) throw new RequestException("Request Interception is not enabled!");
        if (mInterceptionHandled) throw new RequestException("Request is already handled!");

        mInterceptionHandled = true;
        mSession.doCall(FetchDomain.failRequestCommand, new JSONObject() {{
            put("requestId", mInterceptionId);
            put("errorReason", errorReason);
        }});

//    const errorReason = errorReasons[errorCode];
//        assert(errorReason, 'Unknown error code: ' + errorCode);
//        assert(this._allowInterception, 'Request Interception is not enabled!');
//        assert(!this._interceptionHandled, 'Request is already handled!');
//        this._interceptionHandled = true;
//        await this._client.send('Fetch.failRequest', {
//                requestId: this._interceptionId,
//                errorReason
//    }).catch(error => {
//                // In certain cases, protocol will return error if the request was already canceled
//                // or the page was closed. We should tolerate these errors.
//                debugError(error);
//    });
    }


}
