package com.modorone.juppeteer.component.input;


import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.exception.JuppeteerException;
import com.modorone.juppeteer.cdp.InputDomain;
import com.modorone.juppeteer.util.StringUtil;
import com.modorone.juppeteer.util.SystemUtil;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeoutException;

/**
 * author: Shawn
 * time  : 2/19/20 10:51 PM
 * desc  :
 * update: Shawn 2/19/20 10:51 PM
 */
public class Keyboard {

    private CDPSession mSession;
    private int mModifiers = 0;
    private Set<String> mPressKeys = new HashSet<>();

    public Keyboard(CDPSession session) {
        mSession = session;
    }

    public int getModifiers() {
        return mModifiers;
    }

    /**
     * @param key
     * @param options {{text?: string}=} options
     * @throws TimeoutException
     */
    public void down(String key, JSONObject options) throws TimeoutException {
        if (Objects.isNull(options)) options = new JSONObject();
        JSONObject desc = getKeyDescriptionByKeyString(key);
        boolean autoRepeat = mPressKeys.contains(desc.getString("code"));
        mPressKeys.add(desc.getString("code"));
        int modifierBit = getModifierBit(desc.getString("key"));
        if (modifierBit != 0) mModifiers = modifierBit;

        String text = options.getString("text");
        if (Objects.isNull(text)) text = desc.getString("text");
        String finalText = text;
        mSession.doCall(InputDomain.dispatchKeyEventCommand, new JSONObject() {{
            put("type", StringUtil.isEmpty(finalText) ? "rawKeyDown" : "keyDown");
            put("modifiers", mModifiers);
            put("windowsVirtualKeyCode", desc.getIntValue("keyCode"));
            put("code", desc.getString("code"));
            put("key", desc.getString("key"));
            put("text", finalText);
            put("unmodifiedText", finalText);
            put("autoRepeat", autoRepeat);
            put("location", desc.getIntValue("location"));
            put("isKeypad", desc.getIntValue("location") == 3);
        }});
    }

    private JSONObject getKeyDescriptionByKeyString(String key) {
        int shift = mModifiers & 8;
        JSONObject desc = new JSONObject() {{
            put("key", "");
            put("keyCode", 0);
            put("code", "");
            put("text", "");
            put("location", 0);
        }};
        JSONObject definition = USKeyboardLayout.getKey(key);
        if (Objects.isNull(definition)) throw new JuppeteerException("Unknown key: " + key);

        if (StringUtil.nonEmpty((definition.getString("key"))))
            desc.put("key", definition.getString("key"));
        if (shift != 0 && StringUtil.nonEmpty(definition.getString("shiftKey")))
            desc.put("key", definition.getString("shiftKey"));

        if (definition.getIntValue("keyCode") != 0)
            desc.put("keyCode", definition.getIntValue("keyCode"));
        if (shift != 0 && definition.getIntValue("shiftKeyCode") != 0)
            desc.put("keyCode", definition.getIntValue("shiftKeyCode"));

        if (StringUtil.nonEmpty(definition.getString("code")))
            desc.put("code", definition.getString("code"));

        if (definition.getIntValue("location") != 0)
            desc.put("location", definition.getIntValue("location"));

        if (desc.getString("key").length() == 1)
            desc.put("text", desc.getString("key"));

        if (StringUtil.nonEmpty(definition.getString("text")))
            desc.put("text", definition.getString("text"));
        if (shift != 0 && StringUtil.nonEmpty(definition.getString("shiftText")))
            desc.put("text", definition.getString("shiftText"));

        // if any modifiers besides shift are pressed, no text should be sent
        if ((mModifiers & ~8) != 0) desc.put("text", "");

        return desc;
    }

    private int getModifierBit(String key) {
        if (Objects.isNull(key)) return 0;

        switch (key) {
            case "Alt":
                return 1;
            case "Control":
                return 2;
            case "Meta":
                return 4;
            case "Shift":
                return 8;
            default:
                return 0;
        }
    }

    public void up(String key) throws TimeoutException {
        JSONObject desc = getKeyDescriptionByKeyString(key);
        mModifiers &= ~getModifierBit(desc.getString("key"));
        mPressKeys.remove(desc.getString("code"));
        mSession.doCall(InputDomain.dispatchKeyEventCommand, new JSONObject() {{
            put("type", "keyUp");
            put("modifiers", mModifiers);
            put("key", desc.getString("key"));
            put("windowsVirtualKeyCode", desc.getIntValue("keyCode"));
            put("code", desc.getString("code"));
            put("location", desc.getIntValue("location"));
        }});
    }

    public void sendCharacter(String ch) throws TimeoutException {
        mSession.doCall(InputDomain.insertTextCommand, new JSONObject() {{
            put("text", ch);
        }});
    }

    /**
     * @param text
     * @param options {{delay: (number|undefined)}=} options
     */
    public void type(String text, JSONObject options) throws TimeoutException {
        if (Objects.isNull(options)) options = new JSONObject();
        long delay = options.getLongValue("delay");
        for (int i = 0; i < text.length(); i++) {
            String ch = String.valueOf(text.charAt(i));
            if (Objects.nonNull(USKeyboardLayout.getKey(ch))) {
                press(ch, new JSONObject() {{
                    put("delay", delay);
                }});
            } else {
                SystemUtil.sleep(delay);
                sendCharacter(ch);
            }
        }
    }

    /**
     * @param key
     * @param options {!{delay?: number, text?: string}=} options
     */
    public void press(String key, JSONObject options) throws TimeoutException {
        if (Objects.isNull(options)) options = new JSONObject();
        down(key, new JSONObject());
        long delay = options.getLongValue("delay");
        SystemUtil.sleep(delay);
        up(key);
    }
}
