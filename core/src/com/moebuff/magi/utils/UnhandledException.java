package com.moebuff.magi.utils;

import org.slf4j.helpers.FormattingTuple;

/**
 * 包装那些 已/未处理 的异常，并将它们扔出去。这些异常应在开发过程中被解决，或在运行时无效，后者通常用来记录日志。
 * 对于那些工具类来说，大多数的异常是方便开发者 {@code debug} 而使用的，建议将其包装，并在程序中杜绝。
 * 至于那些需要被忽略的异常，不推荐将其放置play，这样对调试很不友好；而那些需要被处理的异常，不要使用本类进行包装。
 * （最后那些不会处理异常的，或着不想处理异常的，请右转 -> 女装山脉
 *
 * @author muto
 */
public class UnhandledException extends RuntimeException {
    public UnhandledException() {
        super();
    }

    public UnhandledException(String message) {
        super(message);
    }

    public UnhandledException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnhandledException(Throwable cause) {
        super(cause);
    }

    @SuppressWarnings("NewApi") //support in Android API 24
    protected UnhandledException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * 使用 {@link Log#formatMessage(String, Object...)} 对 message 进行格式化，并创建一个新的异常。
     *
     * @param message 包含错误信息的格式字符串
     * @param values  格式字符串中由格式说明符引用的参数。参数的数目是可变的，可为 0。
     * @return 新创建的异常对象
     */
    public static RuntimeException format(String message, Object... values) {
        FormattingTuple tuple = Log.formatMessage(message, values);
        if (tuple.getThrowable() != null) {
            return new UnhandledException(tuple.getMessage(), tuple.getThrowable());
        }
        return new UnhandledException(tuple.getMessage());
    }

    /**
     * 验证表达式，若表达式不成立则抛出异常。抛出的异常由给定的 message 经格式化后创建。
     *
     * @param expression 表达式
     * @param message    包含错误信息的格式字符串
     * @param values     格式字符串中由格式说明符引用的参数。参数的数目是可变的，可为 0。
     */
    public static void validate(boolean expression, String message, Object... values) {
        if (!expression) throw format(message, values);
    }
}
