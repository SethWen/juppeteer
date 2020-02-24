package com.modorone.juppeteer.pojo;

import com.alibaba.fastjson.annotation.JSONField;

public class BrowserVersion {

    /**
     * protocolVersion : 1.3
     * product : HeadlessChrome/80.0.3987.0
     * revision : @65d20b8e6b1e34d2687f4367477b92e89867c6f5
     * userAgent : Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) HeadlessChrome/80.0.3987.0 Safari/537.36
     * jsVersion : 8.0.426.1
     */

    @JSONField(name = "protocolVersion")
    private String protocolVersion;
    @JSONField(name = "product")
    private String product;
    @JSONField(name = "revision")
    private String revision;
    @JSONField(name = "userAgent")
    private String userAgent;
    @JSONField(name = "jsVersion")
    private String jsVersion;

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getJsVersion() {
        return jsVersion;
    }

    public void setJsVersion(String jsVersion) {
        this.jsVersion = jsVersion;
    }

    @Override
    public String toString() {
        return "BrowserVersion{" +
                "protocolVersion='" + protocolVersion + '\'' +
                ", product='" + product + '\'' +
                ", revision='" + revision + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", jsVersion='" + jsVersion + '\'' +
                '}';
    }
}