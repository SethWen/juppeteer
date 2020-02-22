package com.modorone.juppeteer;

import com.modorone.juppeteer.cdp.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * author: Shawn
 * time  : 1/13/20 2:48 PM
 * desc  :
 * update: Shawn 1/13/20 2:48 PM
 */
public class Launcher {

    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

    public Launcher(String projectRoot, String preferredRevision, boolean isPuppeteerCore) {
    }

    public Browser launch(Options options) throws TimeoutException {
        List<String> chromeArguments = new ArrayList<>();

//        if (!ignoreDefaultArgs)
//            chromeArguments.push(...this.defaultArgs(options));
//    else if (Array.isArray(ignoreDefaultArgs))
//            chromeArguments.push(...this.defaultArgs(options).filter(arg => ignoreDefaultArgs.indexOf(arg) === -1));
//    else
//        chromeArguments.push(...args);
        if (!options.isIgnoreDefaultArgs()) {
//            chromeArguments.add(this.defaultArgs(options));
        } else {
            chromeArguments.addAll(options.getArgs());
        }

        chromeArguments.add("--remote-debugging-pipe");
        chromeArguments.add("--user-data-dir=userdata");
        // for child process to start browser instance

        BrowserRunner browserRunner = new BrowserRunner(Constants.executablePath, defaultArgs(null));
        browserRunner.run();
        Connection connection = browserRunner.createConnection();
        return Browser.create(browserRunner.getProcess(), connection);
    }

    public Browser connect(Options options) {
        String wsEndPoint = "";
        // TODO: 2/16/20 封装一个远程 process
        return new Browser(null, Connection.create(wsEndPoint));
    }

    public String executablePath() {
        return Constants.executablePath;
    }

    public List<String> defaultArgs(Options options) {
        List<String> args = Arrays.asList(
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
                "--disable-features=TranslateUI",
                "--disable-hang-monitor",
                "--disable-ipc-flooding-protection",
                "--disable-popup-blocking",
                "--disable-prompt-on-repost",
                "--disable-renderer-backgrounding",
                "--disable-sync --force-color-profile=srgb",
                "--metrics-recording-only --no-first-run --enable-automation",
                "--password-store=basic --use-mock-keychain",
                "about:blank",
                "--remote-debugging-port=0",
                "--user-data-dir=/data/debug"
        );
//          const {
//            devtools = false,
//                    headless = !devtools,
//                    args = [],
//            userDataDir = null
//        } = options;
//    const chromeArguments = [...DEFAULT_ARGS];
//        if (userDataDir)
//            chromeArguments.push(`--user-data-dir=${userDataDir}`);
//        if (devtools)
//            chromeArguments.push('--auto-open-devtools-for-tabs');
//        if (headless) {
//            chromeArguments.push(
//                    '--headless',
//                    '--hide-scrollbars',
//                    '--mute-audio'
//            );
//        }
//        if (args.every(arg => arg.startsWith('-')))
//        chromeArguments.push('about:blank');
//        chromeArguments.push(...args);
//        return chromeArguments;
        return args;
    }
}
