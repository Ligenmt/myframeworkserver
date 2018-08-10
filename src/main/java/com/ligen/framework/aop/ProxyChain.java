package com.ligen.framework.aop;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ligen on 2018/8/2.
 */
public class ProxyChain {

    private final Class targetClass;
    private final Object targetObject;
    private final Method targetMethod;
    private final MethodProxy methodProxy;
    private final Object[] methodParams;

    private List<Proxy> proxyList = new ArrayList<>();
    private int proxyIndex = 0;

    public ProxyChain(List<Proxy> proxyList, Object[] methodParams, MethodProxy methodProxy, Method targetMethod, Object targetObject, Class targetClass) {
        this.proxyList = proxyList;
        this.methodParams = methodParams;
        this.methodProxy = methodProxy;
        this.targetMethod = targetMethod;
        this.targetObject = targetObject;
        this.targetClass = targetClass;
    }

    public Object doProxyChain() throws Throwable {
        Object methodResult;
        if (proxyIndex < proxyList.size()) {
            methodResult = proxyList.get(proxyIndex++).doProxy(this);
        } else {
            methodResult = methodProxy.invokeSuper(targetObject, methodParams);
        }
        return methodResult;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }
}
