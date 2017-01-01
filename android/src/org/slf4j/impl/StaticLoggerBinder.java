package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

/**
 * This class is part of the required classes used to specify an SLF4J logger provider implementation.
 *
 * @author muto
 */
public class StaticLoggerBinder implements LoggerFactoryBinder {
    /**
     * Declare the version of the SLF4J API this implementation is compiled against.
     * The value of this field is usually modified with each release.
     * <p>
     * to avoid constant folding by the compiler, this field must *not* be final.
     */
    public static String REQUESTED_API_VERSION = "1.7"; // !final

    private static final String LOGGER_FACTORY_CLASS_STR = SimpleLoggerFactory.class.getName();

    /**
     * The unique instance of this class.
     */
    private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

    /**
     * The ILoggerFactory instance returned by the {@link #getLoggerFactory}
     * method should always be the same object.
     */
    private final ILoggerFactory LOGGER_FACTORY;

    private StaticLoggerBinder() {
        LOGGER_FACTORY = new SimpleLoggerFactory();
    }

    /**
     * @return the singleton of this class.
     */
    public static StaticLoggerBinder getSingleton() {
        return SINGLETON;
    }

    @Override
    public ILoggerFactory getLoggerFactory() {
        return LOGGER_FACTORY;
    }

    @Override
    public String getLoggerFactoryClassStr() {
        return LOGGER_FACTORY_CLASS_STR;
    }
}
