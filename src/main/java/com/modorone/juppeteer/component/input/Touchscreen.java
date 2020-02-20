package com.modorone.juppeteer.component.input;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.protocol.InputDomain;
import com.modorone.juppeteer.protocol.RuntimeDomain;

import java.util.concurrent.TimeoutException;

/**
 * author: Shawn
 * time  : 2/19/20 10:50 PM
 * desc  :
 * update: Shawn 2/19/20 10:50 PM
 */
public class Touchscreen {

    private CDPSession mSession;
    private Keyboard mKeyboard;
    private int mX;
    private int mY;

    public Touchscreen(CDPSession session, Keyboard keyboard) {
        this.mSession = session;
        this.mKeyboard = keyboard;
    }

    public void down(int x, int y) throws TimeoutException {
        mX = x;
        mY = y;
        mSession.doCall(InputDomain.dispatchTouchEventCommand, new JSONObject() {{
            put("type", "touchStart");
            put("touchPoints", new JSONArray() {{
                add(new JSONObject() {{
                    put("x", x);
                    put("y", y);
                }});
            }});
            put("modifiers", ""); // todo
        }});
    }

    public void up() throws TimeoutException {
        mSession.doCall(InputDomain.dispatchTouchEventCommand, new JSONObject() {{
            put("type", "touchEnd");
            put("touchPoints", new JSONArray());
            put("modifiers", ""); // todo
        }});
    }

    public void move(int x, int y, JSONObject options) throws TimeoutException {
        int fromX = mX;
        int fromY = mY;
        mX = x;
        mY = y;
        int steps = options.getInteger("steps");
        if (steps < 1) steps = 1;

        for (int i = 0; i < steps; i++) {
            int finalI = i;
            int finalSteps = steps;
            mSession.doCall(InputDomain.dispatchTouchEventCommand, new JSONObject() {{
                put("type", "touchMove");
                put("touchPoints", new JSONArray() {{
                    add(new JSONObject() {{
                        put("x", fromX + (mX - fromX) * (finalI / finalSteps));
                        put("y", fromY + (mY - fromY) * (finalI / finalSteps));
                    }});
                }});
//                put("modifiers", ""); // todo
            }});
        }
    }

    public void tap(int x, int y) throws TimeoutException {
        mSession.doCall(RuntimeDomain.evaluateCommand, new JSONObject() {{
            put("expression", "new Promise(x => requestAnimationFrame(() => requestAnimationFrame(x)))");
            put("awaitPromise", true);
        }});

        down(x, y);
        up();
    }
}
