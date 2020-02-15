package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.protocol.NetWorkDomain;
import com.modorone.juppeteer.protocol.SecurityDomain;

import java.util.MissingFormatArgumentException;
import java.util.concurrent.TimeoutException;

/**
 * author: Shawn
 * time  : 2/14/20 5:59 PM
 * desc  :
 * update: Shawn 2/14/20 5:59 PM
 */
public class NetWorkManager {

    private CDPSession mSession;
    private FrameManager mFrameManager;
    private boolean mIgnoreHTTPSErrors;

    public NetWorkManager(CDPSession session, FrameManager frameManager, boolean ignoreHTTPSErrors) {
        mSession = session;
        mFrameManager = frameManager;
        mIgnoreHTTPSErrors = ignoreHTTPSErrors;
    }

    public void init() throws TimeoutException {
        mSession.doCall(NetWorkDomain.enableCommand);
        if (mIgnoreHTTPSErrors) {
            mSession.doCall(SecurityDomain.setIgnoreCertificateErrorsCommand, new JSONObject() {{
                put("ignore", true);
            }});

        }
    }
}
