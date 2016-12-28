package com.moebuff.magi;

import android.util.Log;
import com.moebuff.magi.reflect.ReflectionUtil;
import com.moebuff.magi.utils.ApplicationLogger;
import org.slf4j.event.Level;

public class AndroidGdxLogger implements ApplicationLogger {
    private float logLevel = LOG_INFO;

    @Override
    public String getTag(Level logLevel, int depth) {
        return ReflectionUtil.getEquivalentSte(depth + 1).toString();
    }

    @Override
    public void trace(String tag, String message) {
        if (logLevel >= LOG_TRACE) Log.v(tag, message);
    }

    @Override
    public void trace(String tag, String message, Throwable exception) {
        if (logLevel >= LOG_TRACE) Log.v(tag, message, exception);
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
