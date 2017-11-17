package com.greenhat.aop;

/**
 * Created by jiacheng on 2017/7/21.
 */
public interface Proxy {
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
