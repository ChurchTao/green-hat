package com.greenhat.proxy;

import com.greenhat.annotation.AspectMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Method;

/**
 * Created by jiacheng on 2017/7/21.
 */
public abstract class AspectProxy implements Proxy {
    private static final Logger logger = LoggerFactory.getLogger(AspectProxy.class);

    /**
     * 过滤不存在 @AspectMethod 注解的类
     * 只有被注解了的类才会执行代理方法。
     */
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;
        Class<?> cls = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();
        if (method.isAnnotationPresent(AspectMethod.class)) {
            begin();
            try {
                if (intercept(cls, method, params)) {
                    before(cls, method, params);
                    result = proxyChain.doProxyChain();
                    after(cls, method, params, result);
                } else {
                    result = proxyChain.doProxyChain();
                }
            } catch (Exception e) {
                logger.error("代理失败", e);
                error(cls, method, params, e);
                throw e;
            } finally {
                end();
            }
        }else {
            result = proxyChain.doProxyChain();
        }
        return result;
    }

    public void error(Class<?> cls, Method method, Object[] params, Object result) throws Throwable {

    }

    public void end() {
    }

    public void after(Class<?> cls, Method method, Object[] params, Object result) {

    }

    public void before(Class<?> cls, Method method, Object[] params) {

    }

    public boolean intercept(Class<?> cls, Method method, Object[] params) {
        return true;
    }

    public void begin() {

    }

}
