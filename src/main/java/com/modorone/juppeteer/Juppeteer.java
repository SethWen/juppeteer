package com.modorone.juppeteer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * author: Shawn
 * time  : 1/13/20 2:41 PM
 * desc  :
 * update: Shawn 1/13/20 2:41 PM
 */
public class Juppeteer {

    private static final Logger logger = LoggerFactory.getLogger(Juppeteer.class);
    private static Juppeteer sJuppeteer;

    private String mProjectRoot;
    private Launcher mLauncher;

    public static Juppeteer getInstance() {
        if (sJuppeteer == null) {
            synchronized (Juppeteer.class) {
                if (sJuppeteer == null) {
                    sJuppeteer = new Juppeteer("", "", false);
                }
            }
        }
        return sJuppeteer;
    }

    private Juppeteer(String projectRoot, String preferredRevision, boolean isPuppeteerCore) {
        this.mProjectRoot = projectRoot;
        this.mLauncher = new Launcher(projectRoot, preferredRevision, isPuppeteerCore);
    }

    public Browser launch(IRunner runner, LaunchOptions options) throws Exception {
        logger.debug("launch: options={}", options);
        return mLauncher.launch(runner, options);
    }

    public Browser connect(String wsEndPoint, LaunchOptions options) throws TimeoutException {
        return mLauncher.connect(wsEndPoint, options);
    }


    public String getExecutablePath() {
        return mLauncher.executablePath();
    }

    public List<DeviceDescriptor> getDevices() {
        return new ArrayList<>();
    }

//    public void getErrors() {
//        return Errors;
//    }

//    public List<String> defaultArgs(LaunchOptions options) {
//        return mLauncher.defaultArgs(options);
//    }

    public BrowserFetcher createBrowserFetcher(LaunchOptions options) {
        return new BrowserFetcher(mProjectRoot, options);
    }
}
