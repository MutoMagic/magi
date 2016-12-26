package com.moebuff.magi.io;

import com.moebuff.magi.utils.OS;
import com.moebuff.magi.utils.UnhandledException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * 文件工具
 *
 * @author muto
 * @see FilenameUtils
 * @see FileUtils
 */
public class FileKit {
    public static final File WKINGDIR = new File("").getAbsoluteFile();

    /**
     * 将 URL 转换成 URI
     */
    public static URI toURI(URL url) {
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            throw new UnhandledException(e);
        }
    }

    /**
     * 用 url 创建一个 file 对象
     */
    public static File toFile(URL url) {
        return new File(toURI(url));
    }

    /**
     * 获取内部资源，该资源位于 working directory 中；若运行的是jar，则从jar中获取。
     *
     * @param path 所需资源的路径
     * @return 指示该资源的 {@link File} 对象
     */
    public static File getResource(String path) {
        if (isDirectory(OS.location)) {
            return new File(WKINGDIR, path);
        }
        return new ZipPackage(OS.classpath).child(path);
    }

    /**
     * 测试此抽象路径名表示的文件是否是一个目录；若判断的文件不存在，则根据路径所示的文件名中是否存在后缀来判断。
     *
     * @param path 给定的抽象路径名
     * @return 当文件存在且是目录时返回 true；当文件不存在且不包含后缀名时返回 true；其他为 false。
     */
    public static boolean isDirectory(String path) {
        return isDirectory(new File(path));
    }

    public static boolean isDirectory(File f) {
        if (f.exists()) {
            return f.isDirectory();
        }
        String ex = FilenameUtils.getExtension(f.getName());
        return ex.isEmpty();
    }

    /**
     * Construct a file from the set of path.
     *
     * @param path   指定的文件路径
     * @param values 格式字符串中由格式说明符引用的参数。参数的数目是可变的，可为 0。
     * @return 由指定 {@code path} 创建的新文件对象
     */
    public static File getFile(String path, Object... values) {
        return new File(String.format(path, values));
    }

    /**
     * 当且仅当不存在具有此抽象路径名指定名称的文件时，不可分地创建一个新的空文件。它会在创建前先 {@link File#mkdirs()}，
     * 以确保父路径的存在。若该文件指示的是一个目录，并在 {@code mkdirs} 结束后，将其结果返回。
     *
     * @param f 需要新建的文件
     * @return 如果指定的文件不存在并成功地创建，则返回 true；如果指定的文件已经存在，则返回 false
     */
    public static boolean createNewFile(File f) {
        return createNewFile(f, isDirectory(f));
    }

    /**
     * 由于 {@code f} 的实体并不存在，因此给与 {@code isDir} 用来表示 {@code f} 是否为目录。
     * 若 {@code f} 是目录，那么在 mkdirs 后将其结果返回。若 {@code f} 是文件，则在其父目录 mkdirs 后创建空文件，
     * 如果在 {@link File#createNewFile()} 中抛出异常，文件就创建失败，但其父目录已创建完成。
     *
     * @param f     一个文件对象，但该对象指示的实体并不存在
     * @param isDir 表示 f 是否为目录
     * @return 如果 new file 成功则返回true，反之为false
     * @see #createNewFile(File)
     */
    public static boolean createNewFile(File f, boolean isDir) {
        if (isDir) {
            return f.mkdirs();
        }
        f.getParentFile().mkdirs();
        try {
            return f.createNewFile();
        } catch (IOException e) {
            throw new UnhandledException(e);
        }
    }

    /**
     * Copies bytes from an {@link InputStream} <code>source</code> to a file <code>destination</code>.
     * The directories up to <code>destination</code> will be created if they don't already exist.
     * <code>destination</code> will be overwritten if it already exists.
     * The {@code source} stream is left open, e.g. for use with {@link java.util.zip.ZipInputStream}.
     *
     * @param source      the <code>InputStream</code> to copy bytes from, must not be {@code null}
     * @param destination the non-directory <code>File</code> to write bytes to (possibly overwriting),
     *                    must not be {@code null}
     * @throws UnhandledException if <code>destination</code> is a directory
     * @throws UnhandledException if <code>destination</code> cannot be written
     * @throws UnhandledException if <code>destination</code> needs creating but can't be
     * @throws UnhandledException if an IO error occurs during copying
     * @see FileUtils#copyInputStreamToFile(InputStream, File) for a method that closes the input stream.
     */
    public static void copyToFile(InputStream source, File destination) {
        try {
            FileUtils.copyToFile(source, destination);
        } catch (IOException e) {
            throw new UnhandledException(e);
        }
    }

    /**
     * 返回此抽象路径名的规范路径名字符串。
     * <p>
     * 这里规范的是相对路径，与调用 getPath() 方法的效果一样。通常涉及到从路径名中移除多余的名称（比如 "." 和 ".."）、
     * 解析符号连接（对于 UNIX 平台），以及将驱动器号转换为标准大小写形式（对于 Microsoft Windows 平台）。
     * </p>
     *
     * @see FilenameUtils#concat(String, String)
     */
    public static String getCanonicalPath(File f) {
        return FilenameUtils.concat(f.getPath(), "");
    }

    /**
     * 返回此抽象路径名规范的绝对路径名字符串。
     * <p>
     * 如果此抽象路径名已经是绝对路径名，则返回该路径名字符串，这与 getPath() 方法一样。如果此抽象路径名是空抽象路径名，
     * 则返回当前用户目录的路径名字符串，该目录由系统属性 user.dir 指定。否则，使用与系统有关的方式解析此路径名。
     * 在 UNIX 系统上，根据当前用户目录解析相对路径名，可使该路径名成为绝对路径名。在 Microsoft Windows 系统上，
     * 根据路径名指定的当前驱动器目录（如果有）解析相对路径名，可使该路径名成为绝对路径名；否则，可以根据当前用户目录解析它。
     * </p>
     * 本质上与 {@link File#getCanonicalPath()} 的效果一样，只是免去了因构造规范路径名而需要进行的文件系统查询。
     *
     * @param f 表示此抽象路径的 File 对象
     * @return 绝对路径名字符串，它与此抽象路径名表示相同的文件或目录
     */
    public static String getAbsolutePath(File f) {
        return FilenameUtils.concat(f.getAbsolutePath(), "");
    }

    /**
     * Opens a {@link FileInputStream} for the specified file, providing better
     * error messages than simply calling <code>new FileInputStream(file)</code>.
     * <p>
     * At the end of the method either the stream will be successfully opened,
     * or an exception will have been thrown.
     * <p>
     * An exception is thrown if the file does not exist.
     * An exception is thrown if the file object exists but is a directory.
     * An exception is thrown if the file exists but cannot be read.
     *
     * @param f the file to open for input, must not be {@code null}
     * @return a new {@link FileInputStream} for the specified file
     * @throws UnhandledException if the file does not exist
     * @throws UnhandledException if the file object is a directory
     * @throws UnhandledException if the file cannot be read
     * @see FileUtils#openInputStream(File)
     */
    public static InputStream openInputStream(File f) {
        try {
            return FileUtils.openInputStream(f);
        } catch (IOException e) {
            throw new UnhandledException(e);
        }
    }
}
