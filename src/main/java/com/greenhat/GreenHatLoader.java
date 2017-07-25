package com.greenhat;

import com.greenhat.banner.Banner;
import com.greenhat.loader.*;
import com.greenhat.loader.ClassLoader;
import com.greenhat.util.ClassUtil;

/**
 * Created by jiacheng on 2017/7/19.
 */
public final class GreenHatLoader {
    public static void init() {
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

    }
}
