package com.moebuff.magi.io;

import com.moebuff.magi.utils.Log;
import com.moebuff.magi.utils.UnhandledException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharEncoding;

import java.io.*;

/**
 * General IO stream manipulation utilities.
 *
 * @author muto
 * @see IOUtils
 */
public class IOKit {
    /**
     * 关闭此流并释放与此流关联的所有系统资源。如果已经关闭该流，则调用此方法无效。
     *
     * @param closeable 可以关闭的数据源或目标
     */
    public static void closeQuietly(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (final IOException ignored) {
            Log.getLogger().debug("", ignored);
        }
    }

    /**
     * Writes bytes from a <code>byte[]</code> to an <code>OutputStream</code>.
     *
     * @param data   the byte array to write, do not modify during output, null ignored
     * @param output the <code>OutputStream</code> to write to
     * @throws NullPointerException if output is null
     * @throws UnhandledException   if an I/O error occurs
     */
    public static void write(final byte[] data, final OutputStream output) {
        try {
            IOUtils.write(data, output);
        } catch (IOException e) {
            throw new UnhandledException(e);
        }
    }

    /**
     * Writes chars from a <code>String</code> to bytes on an
     * <code>OutputStream</code> using the {@code UTF_8} character encoding
     * of the platform.
     * <p>
     * This method uses {@link String#getBytes(String)}.
     *
     * @param data   the <code>String</code> to write, null ignored
     * @param output the <code>OutputStream</code> to write to
     * @throws NullPointerException if output is null
     * @throws UnhandledException   if an I/O error occurs
     */
    public static void write(final String data, final OutputStream output) {
        try {
            IOUtils.write(data, output, CharEncoding.UTF_8);
        } catch (IOException e) {
            throw new UnhandledException(e);
        }
    }

    /**
     * 往输出流中写入一条字符串，并在写入后换行。
     *
     * @param data   需要写入的字符串，为 null 则忽略
     * @param output 被写入的输出流对象
     * @throws NullPointerException 如果 output 为 null
     * @throws UnhandledException   如果发生 I/O 错误
     */
    public static void writeln(final String data, final OutputStream output) {
        if (data == null) return;
        int size = data.length() + IOUtils.LINE_SEPARATOR.length();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream(size);
        try {
            byteArray.write(data.getBytes(CharEncoding.UTF_8));
            byteArray.write(IOUtils.LINE_SEPARATOR.getBytes(CharEncoding.UTF_8));
            byteArray.writeTo(output);
            output.flush();
        } catch (IOException e) {
            throw new UnhandledException(e);
        }
    }

    /**
     * Gets the contents of an <code>InputStream</code> as a String
     * using the {@code UTF_8} character encoding of the platform.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     *
     * @param input the <code>InputStream</code> to read from
     * @return the requested String
     * @throws NullPointerException if the input is null
     * @throws UnhandledException   if an I/O error occurs
     */
    public static String toString(final InputStream input) {
        try {
            return IOUtils.toString(input, CharEncoding.UTF_8);
        } catch (IOException e) {
            throw new UnhandledException(e);
        }
    }
}
