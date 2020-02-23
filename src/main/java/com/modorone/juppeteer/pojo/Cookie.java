package com.modorone.juppeteer.pojo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * author: Shawn
 * time  : 2/23/20 2:54 PM
 * desc  :
 * update: Shawn 2/23/20 2:54 PM
 */
public class Cookie {

    /**
     * path : /
     * expires : 1.613140071783541E9
     * size : 44
     * session : false
     * domain : .baidu.com
     * name : BAIDUID
     * httpOnly : false
     * secure : false
     * value : 7E48987B41D4C77BF8A8C7A631A9D5B4:FG=1
     */

    @JSONField(name = "path")
    private String path;
    @JSONField(name = "expires")
    private double expires = -1;        // never expired default
    @JSONField(name = "size")
    private int size;
    @JSONField(name = "session")
    private boolean session;
    @JSONField(name = "domain")
    private String domain;
    @JSONField(name = "name")
    private String name = "";
    @JSONField(name = "httpOnly")
    private boolean httpOnly;
    @JSONField(name = "secure")
    private boolean secure;
    @JSONField(name = "value")
    private String value;
    @JSONField(name = "url")
    private String url;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public double getExpires() {
        return expires;
    }

    public void setExpires(double expires) {
        this.expires = expires;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isSession() {
        return session;
    }

    public void setSession(boolean session) {
        this.session = session;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUrl() {
        return url;
    }

    public Cookie setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public String toString() {
        return "Cookie{" +
                "path='" + path + '\'' +
                ", expires=" + expires +
                ", size=" + size +
                ", session=" + session +
                ", domain='" + domain + '\'' +
                ", name='" + name + '\'' +
                ", httpOnly=" + httpOnly +
                ", secure=" + secure +
                ", value='" + value + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
