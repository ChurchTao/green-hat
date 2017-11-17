package com.greenhat.jdbc.DAOFactory;

import com.greenhat.loader.ClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jiacheng on 2017/8/10.
 */
public class DAOFactory {
    private static final Logger logger = LoggerFactory.getLogger(DAOFactory.class);
    private static final ConcurrentHashMap<String, DAO<?>> store = new ConcurrentHashMap<>();
    static {
        logger.info("DAOFactory init start!");
        try {
            List<Class<?>> classes = ClassLoader.getClassListByAnnotation(com.greenhat.jdbc.annotation.DAO.class);
            for (Class<?> c : classes) {
                ActionParser.createProxyDAOBean(c.getName(),c);
                logger.info("DAOFactory Loaded {}!",c.getName());
            }
        } catch (Exception e) {
            logger.error("DAOFactory init Error,Because {}", e);
        }

    }

    @SuppressWarnings("unchecked")
    public static <T> T getDAO(Class<T> daoClass) {
        return (T) store.get(daoClass.getName());
    }

    public static void register(String id, DAO dao) {
        store.put(id, dao);
    }

}
