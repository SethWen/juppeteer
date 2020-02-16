package com.modorone.juppeteer;

import com.modorone.juppeteer.cdp.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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

        BrowserRunner browserRunner = new BrowserRunner();
        browserRunner.run();
        Connection connection = browserRunner.createConnection();
        logger.debug("launch: connection={}", connection);
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

    public Options defaultArgs(Options options) {
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
        return null;
    }
}
