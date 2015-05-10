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
    public interface TagResolver {
        Object analyze(Map<String, Set<Field>> tagFields, Reflect reflect, Object... args);
    }

    public static final String IS_PREFIX = "is";
    public static final String GET_PREFIX = "get";
    public static final String SET_PREFIX = "set";

    private Class targetClass;
    private Set<Method> methods = new HashSet();
    private Set<Method> isMethods = new HashSet();
    private Set<Method> getMethods = new HashSet();
    private Set<Method> setMethods = new HashSet();
    private Set<Field> fields = new HashSet();
    private Map<String, Set<Field>> tagFields = new HashMap();

    public static Object invoke(Method m, Object obj, Object... args) {
        try {
            return m.invoke(obj, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Reflect(Class c) {
        targetClass = c;

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

        Set<Field> tagFieldSet = null;
        for (Field f : c.getDeclaredFields()) {
            fields.add(f);
            Tag tag = f.getAnnotation(Tag.class);
            if (tag != null)
                tagFields.put(tag.value(), tagFieldSet = new HashSet());
            if (tagFieldSet != null)
                tagFieldSet.add(f);
        }

        for (Iterator<String> i = tagFields.keySet().iterator(); i.hasNext(); ) {
            String s = i.next();
            System.out.println("---------");
            System.out.println(s);
            for (Iterator<Field> j = tagFields.get(s).iterator(); j.hasNext(); ) {
                Field f = j.next();
                System.out.println(f.getName());
            }
        }
    }

    public Object analyzeTag(TagResolver resolver, Object... args) {
        return resolver.analyze(tagFields, this, args);
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

    public Set<Field> getFields() {
        return fields;
    }

    public void setFields(Set<Field> fields) {
        this.fields = fields;
    }

    public Map<String, Set<Field>> getTagFields() {
        return tagFields;
    }

    public void setTagFields(Map<String, Set<Field>> tagFields) {
        this.tagFields = tagFields;
    }
}
