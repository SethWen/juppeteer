package com.modorone.juppeteer;

import com.modorone.juppeteer.exception.BrowserCreationException;
import com.modorone.juppeteer.exception.JuppeteerException;
import com.modorone.juppeteer.util.FileUtil;
import com.modorone.juppeteer.util.StringUtil;
import com.modorone.juppeteer.util.SystemUtil;
import com.modorone.juppeteer.util.ThreadExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static java.util.Locale.ENGLISH;

/**
 * author: Shawn
 * time  : 2/23/20 10:28 PM
 * desc  :
 * update: Shawn 2/23/20 10:28 PM
 */
public class SpawnRunner implements IRunner {

    private static final Logger logger = LoggerFactory.getLogger(SpawnRunner.class);
    private String mExecutablePath = Constants.executablePath;
    private CompletableFuture<String> mWsEndPointFuture = new CompletableFuture<>();
    private Process mProcess;
    private String mUserDataDir;

    public SpawnRunner() {
    }

    public SpawnRunner(String executablePath) {
        if (StringUtil.isEmpty(executablePath)) throw new JuppeteerException("executable path can not be empty");

        mExecutablePath = executablePath;
    }

    @Override
    public String run(List<String> args) throws Exception {
        mUserDataDir = args.stream()
                .filter(arg -> StringUtil.startsWith(arg, "--user-data-dir"))
                .collect(Collectors.joining()).split("=", 2)[1];

        ThreadExecutor.getInstance().execute(() -> {
            try {
                ProcessBuilder builder = new ProcessBuilder();
                List<String> command = new ArrayList<String>() {{
                    add(mExecutablePath);
                    addAll(args);
                }};
                logger.debug("run: command={}", String.join(" ", command));
                builder.command(command);
                mProcess = builder.start();
                Scanner scanner = new Scanner(mProcess.getErrorStream());
                while (scanner.hasNext()) {
                    String line = scanner.nextLine().trim();
//                    System.out.println("line: " + line);
                    if (line.isEmpty()) continue;

                    if (line.toLowerCase(ENGLISH).startsWith("devtools listening on")) {
                        int start = line.indexOf("ws://");
                        mWsEndPointFuture.complete(line.substring(start));
                        break;
                    }
                }
                if (mWsEndPointFuture.isDone()) mProcess.waitFor();
            } catch (Exception e) {
                mWsEndPointFuture.completeExceptionally(e);
            }
        });

        String wsEndPoint;
        try {
            wsEndPoint = mWsEndPointFuture.get(30, TimeUnit.SECONDS);
            logger.debug("run: wsEndPoint={}", wsEndPoint);
        } catch (Exception e) {
            mProcess.destroyForcibly();
            FileUtil.deleteDir(new File(mUserDataDir));
            throw e;
        }
        return wsEndPoint;
    }

    @Override
    public void terminate() throws IOException {
        if (Objects.nonNull(mProcess) && mProcess.isAlive()) {
            mProcess.destroyForcibly();
            FileUtil.deleteDir(new File(mUserDataDir));
            mProcess = null;
        }
    }
}
