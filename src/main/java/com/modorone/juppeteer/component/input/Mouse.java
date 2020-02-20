package com.modorone.juppeteer.component.input;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.protocol.InputDomain;
import com.modorone.juppeteer.util.StringUtil;
import com.modorone.juppeteer.util.SystemUtil;

import java.util.Objects;
import java.util.concurrent.TimeoutException;

/**
 * author: Shawn
 * time  : 2/19/20 10:50 PM
 * desc  :
 * update: Shawn 2/19/20 10:50 PM
 */
public class Mouse {

    private CDPSession mSession;
    private Keyboard mKeyboard;
    private int mX;
    private int mY;
    private String mButton;

    public Mouse(CDPSession session, Keyboard keyboard) {
        this.mSession = session;
        this.mKeyboard = keyboard;
        // {'none'|'left'|'right'|'middle'}
        this.mButton = "none";
    }

    public void move(int x, int y, JSONObject options) throws TimeoutException {
        int steps = options.getIntValue("steps");
        if (steps < 1) steps = 1;

        int fromX = mX, fromY = mY;
        mX = x;
        mY = y;
        for (int i = 1; i <= steps; i++) {
            int finalI = i, finalSteps = steps;
            mSession.doCall(InputDomain.dispatchMouseEventCommand, new JSONObject() {{
                put("type", "mouseMoved");
                put("button", mButton);
                put("x", fromX + (mX - fromX) * (finalI / finalSteps));
                put("y", fromY + (mY - fromY) * (finalI / finalSteps));
                put("modifiers", mKeyboard.getModifiers());
            }});
        }
    }

    public void click(int x, int y, JSONObject options) throws TimeoutException {
        long delay = options.getLongValue("delay");
        move(x, y, options);
        down(options);
        SystemUtil.sleep(delay);
        up(options);
    }

    public void down(JSONObject options) throws TimeoutException {
        String button = StringUtil.isEmpty(options.getString("button")) ? "left" : options.getString("button");
        int clickCount = Objects.isNull(options.getInteger("clickCount")) ? 1 : options.getInteger("clickCount");
        mButton = button;
        mSession.doCall(InputDomain.dispatchMouseEventCommand, new JSONObject() {{
            put("type", "mousePressed");
            put("button", button);
            put("x", mX);
            put("y", mY);
            put("modifiers", mKeyboard.getModifiers());
            put("clickCount", clickCount);
        }});
    }

    public void up(JSONObject options) throws TimeoutException {
        String button = StringUtil.isEmpty(options.getString("button")) ? "left" : options.getString("button");
        int clickCount = Objects.isNull(options.getInteger("clickCount")) ? 1 : options.getInteger("clickCount");
        mButton = "none";
        mSession.doCall(InputDomain.dispatchMouseEventCommand, new JSONObject() {{
            put("type", "mouseReleased");
            put("button", button);
            put("x", mX);
            put("y", mY);
            put("modifiers", mKeyboard.getModifiers());
            put("clickCount", clickCount);
        }});
    }
}
