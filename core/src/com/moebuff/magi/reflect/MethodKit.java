package com.moebuff.magi.reflect;

import com.moebuff.magi.utils.UnhandledException;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 方法工具
 *
 * @author muto
 * @see MethodUtils
 */
public class MethodKit {
    /**
     * 调用此 {@link Method} 对象所表示的底层方法
     *
     * @param m      被调用的方法
     * @param target 从中调用底层方法的对象
     * @param args   用于方法调用的参数
     * @return 执行方法后返回的结果
     */
    public static Object invoke(Method m, Object target, Object... args) {
        try {
            return m.invoke(target, args);
        } catch (IllegalAccessException e) {
            if (m.isAccessible()) {
                throw new UnhandledException(e);
            }
            m.setAccessible(true);
            return invoke(m, target, args);
        } catch (InvocationTargetException e) {
            throw new UnhandledException(e);
        }
    }

    /**
     * 返回一个方法，前提是该方法在类中已被定义。若该方法在 {@code c} 中是公共成员，则直接返回；
     * 如果不是，则取消其访问检查后并返回。当返回的是 {@code null}，表示该方法不存在。
     *
     * @param c              获取该方法的类，不能为null
     * @param methodName     方法名
     * @param parameterTypes 方法所需的参数类型。参数的数目是可变的，可为 0。
     * @return 名为 {@code methodName} 的 {@link Method}；当为 {@code null} 时，方法不存在。
     * @throws IllegalArgumentException 当 {@code c} 为 {@code null} 时
     */
    public static Method getMethod(Class<?> c, String methodName, Class<?>... parameterTypes) {
        Validate.isTrue(c != null, "Class不能为null");
        try {
            Method m = c.getDeclaredMethod(methodName, parameterTypes);
            if (!MemberUtils.isAccessible(m)) {
                m.setAccessible(true);//取消访问检查
            }
            return m;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * 从 {@link Class c} 中获取与 {@link Method m} 的名称和参数类型相同的方法。
     *
     * @throws IllegalArgumentException 当 {@code m} 为 {@code null} 时
     * @see #getMethod(Class, String, Class[])
     */
    public static Method getMethod(Class<?> c, Method m) {
        Validate.isTrue(m != null, "Method不能为null");
        return getMethod(c, m.getName(), m.getParameterTypes());
    }

    public static Method getMethod(Object o, Method m) {
        return getMethod(o.getClass(), m);
    }

    /**
     * 返回一个方法，该方法会从当前 {@link Class c} 和其父类中检索，若找不到则返回null
     *
     * @param c 被反射的类，不能为null
     * @param m 获取与此名称及参数相同的方法
     * @return {@code c} 和其父类中与 {@code m} 同名同参的方法；若为null，则表示不存在。
     * @see #getMethod(Class, Method)
     */
    public static Method getMethodFromSuper(Class<?> c, Method m) {
        Method result = getMethod(c, m);
        if (result == null) {
            Class su = c.getSuperclass();
            if (su == null) return null;
            result = getMethodFromSuper(su, m);
        }
        return result;
    }

    public static Method getMethodFromSuper(Object o, Method m) {
        return getMethodFromSuper(o.getClass(), m);
    }
}
