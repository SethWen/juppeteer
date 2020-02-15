package com.modorone.juppeteer.exception;

/**
 * author: Shawn
 * time  : 2/14/20 10:41 PM
 * desc  :
 * update: Shawn 2/14/20 10:41 PM
 */
public class IllegalFrameException extends JuppeteerException {

    public IllegalFrameException() {
    }

    public IllegalFrameException(String message) {
        super(message);
    }

    public IllegalFrameException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalFrameException(Throwable cause) {
        super(cause);
    }

    public IllegalFrameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
