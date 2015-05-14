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

    public Stream(InputStream in) {
        inTrans = toOutputStream(in);
        encoding = Encoding.getEncoding(copy(), decodeStream = new ByteArrayOutputStream());
    }

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
            if (c != null)
                c.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ByteArrayInputStream copy() {
        return new ByteArrayInputStream(inTrans.toByteArray());
    }

    // Properties
    // -------------------------------------------------------------------------

    public ByteArrayOutputStream getInTrans() {
        return inTrans;
    }

    public void setInTrans(ByteArrayOutputStream inTrans) {
        this.inTrans = inTrans;
    }

    public ByteArrayOutputStream getDecodeStream() {
        return decodeStream;
    }

    public void setDecodeStream(ByteArrayOutputStream decodeStream) {
        this.decodeStream = decodeStream;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
