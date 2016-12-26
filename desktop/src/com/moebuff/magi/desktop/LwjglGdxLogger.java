package com.moebuff.magi.desktop;

import com.moebuff.magi.reflect.ReflectionUtil;
import com.moebuff.magi.utils.ApplicationLogger;
import com.moebuff.magi.utils.OS;
import org.slf4j.event.Level;

public class LwjglGdxLogger implements ApplicationLogger {
    private float logLevel = LOG_INFO;

    @Override
    public String getTag(Level logLevel, int depth) {
        Thread ct = Thread.currentThread();
        String tagName = String.format("%s %s-%s/%s %s/[dev]",
                OS.currentDateTime(),
                ct.getPriority(), ct.getId(), ct.getName(),
                logLevel.name().charAt(0));
        StackTraceElement ste = ReflectionUtil.getEquivalentStackTraceElement(depth + 1);
        return tagName + ste.toString();
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
