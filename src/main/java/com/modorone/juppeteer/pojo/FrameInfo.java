package com.modorone.juppeteer.pojo;

import com.alibaba.fastjson.annotation.JSONField;

public class FrameInfo {

    /**
     * securityOrigin : chrome-search://most-visited
     * loaderId : 572BDFDF2604EFE662C0BE4E4F68F496
     * name : custom-links-edit
     * id : 928ADFF8EE2A1010693140D686E27B6C
     * mimeType : text/html
     * parentId : 16EB95E8F0EE66874D98EFD7DF315C51
     * url : chrome-search://most-visited/edit.html?addTitle=Add%20shortcut&editTitle=Edit%20shortcut&nameField=Name&urlField=URL&linkRemove=Remove&linkCancel=Cancel&linkDone=Done&invalidUrl=Type%20a%20valid%20URL
     */

    @JSONField(name = "securityOrigin")
    private String securityOrigin;
    @JSONField(name = "loaderId")
    private String loaderId;
    @JSONField(name = "name")
    private String name;
    @JSONField(name = "id")
    private String id;
    @JSONField(name = "mimeType")
    private String mimeType;
    @JSONField(name = "parentId")
    private String parentId;
    @JSONField(name = "url")
    private String url;

    public String getSecurityOrigin() {
        return securityOrigin;
    }

    public void setSecurityOrigin(String securityOrigin) {
        this.securityOrigin = securityOrigin;
    }

    public String getLoaderId() {
        return loaderId;
    }

    public void setLoaderId(String loaderId) {
        this.loaderId = loaderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "FrameInfo{" +
                "securityOrigin='" + securityOrigin + '\'' +
                ", loaderId='" + loaderId + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", parentId='" + parentId + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}