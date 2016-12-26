package com.moebuff.magi.utils;

/**
 * 一些常用的数学运算
 *
 * @author muto
 */
public class Operation {
    /**
     * 将16进制数转换成 {@code float} 类型。
     */
    public static float toFloat(String hex) {
        int bits = Integer.parseInt(hex, 16);
        return Float.intBitsToFloat(bits);
    }

    /**
     * 将10进制 {@code float} 转换成16进制。
     */
    public static String toHex(float value) {
        int bits = Float.floatToIntBits(value);
        return Integer.toHexString(bits);
    }
}
