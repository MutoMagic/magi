package com.moebuff.magi.reflect;

import com.moebuff.magi.utils.URLUtils;
import com.moebuff.magi.utils.UnhandledException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类型工具
 *
 * @author muto
 * @see ClassUtils
 */
public class ClassKit {
    /**
     * 创建一个新实例，本质上就是调用构造方法。如果被创建的类尚未初始化，则初始化这个类。此封装便于类型转换。
     *
     * @param c    对象的类型
     * @param args 将作为变量传递给构造方法调用的对象数组；
     *             基本类型的值被包装在适当类型的包装器对象（如 Float 中的 float）中。
     *             如果该参数存在，则调用对应的构造方法。
     * @return 此对象所表示的类的一个新分配的实例。
     * @throws UnhandledException 如果此 Class 表示一个抽象类、接口、数组类、基本类型或 void； 或者该类没有
     *                            null 构造方法； 或者由于其他某种原因导致实例化失败。
     * @see ConstructorKit#newInstance(Constructor, Object...)
     */
    public static <T> T newInstance(Class<T> c, Object... args) {
        if (ArrayUtils.isEmpty(args)) {
            try {
                return c.newInstance();
            } catch (InstantiationException e) {
                throw new UnhandledException(e);
            } catch (IllegalAccessException e) {
                Constructor<T> ctor0 = ConstructorKit.getConstructor(c);
                return ConstructorKit.newInstance(ctor0);
            }
        }
        Class<?>[] parameterTypes = ClassUtils.toClass(args);
        Constructor<T> ctor = ConstructorKit.getConstructor(c, parameterTypes);
        return ConstructorKit.newInstance(ctor, args);
    }

    public static Class[] getClasses(String pkg) {
        return getClasses(pkg, false);
    }

    /**
     * 获取指定包下的类集合。若要深入子包请将 deep 设置为 true
     *
     * @param pkg  包名
     * @param deep 为true，允许递归子包
     * @return 指定包下的类集合
     */
    public static Class[] getClasses(String pkg, boolean deep) {
        Set<Class> classes = new HashSet<>();
        ClassLoader loader = LoaderUtil.getThreadContextClassLoader();

        URL url = loader.getResource(pkg.replace(".", "/"));
        if (url != null) {
            String protocol = url.getProtocol();

            if ("file".equals(protocol)) {
                classes = getClasses(URLUtils.decode(url::getPath), pkg, deep);
            } else if ("jar".equals(protocol)) {
                classes = getClasses(URLUtils.getJarFile(url), pkg, deep);
            }
        }
        return classes.toArray(new Class[classes.size()]);
    }

    private static Set<Class> getClasses(String path, final String pkg, final boolean deep) {
        final Set<Class> classes = new HashSet<>();
        File[] list = new File(path).listFiles(pathname -> {
            boolean isDir = pathname.isDirectory();

            if (isDir && deep) {
                Set<Class> child = getClasses(pathname.getPath(), pkg, true);
                classes.addAll(child);
            }
            return !isDir;
        });

        if (list != null) {
            Class clazz;
            for (File f : list) {
                String name = f.getName();
                if (!isValid(name)) {
                    continue;//无效的类文件
                }

                name = FilenameUtils.getBaseName(name);
                name = String.format("%s.%s", pkg, name);
                clazz = LoaderUtil.loadClass(name, true);
                classes.add(clazz);
            }
        }
        return classes;
    }

    private static Set<Class> getClasses(JarFile file, String pkg, boolean deep) {
        Set<Class> classes = new HashSet<>();
        Enumeration<JarEntry> enumeration = file.entries();
        while (enumeration.hasMoreElements()) {
            JarEntry entry = enumeration.nextElement();
            if (entry.isDirectory()) continue;

            String name = entry.getName();//包含路径
            String path = FilenameUtils.getPathNoEndSeparator(name).replace("/", ".");
            name = FilenameUtils.getName(name);
            if (!isValid(name) || !path.startsWith(pkg)) {
                continue;//不在指定的包内
            }
            if (deep || path.replace(pkg, "").isEmpty()) {
                name = FilenameUtils.getBaseName(name);
                name = String.format("%s.%s", pkg, name);
                Class clazz = LoaderUtil.loadClass(name, true);
                classes.add(clazz);
            }
        }
        return classes;
    }

    private static boolean isValid(String name) {
        // 主类名$内部类名.class（如果匿名内部类，这内部类名为数字）
        return StringUtils.isNotEmpty(name) && name.endsWith(".class") && !name.contains("$");
    }
}
