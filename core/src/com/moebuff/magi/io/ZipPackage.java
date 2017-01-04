package com.moebuff.magi.io;

import com.moebuff.magi.utils.OS;
import com.moebuff.magi.utils.UnhandledException;
import org.apache.commons.lang3.Validate;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * ZipItem 压缩包
 * <p>
 * 被操作的对象是整个压缩包，而不是包中某一个文件，如果要操作包中具体的文件，需要使用 {@link ZipItem} 这个类。ZipPackage
 * 本质上就是一个 {@link File} 对象，为了保证被操作的只是压缩包本身，因此仅重写了部分方法，比如 {@link #list()} 等。
 * 其实被重写的这些方法，本意上也就是用来操作压缩包中的文件而已，不会对压缩包本身的操作起到任何影响。
 *
 * @author muto
 */
public class ZipPackage extends File {
    /**
     * 解压 {@code source} 到指定的目录
     *
     * @param source 压缩包文件
     * @param dest   解压目录
     */
    public static void unpack(File source, File dest) {
        ZipFile file = getFile(source);
        unpack(file, dest);
        IOKit.closeQuietly(file);
    }

    private static void unpack(ZipFile source, File dest) {
        Validate.isTrue(dest != null, "The destination must not be null");
        Validate.isTrue(!dest.exists() || dest.isDirectory(), "File exists but is not a directory");

        Enumeration<? extends ZipEntry> entries = source.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            File out = new File(dest, entry.getName());
            if (entry.isDirectory()) {
                out.mkdirs();//创建空目录
                continue;
            }
            FileKit.copyToFile(read(source, entry), out);
        }
    }

    /**
     * @return 压缩包中指定 {@link ZipEntry entry} 的 {@link InputStream}
     */
    private static InputStream read(ZipFile file, ZipEntry entry) {
        try {
            return file.getInputStream(entry);
        } catch (IOException e) {
            throw new UnhandledException(e);
        }
    }

    /**
     * 根据指定的文件创建 {@link ZipFile}
     */
    private static ZipFile getFile(File f) {
        try {
            return new ZipFile(f);
        } catch (IOException e) {
            throw new UnhandledException(e);
        }
    }

    //---------------------------------------------------------------------------------------------

    private ZipFile file;
    private File tmp;

    /**
     * 读取压缩包，并生成空临时文件，目的是为了提供目录结构。
     * 生成的临时文件根目录名由 {@link FCIV#crc(File)} 提供。
     *
     * @param path 指示压缩包文件的抽象路径
     */
    public ZipPackage(String path) {
        super(path);
        file = getFile(this);
        String crc = FCIV.crc(this);
        tmp = FileKit.getFile("%s/jzip/%s", OS.cache, crc);
        if (tmp.exists()) return;
        Enumeration<? extends ZipEntry> entries = file.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            FileKit.createNewFile(new File(tmp, entry.getName()),
                    entry.isDirectory());
        }
    }

    public ZipPackage(File f) {
        this(FileKit.getCanonicalPath(f));
    }

    public void unpack(File dest) {
        unpack(file, dest);
    }

    public InputStream read(ZipEntry entry) {
        return read(file, entry);
    }

    public ZipEntry getEntry(ZipItem z) {
        return file.getEntry(z.getPath());
    }

    /**
     * @return 指定 {@link ZipItem} 的临时文件对象
     */
    public File getTmp(ZipItem z) {
        return new File(tmp, z.getPath());
    }

    public ZipItem child(String name) {
        return new ZipItem(name, this);
    }

    //---------------------------------------------------------------------------------------------

    @Override
    public String[] list() {
        return tmp.list();
    }

    @Override
    public String[] list(FilenameFilter filter) {
        return tmp.list(filter);
    }

    @Override
    public ZipItem[] listFiles() {
        String[] names = list();
        if (names == null) return null;
        ZipItem[] result = new ZipItem[names.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = new ZipItem(names[i], this);
        }
        return result;
    }

    @Override
    public ZipItem[] listFiles(FilenameFilter filter) {
        String[] names = list();
        if (names == null) return null;
        ArrayList<ZipItem> list = new ArrayList<>();
        for (String n : names) {
            if (filter == null || filter.accept(tmp, n)) {
                list.add(new ZipItem(n, this));
            }
        }
        return list.toArray(new ZipItem[list.size()]);
    }

    @Override
    public ZipItem[] listFiles(FileFilter filter) {
        String[] names = list();
        if (names == null) return null;
        ArrayList<ZipItem> list = new ArrayList<>();
        for (String n : names) {
            ZipItem z = new ZipItem(n, this);
            if (filter == null || filter.accept(z)) {
                list.add(z);
            }
        }
        return list.toArray(new ZipItem[list.size()]);
    }
}
