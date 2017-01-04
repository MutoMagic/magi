package org.slf4j.impl;

import com.moebuff.magi.BuildConfig;
import com.moebuff.magi.utils.Log;
import org.slf4j.event.Level;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;

import static org.slf4j.spi.LocationAwareLogger.*;

/**
 * This class serves as base for adapters or native implementations of
 * logging systems lacking format support.
 *
 * @author muto
 */
public abstract class MessageFormatBase extends MarkerIgnoringBase {

    /**
     * 忽略只有一个参数的方法，基于 {@link Throwable} 为null的实现。
     */
    public static abstract class IgnoreOnlyMessage extends MessageFormatBase {
        @Override
        public void trace(String msg) {
            trace(msg, (Throwable) null);
        }

        @Override
        public void debug(String msg) {
            debug(msg, (Throwable) null);
        }

        @Override
        public void info(String msg) {
            info(msg, (Throwable) null);
        }

        @Override
        public void warn(String msg) {
            warn(msg, (Throwable) null);
        }

        @Override
        public void error(String msg) {
            error(msg, (Throwable) null);
        }
    }

    //---------------------------------------------------------------------------------------------

    private int logLevel = Level.valueOf(BuildConfig.LOG_LEVEL).toInt();

    @Override
    public boolean isTraceEnabled() {
        return logLevel <= TRACE_INT;
    }

    @Override
    public void trace(String format, Object arg) {
        trace(format, new Object[]{arg});
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        trace(format, new Object[]{arg1, arg2});
    }

    @Override
    public void trace(String format, Object... arguments) {
        FormattingTuple tuple = Log.formatMessage(format, arguments);
        trace(tuple.getMessage(), tuple.getThrowable());
    }

    @Override
    public boolean isDebugEnabled() {
        return logLevel <= DEBUG_INT;
    }

    @Override
    public void debug(String format, Object arg) {
        debug(format, new Object[]{arg});
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        debug(format, new Object[]{arg1, arg2});
    }

    @Override
    public void debug(String format, Object... arguments) {
        FormattingTuple tuple = Log.formatMessage(format, arguments);
        debug(tuple.getMessage(), tuple.getThrowable());
    }

    @Override
    public boolean isInfoEnabled() {
        return logLevel <= INFO_INT;
    }

    @Override
    public void info(String format, Object arg) {
        info(format, new Object[]{arg});
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        info(format, new Object[]{arg1, arg2});
    }

    @Override
    public void info(String format, Object... arguments) {
        FormattingTuple tuple = Log.formatMessage(format, arguments);
        info(tuple.getMessage(), tuple.getThrowable());
    }

    @Override
    public boolean isWarnEnabled() {
        return logLevel <= WARN_INT;
    }

    @Override
    public void warn(String format, Object arg) {
        warn(format, new Object[]{arg});
    }

    @Override
    public void warn(String format, Object... arguments) {
        FormattingTuple tuple = Log.formatMessage(format, arguments);
        warn(tuple.getMessage(), tuple.getThrowable());
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        warn(format, new Object[]{arg1, arg2});
    }

    @Override
    public boolean isErrorEnabled() {
        return logLevel <= ERROR_INT;
    }

    @Override
    public void error(String format, Object arg) {
        error(format, new Object[]{arg});
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        error(format, new Object[]{arg1, arg2});
    }

    @Override
    public void error(String format, Object... arguments) {
        FormattingTuple tuple = Log.formatMessage(format, arguments);
        error(tuple.getMessage(), tuple.getThrowable());
    }
}
