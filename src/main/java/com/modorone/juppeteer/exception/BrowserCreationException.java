package com.modorone.juppeteer.exception;

/**
 * author: Shawn
 * time  : 2/13/20 5:41 PM
 * desc  :
 * update: Shawn 2/13/20 5:41 PM
 */
public class BrowserCreationException extends JuppeteerException {

    public BrowserCreationException() {
    }

    public BrowserCreationException(String message) {
        super(message);
    }

    public BrowserCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BrowserCreationException(Throwable cause) {
        super(cause);
    }

    public BrowserCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
