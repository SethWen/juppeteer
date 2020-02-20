package com.modorone.juppeteer.pojo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * author: Shawn
 * time  : 2/20/20 8:59 PM
 * desc  :
 * update: Shawn 2/20/20 8:59 PM
 */
public class Device {


    /**
     * viewport : {"hasTouch":true,"isLandscape":false,"width":600,"deviceScaleFactor":1,"isMobile":true,"height":1024}
     * name : Blackberry PlayBook
     * userAgent : Mozilla/5.0 (PlayBook; U; RIM Tablet OS 2.1.0; en-US) AppleWebKit/536.2+ (KHTML like Gecko) Version/7.2.1.0 Safari/536.2+
     */

    @JSONField(name = "viewport")
    private Viewport viewport;
    @JSONField(name = "name")
    private String name;
    @JSONField(name = "userAgent")
    private String userAgent;

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
