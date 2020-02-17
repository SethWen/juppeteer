package com.modorone.juppeteer.exception;

/**
 * author: Shawn
 * time  : 2/17/20 10:58 AM
 * desc  :
 * update: Shawn 2/17/20 10:58 AM
 */
public class RequestException extends JuppeteerException {

    public RequestException() {
    }

    public RequestException(String message) {
        super(message);
    }

    public RequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestException(Throwable cause) {
        super(cause);
    }

    public RequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
