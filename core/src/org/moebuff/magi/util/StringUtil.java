package org.moebuff.magi.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * String工具
 *
 * @author MuTo
 */
public class StringUtil {
    /**
     * 表示null的字符串
     */
    public static final String NULL_STRING = "" + null;
    /**
     * 表示true的字符串
     */
    public static final String TRUE_STRING = "" + true;
    /**
     * 表示false的字符串
     */
    public static final String FALSE_STRING = "" + false;

    /**
     * 当且仅当s等于null或为null字符串时返回true。
     * <p>该方法当s不等于null时，会判断其内容是否为null字符串，此处忽略大小写。
     *
     * @param s 需要判断的字符串
     * @return 当且仅当s等于null或为null字符串时返回true。
     */
    public static boolean isNull(String s) {
        return s == null || s.equalsIgnoreCase(NULL_STRING);
    }

    /**
     * 当且仅当s等于null或{@link String#isEmpty() s.isEmpty()} 为true时返回true。
     *
     * @param s 需要判断的字符串
     * @return 当且仅当s等于null或 {@link String#isEmpty() s.isEmpty()} 为true时返回true。
     */
    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * 当且仅当{@link #isNull(String)} 为true或{@link String#isEmpty() s.isEmpty()} 为true时返回true。
     *
     * @param s 需要判断的字符串
     * @return 当且仅当{@link #isNull(String)} 为true或 {@link String#isEmpty() s.isEmpty()} 为true时返回true。
     */
    public static boolean isBlank(String s) {
        return isNull(s) || s.isEmpty();
    }

    /**
     * 判断s是否为布尔字符串。
     *
     * @param s 需要判断的字符串。
     * @return 如果s为布尔字符串则返回true，反之为false。
     */
    public static boolean isBoolean(String s) {
        return TRUE_STRING.equals(s) || FALSE_STRING.equals(s) ? true : false;
    }

    /**
     * 将s转换成对应的 {@link Boolean}值，如果转换失败则返回null。
     *
     * @param s 需要转换的字符串
     * @return 将s转换成对应的 {@link Boolean}值，如果转换失败则返回null。
     */
    public static Boolean toBoolean(String s) {
        if (TRUE_STRING.equals(s))
            return Boolean.TRUE;
        if (FALSE_STRING.equals(s))
            return Boolean.FALSE;
        return null;
    }

    /**
     * 首字母大写
     *
     * @param s 需要大写首字母的字符串
     * @return 首字母大写的字符串
     */
    public static String upperInitial(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param s 需要小写首字母的字符串
     * @return 首字母小写的字符串
     */
    public static String lowerInitial(String s) {
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }

    /**
     * 将s强转成int，若转换失败则返回{@code -1}。
     * <p>该方法内部调用{@link #toInt(String, int)}，beginIndex默认为{@code 0}。
     *
     * @param s 需要转换的字符串
     * @return 十进制表示的整数值
     */
    public static int toInt(String s) {
        return toInt(s, 0);
    }

    /**
     * 将s强转成int，若转换失败则返回{@code -1}。
     * <p>该方法会从s的beginIndex处开始，对每个字符依次调用{@link Character#isDigit(char)}检查
     * 其是否为数字，<b><i>不包含符号</i></b>。当遇到非数字后停止检查，并将已检查到的数字转换成int。
     *
     * @param s          需要转换的字符串
     * @param beginIndex 开始的index，包含。
     * @return 十进制表示的整数值
     */
    public static int toInt(String s, int beginIndex) {
        StringBuffer num = new StringBuffer();
        for (int i = beginIndex; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!Character.isDigit(c))
                break;
            num.append(c);
        }
        return isBlank(num.toString()) ? -1 : Integer.parseInt(num.toString());
    }

    public static int[] toInt(String[] array) {
        int[] ints = new int[array.length];
        for (int i = 0; i < array.length; i++)
            ints[i] = toInt(array[i]);
        return ints;
    }

    /**
     * 独立数字部分，并按照原有顺序排列。
     * <p>假设s的值为 <i>9501abcd12p</i> ，该方法会将数字部分和非数字部分隔开，
     * 并按照原有顺序组成一个{@link String} 数组：
     * <blockquote><pre>
     *     new String[]{
     *         "9501","abcd","12","p"
     *     }
     * </blockquote></pre>
     *
     * @param s 需要拆分的字符串
     * @return 按照原有顺序排列的字符串数组。
     */
    public static String[] splitNums(String s) {
        List split = new ArrayList();

        int i = 0;
        while (i < s.length()) {
            //数字部分
            int num = toInt(s, i);
            if (num != -1) {
                String n = Integer.toString(num);
                split.add(n);
                i += n.length();
            }

            //非数字部分
            StringBuffer str = new StringBuffer();
            while (i < s.length()) {
                char c = s.charAt(i++);
                if (Character.isDigit(c))
                    break;
                str.append(c);
            }
        }

        return Arrays.copyOf(split.toArray(), split.size(), String[].class);
    }

    /**
     * 将{@link String}数组拼接成单个字符串。
     *
     * @param args 需要转换的字符串数组
     * @return 拼接后的字符串
     */
    public static String toString(String[] args) {
        return toString(args, "");
    }

    public static String toString(String[] args, String split) {
        StringBuffer str = new StringBuffer();
        for (int i = 0; ; ) {
            str.append(args[i]);
            if (++i == args.length)
                break;
            str.append(split);
        }
        return str.toString();
    }

    /**
     * 按照字典顺序比较两个字符串，数字部分优先按数值大小进行比较。
     *
     * @param arg0 需要比较的字符串
     * @param arg1 需要比较的字符串
     * @return 如果arg0与arg1相同则返回0；如果arg0的顺序小于arg1则返回一个小于0的值；
     * 如果arg0的顺序大于arg1则返回一个大于0的值。
     */
    public static int compare(String arg0, String arg1) {
        String[] split0 = splitNums(arg0);
        String[] split1 = splitNums(arg1);

        int length = split0.length > split1.length ? split1.length : split0.length;
        for (int i = 0; i < length; i++) {
            int i1 = toInt(split0[i]);
            int i2 = toInt(split1[i]);

            //都是数字时根据数值大小进行排序
            if (i1 != -1 && i2 != -1 && i1 != i2)
                return i1 > i2 ? 1 : -1;

            int r = split0[i].compareTo(split1[i]);
            if (r != 0)//排除相同
                return r;
        }

        return arg0.compareTo(arg1);
    }

    public static void sort(String[] args) {
        Arrays.sort(args, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return compare(o1, o2);
            }
        });
    }

    public static String delNotes(String s) {
        return s.replaceAll("//.*", "").replaceAll("/\\*.*\\*/", "");
    }

    public static String arrayStyle(String s, int index) {
        return String.format("%s[%d]", s, index);
    }

    public static String cmdStyle(String arg0, String arg1) {
        return String.format("%s.%s", arg0, arg1);
    }
}
