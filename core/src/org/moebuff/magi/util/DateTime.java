package org.moebuff.magi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期时间工具
 *
 * @author MuTo
 */
public class DateTime {
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String dateFormat(Date d) {
        return dateFormat(d, DATE_PATTERN);
    }

    public static String dateFormat(Date d, String pattern) {
        return new SimpleDateFormat(pattern).format(d);
    }

    public static Date toDate(String s) {
        return toDate(s, DATE_PATTERN);
    }

    public static Date toDate(String s, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(s);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toDateStr(String s) {
        return toDateStr(s, DATE_PATTERN);
    }

    public static String toDateStr(String s, String pattern) {
        return dateFormat(toDate(s, pattern), pattern);
    }
}
