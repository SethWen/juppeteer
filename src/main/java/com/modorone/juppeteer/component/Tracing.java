package com.modorone.juppeteer.component;

import com.modorone.juppeteer.cdp.CDPSession;

/**
 * author: Shawn
 * time  : 2/19/20 10:51 PM
 * desc  :
 * update: Shawn 2/19/20 10:51 PM
 */
public class Tracing {

    private CDPSession mSession;

    public Tracing(CDPSession session) {
        mSession = session;
    }
}
