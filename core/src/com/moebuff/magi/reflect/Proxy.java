package com.moebuff.magi.reflect;

/**
 * 用于创建代理对象的规范化接口
 *
 * @param <T> 被代理的类
 * @author muto
 */
public interface Proxy<T> {
    /**
     * 创建动态代理
     *
     * @param obj 被代理的对象
     * @return 代理后的对象
     */
    T getInstance(T obj);
}
