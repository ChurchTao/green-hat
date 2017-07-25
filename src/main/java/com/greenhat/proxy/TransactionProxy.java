package com.greenhat.proxy;


import com.greenhat.annotation.Transaction;
import com.greenhat.loader.DatabaseLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by jiacheng on 2017/7/21.
 */
public class TransactionProxy implements Proxy {
    private static final Logger logger = LoggerFactory.getLogger(TransactionProxy.class);



    private static final ThreadLocal<Boolean> FLAG_HOLDER = new ThreadLocal<Boolean>(){
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;
        boolean flag = FLAG_HOLDER.get();
        Method method = proxyChain.getTargetMethod();
        if (!flag&&method.isAnnotationPresent(Transaction.class)){
            FLAG_HOLDER.set(true);
            try {
                DatabaseLoader.beginTransaction();
                logger.debug("开始事务");
                result = proxyChain.doProxyChain();
                DatabaseLoader.commitTransaction();
                logger.debug("提交事务");
            }catch (Exception e){
                DatabaseLoader.rollbackTransaction();
                logger.debug("回滚事务");
                e.printStackTrace();
            }finally {
                FLAG_HOLDER.remove();
            }
        }else {
            result = proxyChain.doProxyChain();
        }
        return result;
    }
}
