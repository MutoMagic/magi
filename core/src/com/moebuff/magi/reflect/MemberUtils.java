package com.moebuff.magi.reflect;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

/**
 * 鉴于 apache-commons-lang3 下的同名类是 {@code Friendly}，因此在这里复写了一些常用的方法。
 *
 * @author muto
 * @see org.apache.commons.lang3.reflect.MemberUtils
 */
public class MemberUtils {
    public static final int NOT_FINAL = ~Modifier.FINAL;

    /**
     * @return {@code true} if {@code m} includes the {@code final} modifier; {@code false} otherwise.
     */
    public static boolean isFinal(Member m) {
        return m != null && Modifier.isFinal(m.getModifiers());
    }

    /**
     * Returns whether a {@link Member} is accessible.
     *
     * @param m Member to check
     * @return {@code true} if <code>m</code> is accessible
     */
    static boolean isAccessible(final Member m) {
        return m != null && Modifier.isPublic(m.getModifiers()) && !m.isSynthetic();
    }
}
