package com.modorone.juppeteer;

import com.alibaba.fastjson.JSONObject;

/**
 * author: Shawn
 * time  : 2/21/20 2:12 PM
 * desc  :
 * update: Shawn 2/21/20 2:12 PM
 */
public class RequestOptions extends JSONObject {

    /**
     * millisecond
     */
    private int timeout;
    /**
     * millisecond
     */
    private int delay;
    /**
     * request header of referer
     */
    private String referer;

    private String waitUntil;

}
