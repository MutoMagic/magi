package com.moebuff.magi.reflect;

/**
 * 所有被代理的类用于创建代理对象的接口
 *
 * @author muto
 */
public interface Proxy {
    /**
     * 创建动态代理
     *
     * @param obj 被代理的对象
     * @return 代理后的对象
     */
    <T> T getInstance(T obj);
}
