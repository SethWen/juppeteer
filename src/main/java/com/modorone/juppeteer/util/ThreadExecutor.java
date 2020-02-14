package com.modorone.juppeteer.util;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * author: Shawn
 * time  : 1/8/20 10:35 AM
 * desc  :
 * update: Shawn 1/8/20 10:35 AM
 */
public class ThreadExecutor extends ScheduledThreadPoolExecutor {

    private static ThreadExecutor mInstance;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE_SECONDS = 30;


    public ThreadExecutor(int corePoolSize) {
        super(corePoolSize);
    }

    public static ThreadExecutor getInstance() {
        if (mInstance == null) {
            synchronized (ThreadExecutor.class) {
                if (mInstance == null) {
                    mInstance = new ThreadExecutor(MAXIMUM_POOL_SIZE);
                }
            }
        }
        return mInstance;
    }
}

