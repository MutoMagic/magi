package org.moebuff.magi.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * 路径工具
 *
 * @author MuTo
 */
public class PathUtil {
    public static final String USERDIR = System.getProperty("user.dir");
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    public static String getPath(Class c) {
        String path = c.getResource("").getPath();
        return new File(path).getAbsolutePath();
    }

    public static String getPath(Object obj) {
        String path = obj.getClass().getResource("").getPath();
        return new File(path).getAbsolutePath();
    }

    public static String getWebRoot() {
        try {
            String path = PathUtil.class.getResource("/").getPath();
            return new File(path).getParentFile().getParentFile().getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getClassPath() {
        try {
            String path = PathUtil.class.getResource("/").getPath();
            return new File(path).getAbsolutePath();
        } catch (Exception e) {
            String path = PathUtil.class.getClassLoader().getResource("").getPath();
            return new File(path).getAbsolutePath();
        }
    }

    public static String getPackagePath(Object obj) {
        Package p = obj.getClass().getPackage();
        return p == null ? null : p.getName().replaceAll("\\.", "/");
    }

    public static String addSeparator(String... args) {
        return FixedRuntime.isEmpty(args) ? addSeparator(true, args) : addSeparator(false, args);
    }

    public static String addSeparator(boolean addLast, String... args) {
        if (args == null)
            return null;
        if (args.length == 0)
            return addLast ? FILE_SEPARATOR : "";
        StringBuffer r = new StringBuffer();
        for (int i = 0; ; ) {
            r.append(args[i]);
            if (++i == args.length)
                break;
            r.append(FILE_SEPARATOR);
        }
        return addLast ? r.append(FILE_SEPARATOR).toString() : r.toString();
    }
}
