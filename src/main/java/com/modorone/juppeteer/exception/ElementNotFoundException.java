package com.modorone.juppeteer.exception;

/**
 * author: Shawn
 * time  : 2/20/20 3:42 PM
 * desc  :
 * update: Shawn 2/20/20 3:42 PM
 */
public class ElementNotFoundException extends JuppeteerException {

    public ElementNotFoundException() {
    }

    public ElementNotFoundException(String message) {
        super(message);
    }

    public ElementNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ElementNotFoundException(Throwable cause) {
        super(cause);
    }

    public ElementNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
