package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.cdp.PageDomain;

/**
 * author: Shawn
 * time  : 2/20/20 2:40 PM
 * desc  :
 * update: Shawn 2/20/20 2:40 PM
 */
public class Dialog {

    private CDPSession session;
    private String type;
    private String message;

    public CDPSession getSession() {
        return session;
    }

    public Dialog setSession(CDPSession session) {
        this.session = session;
        return this;
    }

    public String getType() {
        return type;
    }

    public Dialog setType(String type) {
        this.type = type;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Dialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public void accept(String promptText) {
        try {
            // 这个地方没有返回 非 RPC 奇怪 fixme
            this.session.doCall(PageDomain.handleJavaScriptDialogCommand, new JSONObject() {{
                put("accept", true);
                put("promptText", promptText);
            }}, 3000);
        } catch (Exception ignore) {
        }
    }

    public void dismiss() {
        try {
            // 这个地方没有返回 非 RPC 奇怪 fixme
            this.session.doCall(PageDomain.handleJavaScriptDialogCommand, new JSONObject() {{
                put("accept", false);
            }}, 3000);
        } catch (Exception ignore) {
        }
    }

    @Override
    public String toString() {
        return "Dialog{" +
                "type='" + type + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
