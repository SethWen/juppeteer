package com.modorone.juppeteer.pojo;

import com.alibaba.fastjson.annotation.JSONField;

public class Viewport {
    /**
     * hasTouch : true
     * isLandscape : false
     * width : 600
     * deviceScaleFactor : 1
     * isMobile : true
     * height : 1024
     */

    @JSONField(name = "hasTouch")
    private boolean hasTouch = false;
    @JSONField(name = "isLandscape")
    private boolean isLandscape;
    @JSONField(name = "width")
    private int width = 800;
    @JSONField(name = "deviceScaleFactor")
    private int deviceScaleFactor = 1;
    @JSONField(name = "isMobile")
    private boolean isMobile = false;
    @JSONField(name = "height")
    private int height = 600;

    public boolean hasTouch() {
        return hasTouch;
    }

    public void setHasTouch(boolean hasTouch) {
        this.hasTouch = hasTouch;
    }

    public boolean isLandscape() {
        return isLandscape;
    }

    public void setLandscape(boolean isLandscape) {
        this.isLandscape = isLandscape;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getDeviceScaleFactor() {
        return deviceScaleFactor;
    }

    public void setDeviceScaleFactor(int deviceScaleFactor) {
        this.deviceScaleFactor = deviceScaleFactor;
    }

    public boolean isMobile() {
        return isMobile;
    }

    public void setMobile(boolean isMobile) {
        this.isMobile = isMobile;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}