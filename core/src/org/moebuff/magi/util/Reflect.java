package org.moebuff.magi.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 反射工具
 *
 * @author MuTo
 */
public class Reflect<T> {
    public static final String IS_PREFIX = "is";
    public static final String GET_PREFIX = "get";
    public static final String SET_PREFIX = "set";

    private Class targetClass;
    private Set<Method> methods = new HashSet();
    private Set<Method> isMethods = new HashSet();
    private Set<Method> getMethods = new HashSet();
    private Set<Method> setMethods = new HashSet();

    public Reflect(Class c) {
        targetClass = c;

        for (Method m : targetClass.getMethods()) {
            methods.add(m);
            String name = m.getName();
            if (name.indexOf(GET_PREFIX) == 0)
                isMethods.add(m);
            if (name.indexOf(GET_PREFIX) == 0)
                getMethods.add(m);
            if (name.indexOf(SET_PREFIX) == 0)
                setMethods.add(m);
        }
    }

    public static Object invoke(Method m, Object obj, Object... args) {
        try {
            return m.invoke(obj, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Class[] getTypes(Object... args) {
        if (args == null)
            return null;
        Class[] c = new Class[args.length];
        for (int i = 0; i < args.length; i++)
            c[i] = args[i] == null ? null : args[i].getClass();
        return c;
    }

    public T newInstance(Object... args) {
        try {
            Constructor<T> c = targetClass.getDeclaredConstructor(getTypes(args));
            c.setAccessible(true);
            return c.newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object invokeIs(String name, Object obj) {
        return invoke(IS_PREFIX + StringUtil.upperInitial(name), obj, null, isMethods.iterator());
    }

    public Object invokeGet(String name, Object obj) {
        return invoke(GET_PREFIX + StringUtil.upperInitial(name), obj, null, getMethods.iterator());
    }

    public Object invokeSet(String name, Object obj, Object arg) {
        return invoke(SET_PREFIX + StringUtil.upperInitial(name), obj, new Object[]{arg}, setMethods.iterator());
    }

    public Object invoke(String name, Object obj, Object... args) {
        return invoke(name, getTypes(args), obj, args);
    }

    public Object invoke(String name, Object[] paramTypes, Object obj, Object... args) {
        return invoke(name, paramTypes, obj, args, methods.iterator());
    }

    // Properties
    // -------------------------------------------------------------------------

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }

    public Set<Method> getMethods() {
        return methods;
    }

    public void setMethods(Set<Method> methods) {
        this.methods = methods;
    }

    public Set<Method> getIsMethods() {
        return isMethods;
    }

    public void setIsMethods(Set<Method> isMethods) {
        this.isMethods = isMethods;
    }

    public Set<Method> getGetMethods() {
        return getMethods;
    }

    public void setGetMethods(Set<Method> getMethods) {
        this.getMethods = getMethods;
    }

    public Set<Method> getSetMethods() {
        return setMethods;
    }

    public void setSetMethods(Set<Method> setMethods) {
        this.setMethods = setMethods;
    }

    // Implementation methods
    // -------------------------------------------------------------------------

    protected Object invoke(String name, Object obj, Object[] args, Iterator<Method> methods) {
        return invoke(name, getTypes(args), obj, args, methods);
    }

    protected Object invoke(String name, Object[] paramTypes, Object obj, Object[] args, Iterator<Method> methods) {
        while (methods.hasNext()) {
            Method m = methods.next();
            if (m.getName().equals(name))
                if (paramTypes == null || FixedRuntime.same(m.getParameterTypes(), paramTypes))
                    return invoke(m, obj, args);
        }
        return null;
    }
}
