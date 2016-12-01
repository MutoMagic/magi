package com.moebuff.magi.reflect;

import com.moebuff.magi.utils.UnhandledException;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

/**
 * 扩充 FieldUtils
 *
 * @author muto
 */
public class FieldExtension {
    /**
     * 从类中获取已声明的字段
     *
     * @param c         被反射的类，不能为null
     * @param fieldName 字段名
     * @return 若字段未被声明，则返回null
     */
    public static Field getDeclaredField(Class<?> c, String fieldName) {
        return FieldUtils.getDeclaredField(c, fieldName, true);
    }

    /**
     * 移除final修饰符
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

    //write field
    //==========================================================================================

    /**
     * Writes a {@link Field}.
     *
     * @param f      to write
     * @param target the object to call on, may be {@code null} for {@code static} fields
     * @param value  to set
     */
    public static void writeField(Field f, Object target, Object value) {
        try {
            FieldUtils.writeField(f, target, value, true);
        } catch (IllegalAccessException e) {
            throw new UnhandledException(e);
        }
    }

    /**
     * 修改一个字段，该字段在target对象所表示的类中已被声明
     *
     * @param fieldName 字段名
     * @param target    被反射的对象，不能为null
     * @param value     to set
     */
    public static void writeField(String fieldName, Object target, Object value) {
        try {
            FieldUtils.writeDeclaredField(target, fieldName, value, true);
        } catch (IllegalAccessException e) {
            throw new UnhandledException(e);
        }
    }

    /**
     * 修改一个静态字段
     *
     * @param f     to write
     * @param value to set
     */
    public static void writeStaticField(Field f, Object value) {
        writeField(f, null, value);
    }

    /**
     * 修改一个静态字段，，该字段由fieldName指定并在类中已被声明
     *
     * @param c         被反射的类，不能为null
     * @param fieldName 字段名
     * @param value     to set
     */
    public static void writeStaticField(Class<?> c, String fieldName, Object value) {
        writeField(getDeclaredField(c, fieldName), null, value);
    }

    /**
     * 修改常量。需要注意的是，对于String和基本数据类型的静态常量，JVM会将代码中对此常量引用的地方替换成相应的值
     *
     * @param f      to write
     * @param target 被调用的对象，为null时可能是static字段
     * @param value  to set
     */
    public static void writeConstant(Field f, Object target, Object value) {
        removeFinalModifier(f);
        writeField(f, target, value);
    }

    /**
     * 修改一个在target对象所表示的类中已被声明的常量
     *
     * @param fieldName 字段名
     * @param target    被反射的对象，不能为null
     * @param value     to set
     */
    public static void writeConstant(String fieldName, Object target, Object value) {
        writeConstant(getDeclaredField(target.getClass(), fieldName), target, value);
    }

    /**
     * 修改一个静态常量
     *
     * @param f     to write
     * @param value to set
     */
    public static void writeStaticConstant(Field f, Object value) {
        writeConstant(f, null, value);
    }

    /**
     * 修改一个静态常量，该常量由fieldName指定并在类中已被声明
     *
     * @param c         被反射的类，不能为null
     * @param fieldName 字段名
     * @param value     to set
     */
    public static void writeStaticConstant(Class<?> c, String fieldName, Object value) {
        writeConstant(getDeclaredField(c, fieldName), null, value);
    }

    //read field
    //==========================================================================================

    /**
     * Reads a {@link Field}.
     *
     * @param f      the field to use
     * @param target the object to call on, may be {@code null} for {@code static} fields
     * @return the field value
     */
    public static Object readField(Field f, Object target) {
        try {
            return FieldUtils.readField(f, target, true);
        } catch (IllegalAccessException e) {
            throw new UnhandledException(e);
        }
    }

    /**
     * Gets a {@link Field} value by name. Only the class of the specified object will be considered.
     *
     * @param target    the object to reflect, must not be {@code null}
     * @param fieldName the field name to obtain
     * @return the Field object
     */
    public static Object readField(String fieldName, Object target) {
        try {
            return FieldUtils.readDeclaredField(target, fieldName, true);
        } catch (IllegalAccessException e) {
            throw new UnhandledException(e);
        }
    }

    /**
     * @return 静态字段的值
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
     */
    public static Object readStaticField(Class<?> c, String fieldName) {
        try {
            return FieldUtils.readStaticField(c, fieldName, true);
        } catch (IllegalAccessException e) {
            throw new UnhandledException(e);
        }
    }
}
