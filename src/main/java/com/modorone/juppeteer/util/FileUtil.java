package com.modorone.juppeteer.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

    public static void writeByteArrayToFile(File file, byte[] data) throws IOException {
        FileOutputStream out = null;

        try {
            out = openOutputStream(file);
            out.write(data);
        } finally {
            closeQuietly(out);
        }
    }

    public static FileOutputStream openOutputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }

            if (!file.canWrite()) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists() && !parent.mkdirs()) {
                throw new IOException("File '" + file + "' could not be created");
            }
        }

        return new FileOutputStream(file);
    }

    public static void closeQuietly(OutputStream output) {
        try {
            if (output != null) {
                output.close();
            }
        } catch (IOException var2) {
        }

    }
}
