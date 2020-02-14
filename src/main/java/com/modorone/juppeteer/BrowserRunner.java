package com.modorone.juppeteer;

import com.modorone.juppeteer.cdp.Connection;
import com.modorone.juppeteer.exception.BrowserCreationException;
import com.modorone.juppeteer.util.StringUtil;
import com.modorone.juppeteer.util.SystemUtil;
import com.modorone.juppeteer.util.ThreadExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Locale.ENGLISH;

/**
 * author: Shawn
 * time  : 2/13/20 4:24 PM
 * desc  :
 * update: Shawn 2/13/20 4:24 PM
 */
public class BrowserRunner {

    private static final Logger logger = LoggerFactory.getLogger(BrowserRunner.class);
    private String mWsEndPoint;

    public void run() throws BrowserCreationException {
        AtomicReference<Exception> exceptionReference = new AtomicReference<>();
        ThreadExecutor.getInstance().execute(() -> {
            try {
                ProcessBuilder builder = new ProcessBuilder();
                String command = Constants.executablePath;
                builder.command(command, "--remote-debugging-port=9222", "--user-data-dir=/data/debug", "--headless"); // todo redirect stdio
                Process process = builder.start();
                Scanner scanner = new Scanner(process.getErrorStream());
                while (scanner.hasNext()) {
                    String line = scanner.nextLine().trim();
                    if (line.isEmpty()) {
                        continue;
                    }
                    if (line.toLowerCase(ENGLISH).startsWith("devtools listening on")) {
                        int start = line.indexOf("ws://");
                        mWsEndPoint = line.substring(start);
                        break;
                    }
                }
                if (!StringUtil.isEmpty(mWsEndPoint)) process.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
                exceptionReference.set(e);
            }
        });

        // wait for browser created
        long start = System.currentTimeMillis();
        while (true) {
            if (!StringUtil.isEmpty(mWsEndPoint)) {
                logger.info("run: browser created");
                break;
            }
            if (Objects.nonNull(exceptionReference.get())) {
                throw new BrowserCreationException(exceptionReference.get().getMessage());
            }

            if (System.currentTimeMillis() - start > 30 * 1000) {
                throw new BrowserCreationException("creating browser timeout");
            }

            SystemUtil.sleep(100);
        }
    }

    public Connection createConnection() {
        logger.info("createConnection: wsEndPoint={}", mWsEndPoint);
        return Connection.create(mWsEndPoint);
    }

    public void kill() {
        // 拼接 shell 杀死进程
    }

    private static void close(Process process) {
        if (process != null && process.isAlive()) {
            process.destroy();
        }
    }
}
