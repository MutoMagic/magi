package com.moebuff.magi.utils;

import com.badlogic.gdx.Application;
import org.slf4j.event.Level;

/**
 * 原先使用 {@link Application#log(String, String)} 和 {@link Application#log(String, String, Throwable)} 来实现
 * 警告日志，但这么做会导致 Android 输出的是info，经讨论决定将其重新实现，以避免逻辑上的错误。
 *
 * @author muto
 */
public interface ApplicationLogger {
    float LOG_INFO = Application.LOG_INFO;
    float LOG_ERROR = Application.LOG_ERROR;
    float LOG_WARN = (LOG_INFO + LOG_ERROR) / 2;

    /**
     * @param depth 深度归零，默认为 {@link #getTag(Level, int)}
     */
    String getTag(Level logLevel, int depth);

    void warn(String tag, String message);

    void warn(String tag, String message, Throwable exception);

    void setLogLevel(float logLevel);
}
