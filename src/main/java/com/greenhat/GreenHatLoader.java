package com.greenhat;

import com.greenhat.banner.Banner;
import com.greenhat.jdbc.DAOFactory;
import com.greenhat.loader.*;
import com.greenhat.loader.ClassLoader;
import com.greenhat.orm.EntityHelper;
import com.greenhat.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jiacheng on 2017/7/19.
 */
public final class GreenHatLoader {
    private static final Logger logger = LoggerFactory.getLogger(GreenHatLoader.class);
    public static void init() {
        Banner.startBanner();
        logger.info("GreenHat init start!");
        Class<?>[] classes = {
                ClassLoader.class,
                EntityHelper.class,
                DAOFactory.class,
                BeanLoader.class,
                AopLoader.class,
                IocLoader.class,
                ControllerLoader.class
        };
        for (Class<?> cls : classes) {
            ClassUtil.loadClass(cls.getName(), true);
        }
        logger.info("GreenHat init done!");
    }
}
