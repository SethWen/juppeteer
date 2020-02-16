package com.modorone.juppeteer.exception;

/**
 * author: Shawn
 * time  : 2/16/20 3:21 PM
 * desc  :
 * update: Shawn 2/16/20 3:21 PM
 */
public class IllegalTargetException extends JuppeteerException {

    public IllegalTargetException() {
    }

    public IllegalTargetException(String message) {
        super(message);
    }

    public IllegalTargetException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalTargetException(Throwable cause) {
        super(cause);
    }

    public IllegalTargetException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
