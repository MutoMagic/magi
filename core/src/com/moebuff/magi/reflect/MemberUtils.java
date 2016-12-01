package com.moebuff.magi.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Contains common code for working with {@link Method}/{@link Constructor},
 * extracted and refactored from {@link org.apache.commons.lang3.reflect.MemberUtils}
 * when it was imported from Commons BeanUtils.
 *
 * @author muto
 */
public class MemberUtils {
    public static final int PACKAGE = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE;
    public static final int NOT_FINAL = ~Modifier.FINAL;

    private static final Class<?>[] PRIMITIVE_TYPES = {Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE,
            Float.TYPE, Double.TYPE, Character.TYPE, Boolean.TYPE};

    /**
     * Returns whether a given set of modifiers implies package access.
     *
     * @param modifiers to test
     * @return {@code true} unless {@code package}/{@code protected}/{@code private} modifier detected
     */
    public static boolean isFriendly(final int modifiers) {
        return (modifiers & PACKAGE) == 0;
    }

    /**
     * Return {@code true} if the argument includes the {@code final} modifier, {@code false} otherwise.
     */
    public static boolean isFinal(Member m) {
        return m != null && Modifier.isFinal(m.getModifiers());
    }

    /**
     * Returns whether a {@link Member} is accessible.
     *
     * @param m to check
     * @return {@code true} if <code>m</code> is accessible
     */
    public static boolean isAccessible(final Member m) {
        return m != null && Modifier.isPublic(m.getModifiers()) && !m.isSynthetic();
    }
}
