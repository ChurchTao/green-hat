package com.greenhat;

import com.greenhat.banner.Banner;
import com.greenhat.loader.*;
import com.greenhat.loader.ClassLoader;
import com.greenhat.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jiacheng on 2017/7/19.
 */
public final class GreenHatLoader {
    private static final Logger logger = LoggerFactory.getLogger(GreenHatLoader.class);
    public static void init() {
        logger.info("GreenHat init start!");
        Class<?>[] classes = {
                ClassLoader.class,
                BeanLoader.class,
                AopLoader.class,
                IocLoader.class,
                ControllerLoader.class
        };
        Banner.startBanner();
        for (Class<?> cls : classes) {
            //todo 这边改过 true
            ClassUtil.loadClass(cls.getName(), true);
        }
        logger.info("GreenHat init done!");
    }
}
