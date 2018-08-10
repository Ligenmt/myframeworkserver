package com.ligen.framework.aop;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by ligen on 2018/8/2.
 */
public class ProxyManager {

    /**
     * 创建代理对象
     * @param targetClass
     * @param proxyList
     * @param <T>
     * @return
     */
    public static <T> T createProxy(final Class targetClass, final List<Proxy> proxyList) {

        return (T)Enhancer.create(targetClass, new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                ProxyChain proxyChain = new ProxyChain(proxyList, objects, methodProxy, method, o, targetClass);
                return proxyChain;
            }
        });
    }

}
