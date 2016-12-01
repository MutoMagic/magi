package com.moebuff.magi.io;

import com.moebuff.magi.utils.FCIV;
import com.moebuff.magi.utils.UnhandledException;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;

/**
 * ZipItem 压缩包中的文件对象
 * <p>
 * 这个类就是用来表示压缩包中具体文件而使用的，它的路径是虚拟路径，其空字符串表示压缩包中的根目录，因此必须使用相对路径名。
 * 由于使用到了临时文件，因此有些方法，比如 {@link #getAbsolutePath()} 所获取的值，就是临时文件的绝对路径。
 * 部分方法仅做了简单的逻辑判断，还有的方法保持不变，原因是 {@link File} 中的某些操作对于压缩包来说并没有什么卵用。
 * </p>
 * 本类并未支持任何添加和修改操作，如有需求请自行 {@code Override}。
 *
 * @author muto
 */
public class ZipItem extends File {
    private ZipPackage pkg;
    private ZipEntry entry;
    private File tmp;
    private long crc;

    public ZipItem(String pathname, ZipPackage pkg) {
        super(pathname);
        this.pkg = pkg;
        init();
    }

    public ZipItem(ZipItem parent, String child) {
        super(parent, child);
        pkg = parent.pkg;
        init();
    }

    private void init() {
        if (pkg == null) {
            throw new NullPointerException("无法在ZipPackage为null的情况下初始化");
        }
        entry = pkg.getEntry(this);
        tmp = pkg.getTmp(this);
        if (tmp.isFile() && tmp.length() > 0) {
            crc = FCIV.crc16(tmp);
        }
    }

    public void unpack(String path) {
        unpack(new File(path));
    }

    /**
     * 解压到指定的文件或目录
     * <p>
     * 当需要解压的是目录，dest必须为目录，该操作会将当前目录中的文件解压到指定的文件夹下。
     * 当需要解压的是文件，dest不可以为目录，若文件已存在则覆盖。
     * </p>
     *
     * @param dest 解压路径
     */
    public void unpack(File dest) {
        if (isDirectory()) {
            if (dest.exists()) {
                UnhandledException.validate(dest.isDirectory(), "dest 存在但是不是目录：%s",
                        dest.getPath());
            } else {
                UnhandledException.validate(dest.mkdirs(), "dest 目录无法被创建：%s",
                        dest.getPath());
            }
            ZipItem[] zs = listFiles();
            if (zs == null) return;
            for (ZipItem z : zs) {
                File c = new File(dest, z.getName());
                z.unpack(c);
            }
            return;
        }
        InputStream is = read();
        UnhandledException.validate(is != null, "找不到需要解压的文件 %s", getPath());
        FileExtension.copyToFile(is, dest);
    }

    public InputStream read() {
        return exists() ? pkg.read(entry) : null;
    }

    /**
     * 解压并读取压缩包中的压缩文件
     */
    public ZipPackage toPackage() {
        if (!exists()) return null;
        long c = entry.getCrc();
        if (crc != c) {
            unpack(tmp);
            crc = c;
        }
        return new ZipPackage(tmp);
    }

    //---------------------------------------------------------------------------------------------

    @Override
    public ZipItem getParentFile() {
        String p = getParent();
        if (p == null) return null;
        return new ZipItem(p, pkg);
    }

    /**
     * 之所以替换分隔符是因为在 {@link java.util.zip.ZipFile#getEntry(String)} 时只认 {@code /}
     */
    @Override
    public String getPath() {
        return super.getPath().replace("\\", "/");
    }

    @Override
    public String getAbsolutePath() {
        return tmp.getAbsolutePath();
    }

    @Override
    public ZipItem getAbsoluteFile() {
        return new ZipItem(getPath(), pkg);
    }

    @Override
    public String getCanonicalPath() {
        return FileExtension.getAbsolutePath(tmp);
    }

    @Override
    public ZipItem getCanonicalFile() {
        return new ZipItem(FileExtension.getCanonicalPath(this), pkg);
    }

    /**
     * 由于压缩包的原因，采用的是虚拟路径的绝对路径名，因此这里的返回值，理论上恒等于 true。
     * 鉴于直接返回 {@code true} 可能会导致不必要的麻烦，所以用 {@code tmp} 中相同的方法作为替代。
     */
    @Override
    public boolean isAbsolute() {
        return tmp.isAbsolute();
    }

    @Override
    public boolean canRead() {
        return exists() && tmp.canRead();
    }

    @Override
    public boolean canWrite() {
        return exists() && tmp.canWrite();
    }

    @Override
    public boolean exists() {
        return entry != null && tmp.exists();
    }

    @Override
    public boolean isDirectory() {
        return exists() ? entry.isDirectory() : tmp.isDirectory();
    }

    @Override
    public boolean isFile() {
        return tmp.isFile();
    }

    /**
     * 由于 {@code entry} 中没有相关方法，因此用 {@code tmp} 的代替
     * 需要注意的是，在类Unix系统上，若文件以点开头，则是隐藏文件。
     */
    @Override
    public boolean isHidden() {
        return tmp.isHidden();
    }

    /**
     * @return 条目的修改时间；如果未指定，则返回 -1
     */
    @Override
    public long lastModified() {
        return exists() ? entry.getTime() : -1;
    }

    /**
     * @return 条目数据的未压缩大小；如果未知，则返回 -1
     */
    @Override
    public long length() {
        return exists() ? entry.getSize() : -1;
    }

    @Override
    public boolean createNewFile() {
        return FileExtension.createNewFile(tmp, false);
    }

    @Override
    public boolean delete() {
        return tmp.delete();
    }

    @Override
    public void deleteOnExit() {
        tmp.deleteOnExit();
    }

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
        String[] ss = list();
        if (ss == null) return null;
        int n = ss.length;
        ZipItem[] zs = new ZipItem[n];
        for (int i = 0; i < n; i++) {
            zs[i] = new ZipItem(this, ss[i]);
        }
        return zs;
    }

    @Override
    public ZipItem[] listFiles(FilenameFilter filter) {
        String ss[] = list();
        if (ss == null) return null;
        ArrayList<ZipItem> list = new ArrayList<>();
        for (String s : ss) {
            if (filter == null || filter.accept(this, s)) {
                list.add(new ZipItem(this, s));
            }
        }
        return list.toArray(new ZipItem[list.size()]);
    }

    @Override
    public ZipItem[] listFiles(FileFilter filter) {
        String ss[] = list();
        if (ss == null) return null;
        ArrayList<ZipItem> list = new ArrayList<>();
        for (String s : ss) {
            ZipItem z = new ZipItem(this, s);
            if (filter == null || filter.accept(z)) {
                list.add(z);
            }
        }
        return list.toArray(new ZipItem[list.size()]);
    }

    @Override
    public boolean mkdir() {
        return tmp.mkdir();
    }

    @Override
    public boolean mkdirs() {
        return tmp.mkdirs();
    }

    @Override
    public boolean renameTo(File dest) {
        return tmp.renameTo(dest);
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) return true;
        if (exists() && obj != null && obj instanceof File) {
            long crc = entry.getCrc();
            if (obj instanceof ZipItem) {
                ZipItem z = (ZipItem) obj;
                return z.exists() ? z.entry.getCrc() == crc :
                        z.crc == crc;
            }
            return FCIV.crc16((File) obj) == crc;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return exists() ? entry.hashCode() : super.hashCode();
    }

    @Override
    public String toString() {
        return exists() ? entry.toString() : getPath();
    }
}

