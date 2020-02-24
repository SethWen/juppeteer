package com.modorone.juppeteer;


/**
 * author: Shawn
 * time  : 2/21/20 2:12 PM
 * desc  :
 * update: Shawn 2/21/20 2:12 PM
 */
public class CommandOptions {

    /**
     * command execute with timeout(millisecond)
     */
    private long timeout = 30000;
    /**
     * command execute with delay(millisecond)
     */
    private long delay = 0;
    /**
     * command execute with steps
     */
    private long steps = 1;
    /**
     * request header of referer
     */
    private String referer;
    /**
     * request command execute until
     */
    private WaitUntil waitUntil = WaitUntil.LOAD;

    private boolean visible;

    private boolean hidden;

    public static CommandOptions getDefault() {
        return new CommandOptions();
    }

    public long getTimeout() {
        return timeout;
    }

    public CommandOptions setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    public long getDelay() {
        return delay;
    }

    public CommandOptions setDelay(long delay) {
        this.delay = delay;
        return this;
    }

    public long getSteps() {
        return steps;
    }

    public CommandOptions setSteps(long steps) {
        this.steps = steps;
        return this;
    }

    public String getReferer() {
        return referer;
    }

    public CommandOptions setReferer(String referer) {
        this.referer = referer;
        return this;
    }

    public WaitUntil getWaitUntil() {
        return waitUntil;
    }

    public CommandOptions setWaitUntil(WaitUntil waitUntil) {
        this.waitUntil = waitUntil;
        return this;
    }

    public boolean isVisible() {
        return visible;
    }

    public CommandOptions setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public boolean isHidden() {
        return hidden;
    }

    public CommandOptions setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }
}
