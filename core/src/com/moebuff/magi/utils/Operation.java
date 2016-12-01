package com.moebuff.magi.utils;

/**
 * 一些常用的数学运算
 *
 * @author muto
 */
public class Operation {
    /**
     * 将16进制转换为 float 类型
     */
    public static float hexToFloat(String hex) {
        int bits = Integer.parseInt(hex, 16);
        return Float.intBitsToFloat(bits);
    }

    /**
     * 将 float 转成16进制
     */
    public static String toHex(float value) {
        int bits = Float.floatToIntBits(value);
        return Integer.toHexString(bits);
    }
}
