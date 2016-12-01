package com.moebuff.magi.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * System Util
 *
 * @author muto
 */
public class OS {
    public static String name = System.getProperty("os.name");
    public static String arch = System.getProperty("os.arch");
    public static String abi = System.getProperty("sun.arch.abi");//JDK 8 only
    public static String tmpdir = System.getProperty("java.io.tmpdir");
    public static String user = System.getProperty("user.name");
    public static String home = System.getProperty("user.home");
    public static String library = System.getProperty("java.library.path");
    public static String classpath = System.getProperty("java.class.path");
    public static String project = getProjectName(OS.class);
    public static String location = StringUtils.EMPTY;

    public static boolean isWindows = name.contains("Windows");
    public static boolean isLinux = name.contains("Linux");
    public static boolean isMac = name.contains("Mac");
    public static boolean isIos = false;
    public static boolean isAndroid = false;
    public static boolean isARM = arch.startsWith("arm");
    public static boolean is64Bit = arch.equals("amd64") || arch.equals("x86_64");
    public static boolean isDesktop = false;

    static {
        if (abi == null) abi = "";
        tmpdir = FilenameUtils.concat(tmpdir, project + user);
        String vm = System.getProperty("java.runtime.name");
        if (vm != null && vm.contains("Android Runtime")) {
            isAndroid = true;
            isWindows = false;
            isLinux = false;
            isMac = false;
            is64Bit = false;
        }
        if (!isAndroid && !isWindows && !isLinux && !isMac) {
            isIos = true;
            is64Bit = false;
        }
        isDesktop = isWindows || isLinux || isMac;
        if (isDesktop) {
            URL local = OS.class.getProtectionDomain().getCodeSource().getLocation();
            location = URLUtils.decode(local.getPath());
        }
    }

    /**
     * 根据指定的类所在的包获取项目名
     */
    public static String getProjectName(Class c) {
        return getProjectName(c.getPackage(), 0);
    }

    /**
     * 根据指定的包获取项目名。在包的层次中，必须有一个包是项目名。若包的层次<=3，就取最后一个包名作为项目名；
     * 若包的层次>3，在子集默认为0前提下，只取第三个，但这并不是固定的。子集表示子项目在主项目中的位置，
     * 该位置必须体现在包的层次中，就好比主项目在层次3，子项目在层次4，那么子集为1，依次类推。子集是有取值范围的，
     * 最大值不得超过 {@code pkg.length-3}，最小值不能低于 {@code -2}，且子集只能在层次>3的环境中使用。
     *
     * @param p   包
     * @param sub 子集
     * @return 项目名
     */
    public static String getProjectName(Package p, int sub) {
        Validate.isTrue(p != null, "Package不能为null");
        String name = p.getName();
        if (name.contains(".")) {
            String[] pkg = name.split("\\.");
            name = pkg[pkg.length == 2 ? 1 : 2 + sub];
        }
        return name;
    }

    /**
     * 加载指定的系统库，支持多平台，命名规范需满足 {@link #mapLibraryName(String)}
     *
     * @param libraryName 库名
     */
    public static void loadLibrary(String libraryName) {
        new OS(null).load(libraryName);
    }

    private static final HashSet<String> loadedLibraries = new HashSet<>();

    private String nativesJar;

    /**
     * Fetches the natives from the given natives jar file. Used for testing a shared lib on the fly.
     */
    public OS(String nativesJar) {
        this.nativesJar = nativesJar;
    }

    /**
     * Maps a platform independent library name to a platform dependent name.
     */
    public String mapLibraryName(String libraryName) {
        if (isWindows) return libraryName + (is64Bit ? "64.dll" : ".dll");
        if (isLinux) return "lib" + libraryName + (isARM ? "arm" + abi : "") + (is64Bit ? "64.so" : ".so");
        if (isMac) return "lib" + libraryName + (is64Bit ? "64.dylib" : ".dylib");
        return libraryName;
    }

    /**
     * Loads a shared library for the platform the application is running on.
     *
     * @param libraryName The platform independent library name.
     *                    If not contain a prefix (eg lib) or suffix (eg .dll).
     */
    public void load(String libraryName) {
        // in case of iOS, things have been linked statically to the executable, bail out.
        if (isIos) return;

        synchronized (OS.class) {
            if (isLoaded(libraryName)) return;
            String platformName = mapLibraryName(libraryName);
            try {
                if (isAndroid) {
                    System.loadLibrary(platformName);
                } else {
                    loadFile(platformName);
                }
                setLoaded(libraryName);
            } catch (Throwable e) {
                throw UnhandledException.format(e, "Couldn't load shared library '%s' for target: %s, %s",
                        platformName, name, is64Bit ? "64-bit" : "32-bit");
            }
        }
    }

    /**
     * Extracts the source file and calls System.load. Attemps to extract and load from multiple locations.
     * Throws runtime exception if all fail.
     */
    private void loadFile(String sourcePath) {
        String sourceCrc = FCIV.crc(readFile(sourcePath));
        String fileName = new File(sourcePath).getName();

        // Temp directory with username in path.
        String path = String.format("%s/%s", tmpdir, sourceCrc);
        File f = new File(path, fileName);
        Throwable ex = loadFile(sourcePath, sourceCrc, f);
        if (ex == null) return;

        // System provided temp directory.
        try {
            f = File.createTempFile(sourceCrc, null);//file can be created
            if (f.delete() && loadFile(sourcePath, sourceCrc, f) == null) return;
        } catch (IOException e) {
            e.printStackTrace();//ignored
        }

        // User home.
        path = String.format("%s/.%s/%s", home, project, sourceCrc);
        f = new File(path, fileName);
        if (loadFile(sourcePath, sourceCrc, f) == null) return;

        // Relative directory.
        f = new File(".temp/" + sourceCrc, fileName);
        if (loadFile(sourcePath, sourceCrc, f) == null) return;

        // Fallback to java.library.path location, eg for applets.
        f = new File(library, sourcePath);
        if (f.exists()) {
            System.load(f.getAbsolutePath());
            return;
        }

        throw new UnhandledException(ex);
    }

    private InputStream readFile(String path) {
        if (nativesJar == null) {
            InputStream input = OS.class.getResourceAsStream("/" + path);
            UnhandledException.validate(input != null, "Unable to read file for extraction: %s", path);
            return input;
        }

        // Read from JAR.
        try {
            ZipFile file = new ZipFile(nativesJar);
            ZipEntry entry = file.getEntry(path);
            UnhandledException.validate(entry != null, "Couldn't find '%s' in JAR: %s", path, nativesJar);
            return file.getInputStream(entry);
        } catch (IOException e) {
            throw UnhandledException.format(e, "Error reading '%s' in JAR: %s", path, nativesJar);
        }
    }

    /**
     * @return null if the file was extracted and loaded.
     */
    private Throwable loadFile(String sourcePath, String sourceCrc, File extractedFile) {
        try {
            //无法通过路径直接获得压缩（.jar）包中的文件，必须在提取后才可使用
            System.load(extractFile(sourcePath, sourceCrc, extractedFile).getAbsolutePath());
            return null;
        } catch (Throwable e) {
            return e;
        }
    }

    private File extractFile(String sourcePath, String sourceCrc, File extractedFile) {
        String extractedCrc = null;
        if (extractedFile.exists()) {
            extractedCrc = FCIV.crc(extractedFile);
        }

        // If file doesn't exist or the CRC doesn't match, extract it to the temp dir.
        if (extractedCrc == null || !extractedCrc.equals(sourceCrc)) {
            extractedFile.getParentFile().mkdirs();
            try {
                InputStream input = readFile(sourcePath);
                FileOutputStream output = new FileOutputStream(extractedFile);
                byte[] buffer = new byte[4096];
                while (true) {
                    int length = input.read(buffer);
                    if (length == -1) break;
                    output.write(buffer, 0, length);
                }
                input.close();
                output.close();
            } catch (IOException e) {
                throw UnhandledException.format(e, "Error extracting file: %s\nTo: %s",
                        sourcePath, extractedFile.getAbsolutePath());
            }
        }
        return extractedFile;
    }

    public static synchronized boolean isLoaded(String libraryName) {
        return loadedLibraries.contains(libraryName);
    }

    /**
     * Sets the library as loaded, for when application code wants to handle libary loading itself.
     */
    public static synchronized void setLoaded(String libraryName) {
        loadedLibraries.add(libraryName);
    }
}
