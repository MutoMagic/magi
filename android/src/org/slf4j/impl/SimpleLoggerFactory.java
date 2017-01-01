package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple implementation of SLF4J ILoggerFactory interface.
 *
 * @author muto
 */
public class SimpleLoggerFactory implements ILoggerFactory {
    /**
     * 缓存所有之前创建的 {@link Logger} 实例，每个相同名字的实例只需要创建一次。
     */
    private final Map<String, Logger> LOGGER_MAP;

    public SimpleLoggerFactory() {
        LOGGER_MAP = new HashMap<>();
    }

    @Override
    public Logger getLogger(String name) {
        Logger simpleLogger;
        // protect against concurrent access of the logger map.
        synchronized (this) {
            simpleLogger = LOGGER_MAP.get(name);
            //noinspection Java8ReplaceMapGet
            if (simpleLogger == null) {
                simpleLogger = new SimpleLogger(name);
                LOGGER_MAP.put(name, simpleLogger);
            }
        }
        return simpleLogger;
    }
}
