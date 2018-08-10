package com.ligen.framework.aop;

/**
 * Created by ligen on 2018/8/2.
 */
public interface Proxy {

    Object doProxy(ProxyChain proxyChain) throws Exception;
}
