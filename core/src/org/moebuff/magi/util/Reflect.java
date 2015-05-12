package org.moebuff.magi.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 反射工具
 *
 * @author MuTo
 */
public class Reflect {
    public static final String IS_PREFIX = "is";
    public static final String GET_PREFIX = "get";
    public static final String SET_PREFIX = "set";

    private Class targetClass;
    private Set<Method> methods = new HashSet();
    private Set<Method> isMethods = new HashSet();
    private Set<Method> getMethods = new HashSet();
    private Set<Method> setMethods = new HashSet();

    public static Object invoke(Method m, Object obj, Object... args) {
        try {
            return m.invoke(obj, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Reflect(Class c) {
        targetClass = c;

        for (Method m : targetClass.getMethods()) {
            methods.add(m);
            String name = m.getName();
            if (IS_PREFIX.indexOf(name) != -1)
                isMethods.add(m);
            if (GET_PREFIX.indexOf(name) != -1)
                getMethods.add(m);
            if (SET_PREFIX.indexOf(name) != -1)
                setMethods.add(m);
        }
    }

    public Object invokeIs(String name, Object obj, Object... args) {
        return invoke(IS_PREFIX + StringUtil.upperInitial(name), obj, args, isMethods.iterator());
    }

    public Object invokeGet(String name, Object obj, Object... args) {
        return invoke(GET_PREFIX + StringUtil.upperInitial(name), obj, args, getMethods.iterator());
    }

    public Object invokeSet(String name, Object obj, Object... args) {
        return invoke(SET_PREFIX + StringUtil.upperInitial(name), obj, args, setMethods.iterator());
    }

    public Object invoke(String name, Object obj, Object... args) {
        return invoke(name, null, obj, args);
    }

    public Object invoke(String name, Object[] paramTypes, Object obj, Object... args) {
        return invoke(name, paramTypes, obj, args, methods.iterator());
    }

    protected Object invoke(String name, Object obj, Object[] args, Iterator<Method> methods) {
        return invoke(name, null, obj, args, methods);
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
}
