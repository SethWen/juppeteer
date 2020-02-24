package com.modorone.juppeteer;

import com.modorone.juppeteer.util.Pair;

import java.util.List;

/**
 * author: Shawn
 * time  : 1/13/20 2:47 PM
 * desc  :
 * update: Shawn 1/13/20 2:47 PM
 */
public class LaunchOptions {

    /**
     * @typedef {Object} LaunchOptions
     * @property {boolean=} ignoreHTTPSErrors
     * @property {boolean=} headless
     * @property {number=} slowMo
     * @property {!Array<string>=} args
     * @property {boolean=} ignoreDefaultArgs
     * @property {boolean=} handleSIGINT
     * @property {boolean=} handleSIGTERM
     * @property {boolean=} handleSIGHUP
     * @property {number=} timeout
     * @property {boolean=} dumpio
     * @property {string=} userDataDir
     * @property {!Object<string, string | undefined>=} env
     * @property {boolean=} devtools
     * @property {boolean=} pipe
     * @property {boolean=} appMode
     */

    private boolean isIgnoreDefaultArgs;
    private List<String> args;
    private boolean isHeadless;
    /**
     * Connection.onMessage() delay
     */
    private int slowMo = 0;
    private String userDataDir;
    private boolean showDevtools;
    /**
     * window size, 800*800 default
     */
    private Pair<Integer, Integer> windowSize = Pair.of(1000, 800);


    //    String env = process.env,
    private boolean handleSIGINT = true;
    private boolean handleSIGTERM = true;
    private boolean handleSIGHUP = true;
    private boolean ignoreHTTPSErrors = false;

    public static LaunchOptions getDefault() {
        return new LaunchOptions();
    }

    public boolean isIgnoreDefaultArgs() {
        return isIgnoreDefaultArgs;
    }

    public LaunchOptions setIgnoreDefaultArgs(boolean ignoreDefaultArgs) {
        isIgnoreDefaultArgs = ignoreDefaultArgs;
        return this;
    }

    public List<String> getArgs() {
        return args;
    }

    public LaunchOptions setArgs(List<String> args) {
        this.args = args;
        return this;
    }

    public boolean isHeadless() {
        return isHeadless;
    }

    public LaunchOptions setHeadless(boolean headless) {
        this.isHeadless = headless;
        return this;
    }

    public int getSlowMo() {
        return slowMo;
    }

    public LaunchOptions setSlowMo(int slowMo) {
        this.slowMo = slowMo;
        return this;
    }

    public String getUserDataDir() {
        return userDataDir;
    }

    public LaunchOptions setUserDataDir(String userDataDir) {
        this.userDataDir = userDataDir;
        return this;
    }

    public boolean isShowDevtools() {
        return showDevtools;
    }

    public LaunchOptions setShowDevtools(boolean showDevtools) {
        this.showDevtools = showDevtools;
        return this;
    }

    public Pair<Integer, Integer> getWindowSize() {
        return windowSize;
    }

    public LaunchOptions setWindowSize(Pair<Integer, Integer> windowSize) {
        this.windowSize = windowSize;
        return this;
    }

    public boolean isHandleSIGINT() {
        return handleSIGINT;
    }

    public void setHandleSIGINT(boolean handleSIGINT) {
        this.handleSIGINT = handleSIGINT;
    }

    public boolean isHandleSIGTERM() {
        return handleSIGTERM;
    }

    public void setHandleSIGTERM(boolean handleSIGTERM) {
        this.handleSIGTERM = handleSIGTERM;
    }

    public boolean isHandleSIGHUP() {
        return handleSIGHUP;
    }

    public void setHandleSIGHUP(boolean handleSIGHUP) {
        this.handleSIGHUP = handleSIGHUP;
    }

    public boolean isIgnoreHTTPSErrors() {
        return ignoreHTTPSErrors;
    }

    public void setIgnoreHTTPSErrors(boolean ignoreHTTPSErrors) {
        this.ignoreHTTPSErrors = ignoreHTTPSErrors;
    }
}
