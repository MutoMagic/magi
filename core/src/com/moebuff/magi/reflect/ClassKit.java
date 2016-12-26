package com.moebuff.magi.reflect;

import com.moebuff.magi.utils.UnhandledException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Constructor;

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
}
