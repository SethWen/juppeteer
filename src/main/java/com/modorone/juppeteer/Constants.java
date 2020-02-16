package com.modorone.juppeteer;

/**
 * author: Shawn
 * time  : 1/13/20 2:42 PM
 * desc  :
 * update: Shawn 1/13/20 2:42 PM
 */
public class Constants {

    public static final long INFINITY = -1;

//    public static final String executablePath = "/home/shawn/WorkSpace/xinde/marcelo/node_modules/playwright-core/.local-chromium/linux-733125/chrome-linux/chrome";
    public static final String executablePath = "/home/shawn/WorkSpace/xinde/marcelo/node_modules/puppeteer/.local-chromium/linux-722234/chrome-linux/chrome";

    public static String[] DEFAULT_ARGS = new String[]{
            "--disable-background-networking",
            "--enable-features=NetworkService,NetworkServiceInProcess",
            "--disable-background-timer-throttling",
            "--disable-backgrounding-occluded-windows",
            "--disable-breakpad",
            "--disable-client-side-phishing-detection",
            "--disable-component-extensions-with-background-pages",
            "--disable-default-apps",
            "--disable-dev-shm-usage",
            "--disable-extensions",
            // BlinkGenPropertyTrees disabled due to crbug.com/937609
            "--disable-features=TranslateUI,BlinkGenPropertyTrees",
            "--disable-hang-monitor",
            "--disable-ipc-flooding-protection",
            "--disable-popup-blocking",
            "--disable-prompt-on-repost",
            "--disable-renderer-backgrounding",
            "--disable-sync",
            "--force-color-profile=srgb",
            "--metrics-recording-only",
            "--no-first-run",
            "--enable-automation",
            "--password-store=basic",
            "--use-mock-keychain",
    };
}
