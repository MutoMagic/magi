package com.moebuff.magi.reflect;

import com.moebuff.magi.utils.UnhandledException;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

/**
 * 字段工具，简化了反射操作，但这并不意味着会提高执行效率，若方法搭配不好反而会降低甚至引发错误。
 *
 * @author muto
 * @see FieldUtils
 */
public class FieldKit {
    /**
     * 在类中获取已被声明的字段，若字段并非公共成员，则取消 Java 语言访问检查。
     *
     * @param c         被反射的类，不能为null
     * @param fieldName 字段名
     * @return 若字段未被声明，则返回null
     * @throws IllegalArgumentException if the class is {@code null}, or the field name is blank or empty
     */
    public static Field getDeclaredField(Class<?> c, String fieldName) {
        return FieldUtils.getDeclaredField(c, fieldName, true);
    }

    /**
     * 移除final修饰符。需要注意的是，对于String和基本类型的静态常量，JVM通常会将代码中对此常量引用的地方替换成相应的值。
     */
    public static void removeFinalModifier(Field f) {
        Field m = getDeclaredField(Field.class, "modifiers");
        if (MemberUtils.isFinal(m)) {
            writeField(m, f, f.getModifiers() & MemberUtils.NOT_FINAL);
            return;
        }

        //The "modifiers" is not a field in the Field class on Android.
        if (m == null) f.setAccessible(true);
    }

    // write field
    //==========================================================================================

    /**
     * Writes a {@link Field}.
     *
     * @param f      to write
     * @param target the object to call on, may be {@code null} for {@code static} fields
     * @param value  to set
     * @throws IllegalArgumentException if the field is {@code null} or {@code value} is not assignable
     * @throws UnhandledException       if the field is {@code final}
     */
    public static void writeField(Field f, Object target, Object value) {
        try {
            FieldUtils.writeField(f, target, value, true);
        } catch (IllegalAccessException e) {
            throw new UnhandledException(e);
        }
    }

    /**
     * Writes a {@link Field}. Only the specified class will be considered.
     *
     * @param fieldName the field name to obtain
     * @param target    the object to reflect, must not be {@code null}
     * @param value     to set
     * @throws IllegalArgumentException if {@code target} is {@code null},
     *                                  {@code fieldName} is blank or empty or could not be found,
     *                                  or {@code value} is not assignable
     */
    public static void writeField(String fieldName, Object target, Object value) {
        try {
            FieldUtils.writeDeclaredField(target, fieldName, value, true);
        } catch (IllegalAccessException e) {
            throw new UnhandledException(e);//理论上这个错误不会出现
        }
    }

    /**
     * 修改一个静态字段。该方法与执行 {@code writeField(f, null, value)} 的结果相同。
     *
     * @param f     to write
     * @param value to set
     * @see #writeField(Field, Object, Object)
     */
    public static void writeStaticField(Field f, Object value) {
        writeField(f, null, value);
    }

    /**
     * 修改一个静态字段，该字段由 fieldName 指定并在类中已被声明。
     *
     * @param c         被反射的类，不能为null
     * @param fieldName 字段名
     * @param value     to set
     * @see #writeField(Field, Object, Object)
     * @see #getDeclaredField(Class, String)
     */
    public static void writeStaticField(Class<?> c, String fieldName, Object value) {
        writeField(getDeclaredField(c, fieldName), null, value);
    }

    /**
     * 修改常量。在执行 {@link #writeField(Field, Object, Object)} 前移除了final修饰符。
     *
     * @param f      to write
     * @param target the object to call on, may be {@code null} for {@code static} fields
     * @param value  to set
     * @see #removeFinalModifier(Field)
     */
    public static void writeConstant(Field f, Object target, Object value) {
        removeFinalModifier(f);
        writeField(f, target, value);
    }

    /**
     * 修改一个在 {@code target} 对象所表示的类中已被声明的常量。
     *
     * @param fieldName 字段名
     * @param target    将被放射的对象，不能为null
     * @param value     to set
     * @see #writeConstant(Field, Object, Object)
     * @see #getDeclaredField(Class, String)
     */
    public static void writeConstant(String fieldName, Object target, Object value) {
        writeConstant(getDeclaredField(target.getClass(), fieldName), target, value);
    }

    /**
     * 修改一个静态常量。该方法与执行 {@code writeConstant(f, null, value)} 的结果相同。
     *
     * @param f     to write
     * @param value to set
     * @see #writeConstant(Field, Object, Object)
     */
    public static void writeStaticConstant(Field f, Object value) {
        writeConstant(f, null, value);
    }

    /**
     * 修改一个静态常量，该字段由 fieldName 指定并在类中已被声明。
     *
     * @param c         被反射的类，不能为null
     * @param fieldName 字段名
     * @param value     to set
     * @see #writeConstant(Field, Object, Object)
     * @see #getDeclaredField(Class, String)
     */
    public static void writeStaticConstant(Class<?> c, String fieldName, Object value) {
        writeConstant(getDeclaredField(c, fieldName), null, value);
    }

    // read field
    //==========================================================================================

    /**
     * Reads a {@link Field}.
     *
     * @param f      the field to use
     * @param target the object to call on, may be {@code null} for {@code static} fields
     * @return the field value
     * @throws IllegalArgumentException if the field is {@code null}
     */
    public static Object readField(Field f, Object target) {
        try {
            return FieldUtils.readField(f, target, true);
        } catch (IllegalAccessException e) {
            throw new UnhandledException(e);//理论上这个错误不会出现
        }
    }

    /**
     * Gets a {@link Field} value by name. Only the class of the specified object will be considered.
     *
     * @param target    the object to reflect, must not be {@code null}
     * @param fieldName the field name to obtain
     * @return the Field object
     * @throws IllegalArgumentException if {@code target} is {@code null},or the field name is blank
     *                                  or empty or could not be found
     */
    public static Object readField(String fieldName, Object target) {
        try {
            return FieldUtils.readDeclaredField(target, fieldName, true);
        } catch (IllegalAccessException e) {
            throw new UnhandledException(e);//理论上这个错误不会出现
        }
    }

    /**
     * 获取静态字段的值。该方法与执行 {@code readField(f, null)} 的结果相同。
     *
     * @param f 将被读取的字段
     * @return 静态字段的值
     * @see #readField(Field, Object)
     */
    public static Object readStaticField(Field f) {
        return readField(f, null);
    }

    /**
     * Reads the named {@code static} {@link Field}. Superclasses will be considered.
     *
     * @param c         the {@link Class} to reflect, must not be {@code null}
     * @param fieldName the field name to obtain
     * @return the Field object
     * @throws IllegalArgumentException if the class is {@code null}, or the field name is blank or empty,
     *                                  is not {@code static}, or could not be found
     */
    public static Object readStaticField(Class<?> c, String fieldName) {
        try {
            return FieldUtils.readStaticField(c, fieldName, true);
        } catch (IllegalAccessException e) {
            throw new UnhandledException(e);//理论上这个错误不会出现
        }
    }
}
