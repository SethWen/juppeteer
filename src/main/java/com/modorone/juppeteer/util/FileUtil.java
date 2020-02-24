package com.modorone.juppeteer.util;

import java.io.File;
import java.util.Objects;

/**
 * author: Shawn
 * time  : 2/24/20 4:06 PM
 * desc  :
 * update: Shawn 2/24/20 4:06 PM
 */
public class FileUtil {

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            for (String name : Objects.requireNonNull(dir.list())) {
                boolean success = deleteDir(new File(dir, name));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
