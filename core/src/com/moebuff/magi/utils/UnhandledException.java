package com.moebuff.magi.utils;

/**
 * 对于那些未处理的异常，将它们打包并扔出去
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

    @SuppressWarnings("NewApi")
    protected UnhandledException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    protected static String formatMessage(String message, Object... values) {
        return String.format(message, values);
    }

    /**
     * 使用 {@link String#format(String, Object...)} 对 message 进行格式化，并在之后用其创建一个新的异常
     *
     * @param message 包含错误信息的格式字符串
     * @param values  格式字符串中由格式说明符引用的参数。参数的数目是可变的，可为 0。
     * @return 新创建的异常对象
     */
    public static RuntimeException format(String message, Object... values) {
        return new UnhandledException(formatMessage(message, values));
    }

    /**
     * 使用 {@link String#format(String, Object...)} 对 message 进行格式化，之后用其和指定的原因创建一个新的异常
     *
     * @param cause   需要保留的原因
     * @param message 包含错误信息的格式字符串
     * @param values  格式字符串中由格式说明符引用的参数。参数的数目是可变的，可为 0。
     * @return 新创建的异常对象
     */
    public static RuntimeException format(Throwable cause, String message, Object... values) {
        return new UnhandledException(formatMessage(message, values), cause);
    }

    /**
     * 验证表达式，若表达式不成立则抛出异常。抛出的异常由给定的message经格式化后创建
     *
     * @param expression 表达式
     * @param message    包含错误信息的格式字符串
     * @param values     格式字符串中由格式说明符引用的参数。参数的数目是可变的，可为 0。
     */
    public static void validate(boolean expression, String message, Object... values) {
        if (!expression) throw format(message, values);
    }

    /**
     * 验证表达式，若表达式不成立则抛出异常。抛出的异常由指定的原因和格式化后的message创建
     *
     * @param expression 表达式
     * @param cause      需要保留的原因
     * @param message    包含错误信息的格式字符串
     * @param values     格式字符串中由格式说明符引用的参数。参数的数目是可变的，可为 0。
     */
    public static void validate(boolean expression, Throwable cause, String message, Object... values) {
        if (!expression) throw format(cause, message, values);
    }
}
