package com.modorone.juppeteer.exception;

/**
 * author: Shawn
 * time  : 2/13/20 5:40 PM
 * desc  :
 * update: Shawn 2/13/20 5:40 PM
 */
public class JuppeteerException extends RuntimeException {

    public JuppeteerException() {
    }

    public JuppeteerException(String message) {
        super(message);
    }

    public JuppeteerException(String message, Throwable cause) {
        super(message, cause);
    }

    public JuppeteerException(Throwable cause) {
        super(cause);
    }

    public JuppeteerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
