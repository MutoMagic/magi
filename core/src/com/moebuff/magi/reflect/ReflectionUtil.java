package com.moebuff.magi.reflect;

import com.moebuff.magi.utils.Log;

import java.lang.reflect.Method;

/**
 * Provides various methods to determine the caller class. <h3>Background</h3>
 * <p>
 * This method, available only in the Oracle/Sun/OpenJDK implementations of the Java Virtual Machine,
 * is a much more efficient mechanism for determining the {@link Class} of the caller of a particular method.
 * When it is not available, a {@link SecurityManager} is the second-best option. When this is also not
 * possible, the {@code StackTraceElement[]} returned by {@link Throwable#getStackTrace()} must be used,
 * and its {@code String} class name converted to a {@code Class} using the slow {@link Class#forName} (which
 * can add an extra microsecond or more for each invocation depending on the runtime ClassLoader hierarchy).
 * </p>
 * <p>
 * During Java 8 development, the {@code sun.reflect.Reflection.getCallerClass(int)} was removed from OpenJDK,
 * and this change was back-ported to Java 7 in version 1.7.0_25 which changed the behavior of the call
 * and caused it to be off by one stack frame. This turned out to be beneficial for the survival of this API
 * as the change broke hundreds of libraries and frameworks relying on the API which brought much more
 * attention to the intended API removal.
 * </p>
 * <p>
 * After much community backlash, the JDK team agreed to restore {@code getCallerClass(int)} and keep its existing
 * behavior for the rest of Java 7. However, the method is deprecated in Java 8, and current Java 9 development
 * has not addressed this API. Therefore, the functionality of this class cannot be relied upon for all future
 * versions of Java. It does, however, work just fine in Sun JDK 1.6, OpenJDK 1.6, Oracle/OpenJDK 1.7, and
 * Oracle/OpenJDK 1.8. Other Java environments may fall back to using {@link Throwable#getStackTrace()} which
 * is significantly slower due to examination of every virtual frame of execution.
 * </p>
 */
public class ReflectionUtil {
    // Checkstyle Suppress: the lower-case 'u' ticks off CheckStyle...
    // CHECKSTYLE:OFF
    static final int JDK_7u25_OFFSET;
    // CHECKSTYLE:OFF

    private static final boolean SUN_REFLECTION_SUPPORTED;
    private static final Method GET_CALLER_CLASS;

    static {
        Method getCallerClass;
        int java7u25CompensationOffset = 0;
        try {
            final Class<?> sunReflectionClass = LoaderUtil.loadClass("sun.reflect.Reflection");
            getCallerClass = sunReflectionClass.getDeclaredMethod("getCallerClass", int.class);
            Object o = getCallerClass.invoke(null, 0);
            final Object test1 = getCallerClass.invoke(null, 0);
            if (o == null || o != sunReflectionClass) {
                Log.warn("Unexpected return value from Reflection.getCallerClass(): %s", test1);
                getCallerClass = null;
                java7u25CompensationOffset = -1;
            } else {
                o = getCallerClass.invoke(null, 1);
                if (o == sunReflectionClass) {
                    Log.warn("You are using Java 1.7.0_25 which has a broken implementation of "
                            + "Reflection.getCallerClass.");
                    Log.warn("You should upgrade to at least Java 1.7.0_40 or later.");
                    Log.debug("Using stack depth compensation offset of 1 due to Java 7u25.");
                    java7u25CompensationOffset = 1;
                }
            }
        } catch (final Exception | LinkageError e) {
            Log.info("sun.reflect.Reflection.getCallerClass is not supported. "
                    + "ReflectionUtil.getCallerClass will be much slower due to this.", e);
            getCallerClass = null;
            java7u25CompensationOffset = -1;
        }

        SUN_REFLECTION_SUPPORTED = getCallerClass != null;
        GET_CALLER_CLASS = getCallerClass;
        JDK_7u25_OFFSET = java7u25CompensationOffset;
    }

    // TODO: return Object.class instead of null (though it will have a null ClassLoader)
    // (MS) I believe this would work without any modifications elsewhere, but I could be wrong

    // migrated from ReflectiveCallerClassUtility
    public static Class<?> getCallerClass(final int depth) {
        if (depth < 0) {
            throw new IndexOutOfBoundsException(Integer.toString(depth));
        }
        // note that we need to add 1 to the depth value to compensate for this method, but not for the
        // Method.invoke since Reflection.getCallerClass ignores the call to Method.invoke()
        if (SUN_REFLECTION_SUPPORTED) {
            try {
                return (Class<?>) GET_CALLER_CLASS.invoke(null, depth + 1 + JDK_7u25_OFFSET);
            } catch (final Exception e) {
                // theoretically this could happen if the caller class were native code
                Log.error("Error in ReflectionUtil.getCallerClass(%s).", e, depth);
                // TODO: return Object.class
                return null;
            }
        }
        // TODO: SecurityManager-based version?
        // slower fallback method using stack trace
        final StackTraceElement element = getEquivalentStackTraceElement(depth + 1);
        try {
            return LoaderUtil.loadClass(element.getClassName());
        } catch (final ClassNotFoundException e) {
            Log.error("Could not find class in ReflectionUtil.getCallerClass(%s).", e, depth);
        }
        // TODO: return Object.class
        return null;
    }

    public static StackTraceElement getEquivalentStackTraceElement(final int depth) {
        // (MS) I tested the difference between using Throwable.getStackTrace() and Thread.getStackTrace(), and
        // the version using Throwable was surprisingly faster! at least on Java 1.8. See ReflectionBenchmark.
        final StackTraceElement[] elements = new Throwable().getStackTrace();
        int i = 0;
        for (final StackTraceElement element : elements) {
            if (isValid(element)) {
                if (i == depth) {
                    return element;
                }
                ++i;
            }
        }
        Log.error("Could not find an appropriate StackTraceElement at index %s", depth);
        throw new IndexOutOfBoundsException(Integer.toString(depth));
    }

    private static boolean isValid(final StackTraceElement element) {
        // ignore native methods (oftentimes are repeated frames)
        if (element.isNativeMethod()) {
            return false;
        }
        final String cn = element.getClassName();
        // ignore OpenJDK internal classes involved with reflective invocation
        if (cn.startsWith("sun.reflect.")) {
            return false;
        }
        final String mn = element.getMethodName();
        // ignore use of reflection including:
        // Method.invoke
        // InvocationHandler.invoke
        // Constructor.newInstance
        if (cn.startsWith("java.lang.reflect.") && (mn.equals("invoke") || mn.equals("newInstance"))) {
            return false;
        }
        // ignore use of Java 1.9+ reflection classes
        if (cn.startsWith("jdk.internal.reflect.")) {
            return false;
        }
        // ignore Class.newInstance
        if (cn.equals("java.lang.Class") && mn.equals("newInstance")) {
            return false;
        }
        // ignore use of Java 1.7+ MethodHandle.invokeFoo() methods
        if (cn.equals("java.lang.invoke.MethodHandle") && mn.startsWith("invoke")) {
            return false;
        }
        // any others?
        return true;
    }
}
