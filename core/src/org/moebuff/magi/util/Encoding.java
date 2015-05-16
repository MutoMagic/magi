package org.moebuff.magi.util;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

/**
 * 编码工具
 *
 * @author MuTo
 */
public class Encoding {
    public static final String DEFAULT = "UTF-8";
    public static final String FILE_ENCODING = System.getProperty("file.encoding");
    public static final String JVM_DEFAULT = Charset.defaultCharset().name();
    public static final String[] CHARSETS = {
            FILE_ENCODING, JVM_DEFAULT,
            //American Standard Code for Information Interchange
            "ASCII",
            //Unicode
            DEFAULT, "UTF-16", "UTF-32",
            //ISO-8859
            "ISO-8859-1", "ISO-8859-2", "ISO-8859-3", "ISO-8859-4",
            "ISO-8859-6", "ISO-8859-7", "ISO-8859-8", "ISO-8859-9",
            "ISO-8859-11", "ISO-8859-13", "ISO-8859-15",
            //Chinese
            "Big5", "GB2312", "GBK", "GB18030",
            //Japanese
            "EUC-JP", "Shift-JIS", "Windows-31J",
    };

    /**
     * 获取in的编码类型。</br>
     * 尝试对in进行解码，若解码成功则返回其编码类型，并将结果写入out，反之则返回默认的编码类型。
     * <p>解码使用的编码类型请参阅{@link #CHARSETS}
     *
     * @param in  需要尝试解码的输入流
     * @param out 用于存放解码后的值
     * @return in使用的编码类型
     */
    public static String getEncoding(InputStream in, OutputStream out) {
        for (int i = 0; i < CHARSETS.length; i++)
            if (tryDecode(in, out, CHARSETS[i]))
                return CHARSETS[i];
        return DEFAULT;
    }

    public static boolean tryDecode(InputStream in, OutputStream out, String enc) {
        return tryDecode(in, out, Charset.forName(enc));
    }

    public static boolean tryDecode(InputStream in, OutputStream out, Charset c) {
        ReadableByteChannel channel = Channels.newChannel(in);
        CharsetDecoder d = c.newDecoder();

        ByteBuffer bb = ByteBuffer.allocate(2048);
        CharBuffer cb = CharBuffer.allocate(1024);

        CoderResult cr = null;
        boolean endOfInput = false;
        boolean error = false;
        while (!endOfInput) {
            try {
                endOfInput = channel.read(bb) == -1;
                bb.flip();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            do {
                cr = d.decode(bb, cb, endOfInput);
                error = drainCharBuffer(out, bb, cb, cr, error);
                cb.clear();
            } while (cr.isOverflow());
            if (cr.isError())
                return false;

            bb.compact();
        }

        do {
            cr = d.flush(cb);
            cb.clear();
        } while (cr.isOverflow());
        if (cr.isError())
            return false;

        return true;
    }

    private static boolean drainCharBuffer(OutputStream out, ByteBuffer bb, CharBuffer cb, CoderResult cr, boolean error) {
        cb.flip();
        try {
            Writer w = new OutputStreamWriter(out);
            if (cb.hasRemaining())
                w.write(cb.array());
            w.flush();
            w.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cb.clear();

        if (error = cr.isError())
            bb.position(bb.position() + cr.length());

        return error;
    }

    public static String urlEncoder(String url) {
        return urlEncoder(url, DEFAULT);
    }

    public static String urlEncoder(String url, String enc) {
        try {
            return URLEncoder.encode(url, enc);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String urlDecoder(String url) {
        return urlDecoder(url, DEFAULT);
    }

    public static String urlDecoder(String url, String enc) {
        try {
            return URLDecoder.decode(url, enc);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
