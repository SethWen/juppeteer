package com.modorone.juppeteer.exception;

/**
 * author: Shawn
 * time  : 2/13/20 6:13 PM
 * desc  :
 * update: Shawn 2/13/20 6:13 PM
 */
public class WebSocketCreationException extends JuppeteerException {

    public WebSocketCreationException() {
    }

    public WebSocketCreationException(String message) {
        super(message);
    }

    public WebSocketCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebSocketCreationException(Throwable cause) {
        super(cause);
    }

    public WebSocketCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
