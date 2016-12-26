package com.moebuff.magi;

import android.util.Log;
import com.moebuff.magi.reflect.ReflectionUtil;
import com.moebuff.magi.utils.ApplicationLogger;
import org.slf4j.event.Level;

public class AndroidGdxLogger implements ApplicationLogger {
    private float logLevel = LOG_INFO;

    @Override
    public String getTag(Level logLevel, int depth) {
        return "[dev]" + ReflectionUtil.getEquivalentStackTraceElement(depth + 1);
    }

    @Override
    public void warn(String tag, String message) {
        if (logLevel >= LOG_WARN) Log.w(tag, message);
    }

    @Override
    public void warn(String tag, String message, Throwable exception) {
        if (logLevel >= LOG_WARN) Log.w(tag, message, exception);
    }

    @Override
    public void setLogLevel(float logLevel) {
        this.logLevel = logLevel;
    }
}
