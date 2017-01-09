package com.moebuff.magi.utils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.moebuff.magi.reflect.ClassKit;
import com.moebuff.magi.reflect.LoaderUtil;
import com.moebuff.magi.reflect.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.util.HashMap;
import java.util.Map;

/**
 * 为了让log更加便捷、快速，好的我编不下去了...
 * 这个自带的log是什么鬼，为什么没有持久化，Are you fucking killing me ?!
 * <p>
 * 因此本类存在的意义就是让log持久化，保存在哪里（本地还是网络），请参考相应的配置文件。
 * 以下均采用 slf4j 作为 api，至于实现嘛，请自行谷歌。需要注意的就是 log4j 不能在 Android 中使用。
 * 对于原先自带的log，我也做了简单的封装，现在能自动生成tag，但并不支持持久化，这些方法不推荐使用。
 * <p>
 * 理解正确的日志输出级别。很多程序员都忽略了日志输出级别，甚至不知道如何指定日志的输出级别。
 * 对于日志输出级别来说, 下面是我们应该记住的一些原则:
 * ERROR:系统发生了严重的错误, 必须马上进行处理, 否则系统将无法继续运行. 比如, NPE, 数据库不可用等.
 * WARN:系统能继续运行, 但是必须引起关注. 对于存在的问题一般可以分为两类: 一种系统存在明显的问题(比如, 数据不可用),
 * 另一种就是系统存在潜在的问题, 需要引起注意或者给出一些建议(比如, 系统运行在安全模式或者访问当前系统的账号存在安全隐患).
 * 总之就是系统仍然可用, 但是最好进行检查和调整.
 * INFO:重要的业务逻辑处理完成. 在理想情况下, INFO的日志信息要能让高级用户和系统管理员理解,
 * 并从日志信息中能知道系统当前的运行状态. 比如对于一个机票预订系统来说, 当一个用户完成一个机票预订操作之后,
 * 提醒应该给出"谁预订了从A到B的机票". 另一个需要输出INFO信息的地方就是一个系统操作引起系统的状态发生了重大变化
 * (比如数据库更新, 过多的系统请求).
 * DEBUG:指明细致的事件信息，对调试应用最有用。
 * TRACE:系统详细信息, 主要给开发人员用, 一般来说, 如果是线上系统的话, 可以认为是临时输出, 而且随时可以通过开关将其关闭.
 * 有时候我们很难将DEBUG和TRACE区分开, 一般情况下, 如果是一个已经开发测试完成的系统, 再往系统中添加日志输出,
 * 那么应该设为TRACE级别.
 *
 * @author muto
 */
public class Log {
    private static final Map<Class, Logger> LOGGER_MAP = new HashMap<>();
    private static final String LWJGL_GDX_LOGGER_PKG = "com.moebuff.magi.desktop.LwjglGdxLogger";
    private static final String ANDROID_GDX_LOGGER_PKG = "com.moebuff.magi.AndroidGdxLogger";

    private static ApplicationLogger logger;

    static {
        Class<ApplicationLogger> gdxLoggerClass;
        try {
            gdxLoggerClass = LoaderUtil.loadClass(LWJGL_GDX_LOGGER_PKG);
        } catch (ClassNotFoundException ignored) {
            gdxLoggerClass = LoaderUtil.loadClass(ANDROID_GDX_LOGGER_PKG, true);
        }
        logger = ClassKit.newInstance(gdxLoggerClass);

        //请不要修改这些级别，以免导致无法打印初始化信息
        logger.setLogLevel(ApplicationLogger.LOG_TRACE);
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }

    public static Logger getLogger() {
        Class<?> caller = ReflectionUtil.getCallerClass(2);
        if (caller == null) {
            caller = Log.class;
        }

        Logger result;
        synchronized (Log.class) {
            result = LOGGER_MAP.get(caller);
            if (result == null) {
                result = LoggerFactory.getLogger(caller);
                LOGGER_MAP.put(caller, result);
            }
        }
        return result;
    }

    /**
     * 一种用于格式化字符串的方法，同时支持 {@link String#format(String, Object...)} 和 {@link MessageFormatter}，
     * 其结果为 {@link FormattingTuple} 对象，从中可获取格式化后的信息，以及对应的错误原因。
     *
     * @param m    包含错误信息的格式字符串
     * @param args 格式字符串中由格式说明符引用的参数。参数的数目是可变的，可以为 0。
     *             通常将最后一个设置为 {@link Throwable cause}，如果有的话。
     * @return 格式化消息对象
     */
    public static FormattingTuple formatMessage(String m, Object... args) {
        return MessageFormatter.arrayFormat(String.format(m, args), args);
    }

    public static void trace(String m, Object... args) {
        String tag = logger.getTag(Level.TRACE, 2);
        FormattingTuple tuple = formatMessage(m, args);
        if (tuple.getThrowable() != null) {
            logger.trace(tag, tuple.getMessage(), tuple.getThrowable());
            return;
        }
        logger.trace(tag, tuple.getMessage());
    }

    public static void debug(String m, Object... args) {
        String tag = logger.getTag(Level.DEBUG, 2);
        FormattingTuple tuple = formatMessage(m, args);
        if (tuple.getThrowable() != null) {
            Gdx.app.debug(tag, tuple.getMessage(), tuple.getThrowable());
            return;
        }
        Gdx.app.debug(tag, tuple.getMessage());
    }

    public static void info(String m, Object... args) {
        String tag = logger.getTag(Level.INFO, 2);
        FormattingTuple tuple = formatMessage(m, args);
        if (tuple.getThrowable() != null) {
            Gdx.app.log(tag, tuple.getMessage(), tuple.getThrowable());
            return;
        }
        Gdx.app.log(tag, tuple.getMessage());
    }

    public static void warn(String m, Object... args) {
        String tag = logger.getTag(Level.WARN, 2);
        FormattingTuple tuple = formatMessage(m, args);
        if (tuple.getThrowable() != null) {
            logger.warn(tag, tuple.getMessage(), tuple.getThrowable());
            return;
        }
        logger.warn(tag, tuple.getMessage());
    }

    public static void error(String m, Object... args) {
        String tag = logger.getTag(Level.ERROR, 2);
        FormattingTuple tuple = formatMessage(m, args);
        if (tuple.getThrowable() != null) {
            Gdx.app.error(tag, tuple.getMessage(), tuple.getThrowable());
            return;
        }
        Gdx.app.error(tag, tuple.getMessage());
    }
}
