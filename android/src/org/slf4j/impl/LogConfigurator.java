package org.slf4j.impl;

import com.moebuff.magi.BuildConfig;
import org.slf4j.event.Level;

import java.io.File;

/**
 * 日志配置
 *
 * @author muto
 */
public class LogConfigurator {
    public static final Level LOG_LEVEL = Level.valueOf(BuildConfig.LOG_LEVEL);
    public static final int LOG_SIZE = BuildConfig.LOG_SIZE;//MB
    public static final File LOG_FILE = new File(BuildConfig.LOG_FILENAME);
    public static final boolean LOG_APPEND = BuildConfig.LOG_APPEND;
}
