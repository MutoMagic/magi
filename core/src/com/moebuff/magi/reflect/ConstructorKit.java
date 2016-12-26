package com.moebuff.magi.reflect;

import com.moebuff.magi.utils.UnhandledException;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.ConstructorUtils;

import java.lang.reflect.Constructor;

/**
 * 构造方法工具
 *
 * @author muto
 * @see ConstructorUtils
 */
public class ConstructorKit {
    /**
     * 使用此 {@code ctor} 对象表示的构造方法来创建该构造方法的声明类的新实例，并用指定的初始化参数初始化该实例。
     * 个别参数会自动解包，以匹配基本形参，必要时，基本参数和引用参数都要进行方法调用转换。
     * <p>
     * 如果底层构造方法所需形参数为 0，则所提供的 args 数组的长度可能为 0 或 null。
     *
     * @param ctor 被调用的构造方法
     * @param args 将作为变量传递给构造方法调用的对象数组；
     *             基本类型的值被包装在适当类型的包装器对象（如 Float 中的 float）中。
     * @return 通过调用此对象表示的构造方法来创建的新对象
     */
    public static <T> T newInstance(Constructor<T> ctor, Object... args) {
        try {
            return ctor.newInstance(args);
        } catch (IllegalAccessException e) {
            if (ctor.isAccessible()) {
                throw new UnhandledException(e);
            }
            ctor.setAccessible(true);
            return newInstance(ctor, args);
        } catch (ReflectiveOperationException e) {
            throw new UnhandledException(e);
        }
    }

    /**
     * 返回一个 Constructor 对象，该对象反映此 Class 对象所表示的类或接口的指定构造方法。
     * parameterTypes 参数是 Class 对象的一个数组，它按声明顺序标识构造方法的形参类型。
     * 如果获取的构造方法对象并非公共成员，则取消其访问检查后并返回。若该方法不存在，则返回null。
     *
     * @param c              获取构造方法的类，不能为null
     * @param parameterTypes 参数数组
     * @return 带有指定参数列表的构造方法的 Constructor 对象
     */
    public static <T> Constructor<T> getConstructor(Class<T> c, Class<?>... parameterTypes) {
        Validate.isTrue(c != null, "Class不能为null");
        try {
            Constructor<T> ctor = c.getDeclaredConstructor(parameterTypes);
            if (!MemberUtils.isAccessible(ctor)) {
                ctor.setAccessible(true);
            }
            return ctor;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
