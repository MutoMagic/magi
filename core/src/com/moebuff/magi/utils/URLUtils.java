package com.moebuff.magi.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;

/**
 * URL 工具集
 *
 * @author muto
 */
public class URLUtils {
    private static final URLCodec codec = new URLCodec();

    /**
     * Decodes a URL safe string into its original form using the default string charset.
     * Escaped characters are converted back to their original representation.
     *
     * @param url URL safe string to convert into its original form
     * @return original string
     */
    public static String decode(String url) {
        try {
            return codec.decode(url);
        } catch (DecoderException e) {
            throw new UnhandledException(e);
        }
    }

    /**
     * Encodes a string into its URL safe form using the default string charset. Unsafe characters are escaped.
     *
     * @param url string to convert to a URL safe form
     * @return URL safe string
     */
    public static String encode(String url) {
        try {
            return codec.encode(url);
        } catch (EncoderException e) {
            throw new UnhandledException(e);
        }
    }
}
