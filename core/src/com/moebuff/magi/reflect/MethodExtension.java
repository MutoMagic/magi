package com.moebuff.magi.reflect;

import com.moebuff.magi.utils.UnhandledException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 扩充 MethodUtils
 *
 * @author muto
 */
public class MethodExtension {
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
     * Returns a method (that is, one that can be invoked via reflection) with given name
     * and parameters. If no such method can be found, return {@code null}.
     *
     * @param c              get method from this class
     * @param methodName     get method with this name
     * @param parameterTypes with these parameters types
     * @return The method
     */
    public static Method getMethod(Class<?> c, String methodName, Class<?>... parameterTypes) {
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
     * 从 {@link Class c} 中获取与 {@link Method m} 的名称和参数类型相同的方法
     */
    public static Method getMethod(Class<?> c, Method m) {
        return getMethod(c, m.getName(), m.getParameterTypes());
    }

    public static Method getMethod(Object o, Method m) {
        return getMethod(o.getClass(), m);
    }

    /**
     * 返回一个方法，该方法会从当前 {@link Class c} 和其父类中检索，若找不到则返回null
     *
     * @param c get method from this class
     * @param m get method with this name and parameters types
     * @return The method
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
