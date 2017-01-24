package com.moebuff.magi.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.jar.JarFile;

/**
 * URL 工具类
 *
 * @author muto
 */
public class URLUtils {

    /**
     * 为 Lambda 而生，它允许你通过表达式来代替功能接口。
     */
    public interface URLGetter {
        String getURL();//获取URL字符串
    }

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
     * 原则上和 {@link #decode(String)} 没有区别，主要针对 Lambda 表达式，
     * 通过 {@code 方法引用::} 简化调用嵌套，从而增强代码的可读性。
     */
    public static String decode(URLGetter getter) {
        return decode(getter.getURL());
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
     * 返回一个 URLConnection 对象，它表示到 URL 所引用的远程对象的连接。
     *
     * @param <T> 便于类型转换，必须是 URLConnection 的子类
     */
    public static <T extends URLConnection> T openConnection(URL url) {
        try {
            //noinspection unchecked
            return (T) url.openConnection();
        } catch (IOException e) {
            throw new UnhandledException(e);
        }
    }

    /**
     * 返回此连接的 JAR 文件。需要保证指定的 URL，其协议名为 jar，否则将触发类型转换错误。
     */
    public static JarFile getJarFile(URL url) {
        JarURLConnection connection = openConnection(url);
        try {
            return connection.getJarFile();
        } catch (IOException e) {
            throw new UnhandledException(e);
        }
    }

}
