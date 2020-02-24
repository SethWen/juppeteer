package com.modorone.juppeteer;

import com.modorone.juppeteer.cdp.Connection;
import com.modorone.juppeteer.component.Browser;
import com.modorone.juppeteer.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

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

    public Browser launch(IRunner runner, LaunchOptions options) throws Exception {
        List<String> args = new ArrayList<>();

        if (options.isIgnoreDefaultArgs()) {
            if (Objects.nonNull(options.getArgs())) args.addAll(options.getArgs());

            String debuggingOption = args.stream().filter(arg -> StringUtil.startsWith(arg, "--remote-debugging-port=")).collect(Collectors.joining());
            if (StringUtil.isEmpty(debuggingOption)) args.add("--remote-debugging-port=0");
        } else {
            args.addAll(defaultArgs());
        }

        if (Objects.nonNull(options.getWindowSize())) {
            args.add(String.format("--window-size=%d,%d", options.getWindowSize().getFirst(), options.getWindowSize().getSecond()));
        }

        if (options.isHeadless()) {
            args.add("--headless");
            args.add("--disable-gpu");
            args.add("--hide-scrollbars");
            args.add("--mute-audio");
        }
        if (StringUtil.nonEmpty(options.getUserDataDir())) {
            args.add("--user-data-dir=" + options.getUserDataDir());
        } else {
            Path tmpPath = Files.createTempDirectory("chromium-", PosixFilePermissions.asFileAttribute(
                    new HashSet<PosixFilePermission>() {{
                        add(PosixFilePermission.OWNER_READ);
                        add(PosixFilePermission.OWNER_WRITE);
                        add(PosixFilePermission.OWNER_EXECUTE);
                        add(PosixFilePermission.GROUP_READ);
                    }})
            );
            options.setUserDataDir(tmpPath.toAbsolutePath().toString());
            args.add("--user-data-dir=" + tmpPath.toAbsolutePath().toString());
        }

        if (options.isShowDevtools()) {
            args.add("--auto-open-devtools-for-tabs");
        }

        String wsEndPoint = runner.run(args);
        return Browser.create(runner, Connection.create(wsEndPoint, options.getSlowMo()));
    }

    public Browser connect(String wsEndPoint, LaunchOptions options) throws TimeoutException {
        return Browser.create(null, Connection.create(wsEndPoint, options.getSlowMo()));
    }

    public String executablePath() {
        return Constants.executablePath;
    }

    public List<String> defaultArgs() {
        return Arrays.asList(
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

                "--enable-automation",
                "--password-store=basic",
                "--use-mock-keychain",

                "--single-process",
                "--no-sandbox",
                "--no-zygote",
                "--disable-infobars",
                "--disk-cache-dir=/tmp/chromium-cache",
                "--disk-cache-size=1048576",

                "--remote-debugging-port=0",
                "about:blank"
        );
    }
}
