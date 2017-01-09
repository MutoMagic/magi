package org.slf4j.impl;

import android.os.Environment;
import android.util.Log;
import com.moebuff.magi.io.FileKit;
import com.moebuff.magi.io.IOKit;
import com.moebuff.magi.utils.OS;
import com.orhanobut.logger.LogAdapter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.OutputStream;

public class AndroidLogAdapter implements LogAdapter {
    @Override
    public void d(String tag, String message) {
        log(Log.DEBUG, tag, message);
    }

    @Override
    public void e(String tag, String message) {
        log(Log.ERROR, tag, message);
    }

    @Override
    public void w(String tag, String message) {
        log(Log.WARN, tag, message);
    }

    @Override
    public void i(String tag, String message) {
        log(Log.INFO, tag, message);
    }

    @Override
    public void v(String tag, String message) {
        log(Log.VERBOSE, tag, message);
    }

    @Override
    public void wtf(String tag, String message) {
        Log.wtf(tag, message);
    }

    //---------------------------------------------------------------------------------------------

    private static final File LOG_FILE = FileKit.getFile("%s/magi!csga/last.log",
            Environment.getExternalStorageDirectory().getAbsolutePath());
    private static final OutputStream LOG_STREAM = FileKit.openOutputStream(LOG_FILE, false);

    private void log(int level, String tag, String message) {
        tag = StringUtils.substringAfter(tag, "-");
        write(print(level, tag, message), tag, message);
    }

    private String print(int level, String tag, String message) {
        switch (level) {
            case Log.VERBOSE:
                Log.v(tag, message);
                return "V";
            case Log.DEBUG:
                Log.d(tag, message);
                return "D";
            case Log.INFO:
                Log.i(tag, message);
                break;
            case Log.WARN:
                Log.w(tag, message);
                return "W";
            case Log.ERROR:
                Log.e(tag, message);
                return "E";
        }
        return "I";
    }

    private void write(String level, String tag, String message) {
        Thread t = Thread.currentThread();
        String msg = String.format("%s %s-%s/%s %s/%s: %s",
                OS.currentDateTime(),
                t.getPriority(), t.getId(), t.getName(),
                level, tag, message);
        IOKit.writeln(msg, LOG_STREAM);
    }
}
