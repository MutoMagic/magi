package com.moebuff.magi.io;

import com.moebuff.magi.utils.Log;
import com.moebuff.magi.utils.UnhandledException;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

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
        } catch (final IOException ioe) {
            Log.getLogger().debug("", ioe);
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
     * <code>OutputStream</code> using the default character encoding of the
     * platform.
     * <p>
     * This method uses {@link String#getBytes()}.
     *
     * @param data   the <code>String</code> to write, null ignored
     * @param output the <code>OutputStream</code> to write to
     * @throws NullPointerException if output is null
     * @throws UnhandledException   if an I/O error occurs
     */
    public static void write(final String data, final OutputStream output) {
        try {
            IOUtils.write(data, output, Charset.defaultCharset());
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
            byteArray.write(data.getBytes());
            byteArray.write(IOUtils.LINE_SEPARATOR.getBytes());
            byteArray.writeTo(output);
            output.flush();
        } catch (IOException e) {
            throw new UnhandledException(e);
        }
    }
}
