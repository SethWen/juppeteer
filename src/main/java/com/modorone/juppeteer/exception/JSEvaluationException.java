package com.modorone.juppeteer.exception;

/**
 * author: Shawn
 * time  : 2/20/20 4:31 PM
 * desc  :
 * update: Shawn 2/20/20 4:31 PM
 */
public class JSEvaluationException extends JuppeteerException {

    public JSEvaluationException() {
    }

    public JSEvaluationException(String message) {
        super(message);
    }

    public JSEvaluationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JSEvaluationException(Throwable cause) {
        super(cause);
    }

    public JSEvaluationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
