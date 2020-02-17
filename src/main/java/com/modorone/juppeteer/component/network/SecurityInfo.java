package com.modorone.juppeteer.component.network;

import com.alibaba.fastjson.JSONObject;

/**
 * author: Shawn
 * time  : 2/17/20 12:36 PM
 * desc  :
 * update: Shawn 2/17/20 12:36 PM
 */
public class SecurityInfo {

    private String subjectName;
    private String issuer;
    private String validFrom;
    private String validTo;
    private String protocol;

    public SecurityInfo(JSONObject securityPayload) {
        this.subjectName = securityPayload.getString("subjectName");
        this.issuer = securityPayload.getString("issuer");
        this.validFrom = securityPayload.getString("validFrom");
        this.validTo = securityPayload.getString("validTo");
        this.protocol = securityPayload.getString("protocol");
    }

    public String getSubjectName() {
        return subjectName;
    }

    public SecurityInfo setSubjectName(String subjectName) {
        this.subjectName = subjectName;
        return this;
    }

    public String getIssuer() {
        return issuer;
    }

    public SecurityInfo setIssuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public SecurityInfo setValidFrom(String validFrom) {
        this.validFrom = validFrom;
        return this;
    }

    public String getValidTo() {
        return validTo;
    }

    public SecurityInfo setValidTo(String validTo) {
        this.validTo = validTo;
        return this;
    }

    public String getProtocol() {
        return protocol;
    }

    public SecurityInfo setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }
}
