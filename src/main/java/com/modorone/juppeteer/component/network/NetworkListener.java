package com.modorone.juppeteer.component.network;

import com.alibaba.fastjson.JSONObject;

public interface NetworkListener {

        void onRequestPaused();

        void onAuthRequired();

        void onRequestWillBeSent(JSONObject json);

        void onRequestServedFromCache();

        void onResponseReceived();

        void onLoadingFinished();

        void onLoadingFailed();
    }