package com.moebuff.magi.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;

/**
 * URL 工具类
 *
 * @author muto
 */
public class URLUtils {
    private static final URLCodec codec = new URLCodec();

    /**
     * 使用默认的编码机制对 application/x-www-form-urlencoded 字符串解码。
     *
     * @param url 要解码的字符串
     * @return 新解码的字符串
     */
    public static String decode(String url) {
        try {
            return codec.decode(url);
        } catch (DecoderException e) {
            throw new UnhandledException(e);
        }
    }

    /**
     * 使用默认的编码机制将字符串转换为 application/x-www-form-urlencoded 格式。
     *
     * @param url 要转换的字符串
     * @return 已转换的字符串
     */
    public static String encode(String url) {
        try {
            return codec.encode(url);
        } catch (EncoderException e) {
            throw new UnhandledException(e);
        }
    }
}
