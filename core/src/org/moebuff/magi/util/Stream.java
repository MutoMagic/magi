package org.moebuff.magi.util;

import org.moebuff.magi.util.Encoding;

import java.io.*;

/**
 * 流工具
 *
 * @author MuTo
 */
public class Stream {
    private ByteArrayOutputStream inTrans;
    private ByteArrayOutputStream decodeStream;
    private String encoding;

    /**
     * 将输入流转换成输出流。
     *
     * @param in 需要转换的输入流
     * @return 转换后的输出流
     */
    public static ByteArrayOutputStream toOutputStream(InputStream in) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        byte[] b = new byte[1024];
        try {
            for (int i = 0; i != -1; i = in.read(b))
                out.write(b, 0, i);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return out;
    }

    public static void close(Closeable c) {
        try {
            if (!FixedRuntime.isNull(c))
                c.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void close(Closeable... args) {
        for (int i = 0; i < args.length; i++)
            close(args[i]);
    }

    public Stream(InputStream in) {
        inTrans = toOutputStream(in);
        encoding = Encoding.getEncoding(copy(), decodeStream = new ByteArrayOutputStream());
    }

    public ByteArrayInputStream copy() {
        return new ByteArrayInputStream(inTrans.toByteArray());
    }

    public String getEncoding() {
        return encoding;
    }
}
