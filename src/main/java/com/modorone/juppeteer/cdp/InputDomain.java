package com.modorone.juppeteer.cdp;

public interface InputDomain {

    // command
    String dispatchMouseEventCommand = "Input.dispatchMouseEvent";
    String dispatchTouchEventCommand = "Input.dispatchTouchEvent";
    String dispatchKeyEventCommand = "Input.dispatchKeyEvent";
    String insertTextCommand = "Input.insertText";
}
