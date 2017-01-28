package com.moebuff.magi.utils;

import com.moebuff.magi.reflect.ClassKit;
import org.slf4j.helpers.FormattingTuple;

/**
 * 摆脱对特定异常的限制，不局限于某个类型，提供用于处理异常信息的工具类。
 *
 * @author muto
 */
public class ExceptionKit {
    /**
     * 使用 {@link Log#formatMessage(String, Object...)} 对 message 进行格式化，并创建一个新的异常。
     * 该异常类型由参数 type 指定，为 {@link Throwable} 或其子类。
     *
     * @param type    异常类型
     * @param message 包含错误信息的格式字符串
     * @param values  格式字符串中由格式说明符引用的参数。参数的数目是可变的，可以为 0。
     *                通常将最后一个设置为 {@link Throwable cause}，如果有的话。
     * @return 新创建的异常对象
     */
    public static <T extends Throwable> T format(Class<T> type,
                                                 String message, Object... values) {
        FormattingTuple tuple = Log.formatMessage(message, values);
        if (tuple.getThrowable() != null) {
            return ClassKit.newInstance(type, tuple.getMessage(), tuple.getThrowable());
        }
        return ClassKit.newInstance(type, tuple.getMessage());
    }

    /**
     * 验证表达式，若表达式不成立则抛出异常。抛出的异常由给定的 message 经格式化后创建。
     * 该异常在运行时抛出，且为 {@link RuntimeException} 或其子类。
     *
     * @param type       异常类型
     * @param expression 表达式
     * @param message    包含错误信息的格式字符串
     * @param values     格式字符串中由格式说明符引用的参数。参数的数目是可变的，可以为 0。
     *                   通常将最后一个设置为 {@link Throwable cause}，如果有的话。
     */
    public static void validate(Class<? extends RuntimeException> type,
                                boolean expression, String message, Object... values) {
        if (!expression) throw format(type, message, values);
    }
}
