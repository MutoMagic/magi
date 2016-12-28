package com.moebuff.magi.desktop;

import com.moebuff.magi.reflect.ReflectionUtil;
import com.moebuff.magi.utils.ApplicationLogger;
import com.moebuff.magi.utils.OS;
import org.slf4j.event.Level;

public class LwjglGdxLogger implements ApplicationLogger {
    private static final String LOG4J_ASYNC_CONTEXT_SELECTOR
            = "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector";
    private float logLevel = LOG_INFO;

    static {
        System.setProperty("Log4jContextSelector", LOG4J_ASYNC_CONTEXT_SELECTOR);
    }

    @Override
    public String getTag(Level logLevel, int depth) {
        Thread ct = Thread.currentThread();
        StackTraceElement ste = ReflectionUtil.getEquivalentSte(depth + 1);
        return String.format("%s %s-%s/%s %s/%s",
                OS.currentDateTime(),
                ct.getPriority(), ct.getId(), ct.getName(),
                logLevel.name().charAt(0), ste);
    }

    @Override
    public void trace(String tag, String message) {
        if (logLevel >= LOG_TRACE) {
            System.out.println(tag + ": " + message);
        }
    }

    @Override
    public void trace(String tag, String message, Throwable exception) {
        if (logLevel >= LOG_TRACE) {
            System.out.println(tag + ": " + message);
            exception.printStackTrace(System.out);
        }
    }

    @Override
    public void warn(String tag, String message) {
        if (logLevel >= LOG_WARN) {
            System.out.println(tag + ": " + message);
        }
    }

    @Override
    public void warn(String tag, String message, Throwable exception) {
        if (logLevel >= LOG_WARN) {
            System.out.println(tag + ": " + message);
            exception.printStackTrace(System.out);
        }
    }

    @Override
    public void setLogLevel(float logLevel) {
        this.logLevel = logLevel;
    }
}
