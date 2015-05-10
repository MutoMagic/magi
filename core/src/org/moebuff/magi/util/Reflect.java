package org.moebuff.magi.util;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
    private Set methods = new HashSet();
    private Set isMethods = new HashSet();
    private Set getMethods = new HashSet();
    private Set setMethods = new HashSet();

    public static Object invoke(Method m, Object obj, Object... args) {
        try {
            return m.invoke(obj, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Reflect(Class c) {
        this.targetClass = c;

        for (Method m : c.getMethods()) {
            methods.add(m);
            String name = m.getName();
            if (StringUtil.indexOf(name, IS_PREFIX) != -1)
                isMethods.add(m);
            if (StringUtil.indexOf(name, GET_PREFIX) != -1)
                getMethods.add(m);
            if (StringUtil.indexOf(name, SET_PREFIX) != -1)
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

    private Object invoke(String name, Object obj, Object[] args, Iterator<Method> methods) {
        return invoke(name, null, obj, args, methods);
    }

    private Object invoke(String name, Object[] paramTypes, Object obj, Object[] args, Iterator<Method> methods) {
        while (methods.hasNext()) {
            Method m = methods.next();
            if (m.getName().equals(name))
                if (FixedRuntime.isNull(paramTypes) || FixedRuntime.isSame(m.getParameterTypes(), paramTypes))
                    return invoke(m, obj, args);
        }
        return null;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }

    public Set getMethods() {
        return methods;
    }

    public void setMethods(Set methods) {
        this.methods = methods;
    }

    public Set getSetMethods() {
        return setMethods;
    }

    public void setSetMethods(Set setMethods) {
        this.setMethods = setMethods;
    }

    public Set getGetMethods() {
        return getMethods;
    }

    public void setGetMethods(Set getMethods) {
        this.getMethods = getMethods;
    }
}
