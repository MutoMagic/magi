package org.moebuff.magi.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 运行时工具
 *
 * @author MuTo
 */
public class FixedRuntime {
    /**
     * 判断obj数组及其元素是否等于null。
     *
     * @param obj 需要判断的{@link Object} 数组
     * @return 如果obj数组等于null则返回true；否则判断数组中个每一个元素，当且仅当所有元素不等于null时返回false。
     */
    public static boolean isNull(Object... obj) {
        if (obj == null)
            return true;
        for (int i = 0; i < obj.length; i++)
            if (obj[i] == null)
                return true;
        return false;
    }

    /**
     * 当obj等于null或obj数组长度为0时返回true。
     *
     * @param obj 需要判断的{@link Object} 数组
     * @return 当obj等于null或obj数组长度为0时返回true。
     */
    public static boolean isEmpty(Object[] obj) {
        return obj == null ? true : obj.length == 0;
    }

    /**
     * 判断arg0和arg1中的元素是否相同。
     *
     * @param arg0 需要判断的{@link Object}数组
     * @param arg1 需要判断的{@link Object}数组
     * @return 当且仅当arg0和arg1中的元素全部相同时返回true；否则返回false。
     */
    public static boolean same(Object[] arg0, Object[] arg1) {
        if (arg0.length != arg1.length)
            return false;
        for (int i = 0; i < arg0.length; i++)
            if (arg0[i] != arg1[i])
                return false;
        return true;
    }

    public static boolean exec(String command, boolean exception) {
        try {
            Process p = Runtime.getRuntime().exec(command);
            int exitValue = p.waitFor();
            if (exitValue != 0) {
                StringBuffer message = new StringBuffer("[Exit value: ");
                message.append(exitValue);
                message.append("] The command ");
                message.append("\"");
                message.append(command);
                message.append("\"");
                message.append(" cannot be executed. See: ");

                Stream c = new Stream(p.getErrorStream());
                InputStreamReader streamReader = new InputStreamReader(c.copy(), c.getEncoding());
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                String line = null;
                while (true) {
                    line = bufferedReader.readLine();
                    if (line == null)
                        break;
                    message.append("\n");
                    message.append(line);
                }
                bufferedReader.close();
                streamReader.close();

                throw new Exception(message.toString());
            }
            return true;
        } catch (Exception e) {
            if (exception)
                throw new RuntimeException(e);
            return false;
        }
    }

    public static boolean exec(String command) {
        return exec(command, false);
    }
}
