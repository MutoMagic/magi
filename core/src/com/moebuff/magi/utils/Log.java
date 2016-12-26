package com.moebuff.magi.utils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.moebuff.magi.reflect.ClassKit;
import com.moebuff.magi.reflect.LoaderUtil;
import com.moebuff.magi.reflect.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

/**
 * 为了让log更加便捷、快速，好的我编不下去了...
 * 这个gdx自带的log是什么鬼，为什么没有持久化，Are you fucking killing me ?!
 * <p>
 * 因此本类存在的意义就是让log持久化，至于保存在哪里（本地还是网络），请参考相应的配置文件。
 * 以下均采用 slf4j 作为 api，至于实现嘛，请自行谷歌。需要注意的就是 log4j 不能在 Android 中使用。
 * 对于原先自带的log，我也做了简单的封装，现在能自动生成tag，但并不支持持久化，那些方法不推荐使用。
 *
 * @author muto
 */
@SuppressWarnings("unchecked")
public final class Log {
    public static void d(String m, Object... args) {
        getLogger().debug(formatMessage(m, args));
    }

    public static void d(String m, Throwable e, Object... args) {
        getLogger().debug(formatMessage(m, args), e);
    }

    public static void i(String m, Object... args) {
        getLogger().info(formatMessage(m, args));
    }

    public static void i(String m, Throwable e, Object... args) {
        getLogger().info(formatMessage(m, args), e);
    }

    public static void w(String m, Object... args) {
        getLogger().warn(formatMessage(m, args));
    }

    public static void w(String m, Throwable e, Object... args) {
        getLogger().warn(formatMessage(m, args), e);
    }

    public static void e(String m, Object... args) {
        getLogger().error(formatMessage(m, args));
    }

    public static void e(String m, Throwable e, Object... args) {
        getLogger().error(formatMessage(m, args), e);
    }

    private static Logger getLogger() {
        Class<?> callerClass = ReflectionUtil.getCallerClass(3);
        UnhandledException.validate(callerClass != null, "无法获取上级类");
        return LoggerFactory.getLogger(callerClass);
    }

    public static String formatMessage(String m, Object... args) {
        return String.format(m, args);
    }

    //---------------------------------------------------------------------------------------------

    private static ApplicationLogger logger;

    private static final String LWJGL_GDX_LOGGER_PKG = "com.moebuff.magi.desktop.LwjglGdxLogger";
    private static final String ANDROID_GDX_LOGGER_PKG = "com.moebuff.magi.AndroidGdxLogger";

    static {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        Class<ApplicationLogger> gdxLoggerClass;
        try {
            gdxLoggerClass = LoaderUtil.loadClass(LWJGL_GDX_LOGGER_PKG);
        } catch (ClassNotFoundException ignored) {
            gdxLoggerClass = LoaderUtil.loadClass(ANDROID_GDX_LOGGER_PKG, true);
        }
        logger = ClassKit.newInstance(gdxLoggerClass);
        logger.setLogLevel(Gdx.app.getLogLevel());
    }

    public static void debug(String m, Object... args) {
        Gdx.app.debug(logger.getTag(Level.DEBUG, 2), formatMessage(m, args));
    }

    public static void debug(String m, Throwable e, Object... args) {
        Gdx.app.debug(logger.getTag(Level.DEBUG, 2), formatMessage(m, args), e);
    }

    public static void info(String m, Object... args) {
        Gdx.app.log(logger.getTag(Level.INFO, 2), formatMessage(m, args));
    }

    public static void info(String m, Throwable e, Object... args) {
        Gdx.app.log(logger.getTag(Level.INFO, 2), formatMessage(m, args), e);
    }

    public static void warn(String m, Object... args) {
        logger.warn(logger.getTag(Level.WARN, 2), formatMessage(m, args));
    }

    public static void warn(String m, Throwable e, Object... args) {
        logger.warn(logger.getTag(Level.WARN, 2), formatMessage(m, args), e);
    }

    public static void error(String m, Object... args) {
        Gdx.app.error(logger.getTag(Level.ERROR, 2), formatMessage(m, args));
    }

    public static void error(String m, Throwable e, Object... args) {
        Gdx.app.error(logger.getTag(Level.ERROR, 2), formatMessage(m, args), e);
    }
}
