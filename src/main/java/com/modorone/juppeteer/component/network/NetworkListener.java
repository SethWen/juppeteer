package com.modorone.juppeteer.component.network;

import com.alibaba.fastjson.JSONObject;

public interface NetworkListener {

        void onRequestPaused(JSONObject event);

        void onAuthRequired(JSONObject event);

        void onRequestWillBeSent(JSONObject event);

        void onRequestServedFromCache(JSONObject event);

        void onResponseReceived(JSONObject event);

        void onLoadingFinished(JSONObject event);

        void onLoadingFailed(JSONObject event);
    }