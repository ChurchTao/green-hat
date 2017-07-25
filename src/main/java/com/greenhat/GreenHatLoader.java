package com.greenhat;

import com.greenhat.banner.Banner;
import com.greenhat.helper.*;
import com.greenhat.util.ClassUtil;

/**
 * Created by jiacheng on 2017/7/19.
 */
public final class GreenHatLoader {
    public static void init() {
        Class<?>[] classes = {
                ClassHelper.class,
                BeanHelper.class,
                AopHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };
        Banner.startBanner();
        for (Class<?> cls : classes) {
            //todo 这边改过 true
            ClassUtil.loadClass(cls.getName(), true);
        }

    }
}
