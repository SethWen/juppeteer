package com.modorone.juppeteer.util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * author: Shawn
 * time  : 12/25/19 1:49 PM
 * desc  :
 * update: Shawn 12/25/19 1:49 PM
 */
public class SystemUtil {

    /**
     * 获取主机名
     *
     * @return 主机名
     */
    public static String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "unknown-hostname";
    }

    /**
     * 获取局域网 IP
     *
     * @return 局域网 IP
     */
    public static String getAreaIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "unknown-ip";
    }

    public static int getPid() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return Integer.parseInt(runtimeMXBean.getName().split("@")[0]);
    }

    /**
     * @param ms millisecond
     */
    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignore) {
        }
    }
}
