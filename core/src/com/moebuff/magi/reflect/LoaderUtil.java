package com.moebuff.magi.reflect;

import com.moebuff.magi.utils.UnhandledException;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Utility class for ClassLoaders.
 *
 * @see ClassLoader
 * @see RuntimePermission
 * @see Thread#getContextClassLoader()
 * @see ClassLoader#getSystemClassLoader()
 */
public class LoaderUtil implements PrivilegedAction<ClassLoader> {
    private static final SecurityManager SECURITY_MANAGER = System.getSecurityManager();
    private static final RuntimePermission GET_CLASSLOADER_PERMISSION
            = new RuntimePermission("getClassLoader");

    private static final boolean GET_CLASS_LOADER_DISABLED;

    private static final PrivilegedAction<ClassLoader> TCCL_GETTER = new LoaderUtil();

    static {
        boolean getClassLoaderDisabled;
        try {
            if (SECURITY_MANAGER != null) {
                SECURITY_MANAGER.checkPermission(GET_CLASSLOADER_PERMISSION);
            }
            getClassLoaderDisabled = false;
        } catch (final SecurityException ignored) {
            getClassLoaderDisabled = true;
        }
        GET_CLASS_LOADER_DISABLED = getClassLoaderDisabled;
    }

    /**
     * Loads a class by name.
     *
     * @param className The class name.
     * @return the Class for the given name.
     * @throws ClassNotFoundException if the specified class name could not be found
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> loadClass(final String className) throws ClassNotFoundException {
        try {
            return (Class<T>) getThreadContextClassLoader().loadClass(className);
        } catch (final Throwable ignored) {
            return (Class<T>) Class.forName(className);
        }
    }

    /**
     * @param notNull 当找不到类时，若为false则返回null，为true则抛出异常
     * @see #loadClass(String)
     */
    public static <T> Class<T> loadClass(final String className, final boolean notNull) {
        try {
            return loadClass(className);
        } catch (ClassNotFoundException e) {
            if (notNull) {
                throw new UnhandledException(e);
            }
            return null;
        }
    }

    /**
     * Gets the current Thread ClassLoader. Returns the system ClassLoader if the TCCL is {@code null}.
     * If the system ClassLoader is {@code null} as well, then the ClassLoader for this class is returned.
     * If running with a {@link SecurityManager} that does not allow access to the Thread ClassLoader
     * or system ClassLoader, then the ClassLoader for this class is returned.
     *
     * @return the current ThreadContextClassLoader.
     */
    public static ClassLoader getThreadContextClassLoader() {
        if (GET_CLASS_LOADER_DISABLED) {
            // we can at least get this class's ClassLoader regardless of security context
            // however, if this is null, there's really no option left at this point
            return LoaderUtil.class.getClassLoader();
        }
        return SECURITY_MANAGER == null ? TCCL_GETTER.run() : AccessController.doPrivileged(TCCL_GETTER);
    }

    @Override
    public ClassLoader run() {
        final ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl != null) {
            return cl;
        }
        final ClassLoader ccl = LoaderUtil.class.getClassLoader();
        return ccl == null && !GET_CLASS_LOADER_DISABLED ? ClassLoader.getSystemClassLoader() : ccl;
    }
}
