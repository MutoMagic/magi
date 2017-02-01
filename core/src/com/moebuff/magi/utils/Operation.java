package com.moebuff.magi.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.Validate;

import java.util.Random;

/**
 * 一些常用的数学运算
 *
 * @author muto
 */
public class Operation {
    private static final Random RANDOM_NUMBER_GENERATOR = new Random(47);
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};//十六进制数字

    /**
     * 将16进制数转换成 {@code float} 类型。
     */
    public static float toHexFloat(String hex) {
        int bits = Integer.parseInt(hex, 16);
        return Float.intBitsToFloat(bits);
    }

    /**
     * 将10进制 {@code float} 转换成16进制。
     */
    public static String toHexString(float value) {
        int bits = Float.floatToIntBits(value);
        return Integer.toHexString(bits);
    }

    /**
     * 多区间随机数，参数 args 必须为2的倍数，但这并不是强制的。
     * 如果 args.length == 0，则返回 {@link Random#nextDouble()} 的值；
     * 若 args.length 为奇数，其执行效果等同于 args.length - 1
     *
     * @param args 一组区间，不能为null
     * @return 区间内的随机数
     */
    public static double random(double... args) {
        Validate.isTrue(args != null, "参数 args 不能为null");

        if (args.length <= 1) {
            return RANDOM_NUMBER_GENERATOR.nextDouble();
        }

        int n = RANDOM_NUMBER_GENERATOR.nextInt(args.length / 2) * 2;
        return RandomUtils.nextDouble(args[n], args[n + 1]);
    }

    /**
     * 对指定的数组（从小到大）进行冒泡排序。
     *
     * @param args 需要排序的数组
     */
    @SuppressWarnings("Duplicates")
    public static void bubbleSort(double[] args) {
        double temp;
        for (int i = args.length - 1; i > 0; i--)
            for (int j = 0; j < i; j++) {
                if (args[j + 1] < args[j]) {
                    temp = args[j];
                    args[j] = args[j + 1];
                    args[j + 1] = temp;
                }
            }
    }

    @SuppressWarnings("Duplicates")
    public static void bubbleSort(int[] args) {
        int temp;
        for (int i = args.length - 1; i > 0; i--)
            for (int j = 0; j < i; j++) {
                if (args[j + 1] < args[j]) {
                    temp = args[j];
                    args[j] = args[j + 1];
                    args[j + 1] = temp;
                }
            }
    }

    /**
     * 计算 MD5 摘要，32位加密算法。
     *
     * @param data 待加密的数据
     * @return 加密结果，全小写的字符串
     */
    public static String md5Hex(String data) {
        byte[] result = DigestUtils.md5(data);

        int length = result.length;
        char[] hash = new char[length * 2];
        // 把密文转换成十六进制的字符串形式
        for (int i = 0, j = 0; i < length; i++) {
            byte byte0 = result[i];
            hash[j++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
            hash[j++] = HEX_DIGITS[byte0 & 0xf];
        }
        return new String(hash);
    }
}
