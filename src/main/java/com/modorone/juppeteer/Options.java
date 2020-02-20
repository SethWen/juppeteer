package com.modorone.juppeteer;

import com.modorone.juppeteer.pojo.Viewport;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Shawn
 * time  : 1/13/20 2:47 PM
 * desc  :
 * update: Shawn 1/13/20 2:47 PM
 */
public class Options {

    private boolean ignoreDefaultArgs = false;
    private List<String> args = new ArrayList<>();
    private boolean dumpio = false;
    private String executablePath = null;
    private boolean pipe = false;
    //    String env = process.env,
    private boolean handleSIGINT = true;
    private boolean handleSIGTERM = true;
    private boolean handleSIGHUP = true;
    private boolean ignoreHTTPSErrors = false;
    private Viewport defaultViewport = new Viewport();
    private int slowMo = 0;
    private int timeout = 30000;

    public boolean isIgnoreDefaultArgs() {
        return ignoreDefaultArgs;
    }

    public void setIgnoreDefaultArgs(boolean ignoreDefaultArgs) {
        this.ignoreDefaultArgs = ignoreDefaultArgs;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    public boolean isDumpio() {
        return dumpio;
    }

    public void setDumpio(boolean dumpio) {
        this.dumpio = dumpio;
    }

    public String getExecutablePath() {
        return executablePath;
    }

    public void setExecutablePath(String executablePath) {
        this.executablePath = executablePath;
    }

    public boolean isPipe() {
        return pipe;
    }

    public void setPipe(boolean pipe) {
        this.pipe = pipe;
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

    public Viewport getDefaultViewport() {
        return defaultViewport;
    }

    public void setDefaultViewport(Viewport defaultViewport) {
        this.defaultViewport = defaultViewport;
    }

    public int getSlowMo() {
        return slowMo;
    }

    public void setSlowMo(int slowMo) {
        this.slowMo = slowMo;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
