package com.modorone.juppeteer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public Browser launch(Options options) throws IOException, InterruptedException {
        logger.debug("launch: options={}", options);
        return mLauncher.launch(options);
    }

    public Browser connect(Options options) {
        return mLauncher.connect(options);
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

    /**
     * @param {!Launcher.ChromeArgOptions=} options
     * @return {!Array<string>}
     */
    public Options defaultArgs(Options options) {
        return mLauncher.defaultArgs(options);
    }

    public BrowserFetcher createBrowserFetcher(Options options) {
        return new BrowserFetcher(mProjectRoot, options);
    }
}
