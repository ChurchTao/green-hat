package com.greenhat.aop;


import com.greenhat.aop.annotation.Transaction;
import com.greenhat.jdbc.DatabaseLoader;
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
                logger.debug("start Transaction");
                result = proxyChain.doProxyChain();
                DatabaseLoader.commitTransaction();
                logger.debug("commit Transaction");
            }catch (Exception e){
                DatabaseLoader.rollbackTransaction();
                logger.debug("rollback Transaction");
                logger.error("because :{}",e);
            }finally {
                FLAG_HOLDER.remove();
            }
        }else {
            result = proxyChain.doProxyChain();
        }
        return result;
    }
}
