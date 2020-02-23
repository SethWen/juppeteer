package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.util.StringUtil;

import java.util.List;

/**
 * author: Shawn
 * time  : 2/23/20 9:22 PM
 * desc  :
 * update: Shawn 2/23/20 9:22 PM
 */
public class FileChooser {

    private CDPSession mSession;
    private JSONObject mEvent;
    private boolean mMultiple;
    private boolean mHandled;

    public FileChooser(CDPSession session, JSONObject event) {
        mSession = session;
        mEvent = event;
        mMultiple = !StringUtil.equals("selectSingle", event.getString("mode"));
    }

    public boolean isMultiple() {
        return mMultiple;
    }

    public void accept(List<String> filePaths) {
//        assert(!this._handled, 'Cannot accept FileChooser which is already handled!');
//        this._handled = true;
//    const files = filePaths.map(filePath => path.resolve(filePath));
//        await this._client.send('Page.handleFileChooser', {
//                action: 'accept',
//    });
    }


    public void cancel() {
//        assert(!this._handled, 'Cannot cancel FileChooser which is already handled!');
//        this._handled = true;
//        await this._client.send('Page.handleFileChooser', {
//                action: 'cancel',
//    });
    }
}
