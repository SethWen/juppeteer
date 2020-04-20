package com.modorone.juppeteer.component.network;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.util.BlockingCell;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.component.Frame;
import com.modorone.juppeteer.exception.RequestException;
import com.modorone.juppeteer.cdp.NetWorkDomain;
import com.modorone.juppeteer.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

/**
 * author: Shawn
 * time  : 2/17/20 12:35 PM
 * desc  :
 * update: Shawn 2/17/20 12:35 PM
 */
public class Response {

    private static final Logger logger = LoggerFactory.getLogger(Response.class);

    private CDPSession mSession;
    private Request mRequest;
    private Pair<String, Integer> mRemoteAddress;
    private int mStatus;
    private String mStatusText;
    private String mUrl;
    private boolean mFromDiskCache;
    private boolean mFromServiceWorker;
    private Map<String, String> mHeaders = new HashMap<>();
    private SecurityInfo mSecurityInfo;
    private BlockingCell<Pair<Boolean, String>> mResponseWaiter;

    public Response(CDPSession session, Request request, JSONObject responsePayload) {
        mSession = session;
        mRequest = request;
        mRemoteAddress = Pair.of(responsePayload.getString("remoteIPAddress"), responsePayload.getInteger("remotePort"));
        mStatus = responsePayload.getInteger("status");
        mStatusText = responsePayload.getString("statusText");
        mUrl = request.getUrl();
        mFromDiskCache = responsePayload.getBoolean("fromDiskCache");
        mFromServiceWorker = responsePayload.getBoolean("fromServiceWorker");

        if (Objects.nonNull(responsePayload.getJSONObject("securityDetails"))) {
            mSecurityInfo = new SecurityInfo(responsePayload.getJSONObject("securityDetails"));
        }

        JSONObject json = responsePayload.getJSONObject("headers");
        json.forEach((key, value) -> mHeaders.put(key.toLowerCase(), (String) value));

        mResponseWaiter = new BlockingCell<>();
    }

    //    constructor(client, request, responsePayload) {
//        this._client = client;
//        this._request = request;
//        this._contentPromise = null;
//
//        this._bodyLoadedPromise = new Promise(fulfill => {
//                this._bodyLoadedPromiseFulfill = fulfill;
//    });
//
//        this._remoteAddress = {
//                ip: responsePayload.remoteIPAddress,
//                port: responsePayload.remotePort,
//    };
//        this._status = responsePayload.status;
//        this._statusText = responsePayload.statusText;
//        this._url = request.url();
//        this._fromDiskCache = !!responsePayload.fromDiskCache;
//        this._fromServiceWorker = !!responsePayload.fromServiceWorker;
//        this._headers = {};
//        for (const key of Object.keys(responsePayload.headers))
//        this._headers[key.toLowerCase()] = responsePayload.headers[key];
//        this._securityDetails = responsePayload.securityDetails ? new SecurityDetails(responsePayload.securityDetails) : null;
//    }

    public String getUrl() {
        return mUrl;
    }

    public Pair<String, Integer> getRemoteAddress() {
        return mRemoteAddress;
    }

    public boolean ok() {
        return mStatus == 0 || (mStatus >= 200 && mStatus <= 299);
    }

    public int getStatus() {
        return mStatus;
    }

    public String getStatusText() {
        return mStatusText;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public SecurityInfo getSecurityInfo() {
        return mSecurityInfo;
    }

    public void terminateWaiting(Pair<Boolean, String> info) {
        if (Objects.nonNull(mResponseWaiter)) mResponseWaiter.set(info);
    }

    public byte[] getBytes() throws TimeoutException {
        if (Objects.nonNull(mResponseWaiter)) {
            Pair<Boolean, String> info = mResponseWaiter.uninterruptibleGet();
            // request failed
            if (!info.getFirst()) throw new RequestException(info.getSecond());

            JSONObject response = mSession.doCall(NetWorkDomain.getResponseBodyCommand, new JSONObject() {{
                put("requestId", mRequest.getRequestId());
            }}).getJSONObject("result");
            byte[] bytes = response.getString("body").getBytes(StandardCharsets.UTF_8);
            if (response.getBooleanValue("base64Encoded")) {
                return Base64.getDecoder().decode(bytes);
            } else {
                return bytes;
            }
        }
        return null;
    }

    public String getText() throws TimeoutException {
        return new String(getBytes(), StandardCharsets.UTF_8);
    }

    public Request getRequest() {
        return mRequest;
    }

    public boolean isFromCache() {
        return mFromDiskCache || mRequest.isFromMemoryCache();
    }

    public boolean isFromServiceWorker() {
        return mFromServiceWorker;
    }

    public Frame getFrame() {
        return mRequest.getFrame();
    }
}
