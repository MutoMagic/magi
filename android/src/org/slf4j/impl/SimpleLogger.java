package org.slf4j.impl;

import com.orhanobut.logger.Logger;

/**
 * 简易日志
 *
 * @author muto
 */
public class SimpleLogger extends MessageFormatBase.IgnoreOnlyMessage {
    static {
        Logger.init().methodCount(5);
    }

    public SimpleLogger(String name) {
        this.name = name;
    }

    @Override
    public void trace(String msg, Throwable t) {
        if (isTraceEnabled()) {
            Logger.log(Logger.VERBOSE, null, msg, t);
        }
    }

    @Override
    public void debug(String msg, Throwable t) {
        if (isDebugEnabled()) {
            Logger.log(Logger.DEBUG, null, msg, t);
        }
    }

    @Override
    public void info(String msg, Throwable t) {
        if (isInfoEnabled()) {
            Logger.log(Logger.INFO, null, msg, t);
        }
    }

    @Override
    public void warn(String msg, Throwable t) {
        if (isWarnEnabled()) {
            Logger.log(Logger.WARN, null, msg, t);
        }
    }

    @Override
    public void error(String msg, Throwable t) {
        if (isErrorEnabled()) {
            Logger.log(Logger.ERROR, null, msg, t);
        }
    }
}
