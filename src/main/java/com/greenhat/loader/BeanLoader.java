package com.greenhat.loader;

import com.greenhat.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiacheng on 2017/7/19.
 */
public final class BeanLoader {
    private static final Map<Class<?>, Object> Beans = new HashMap<Class<?>, Object>();

    static {
//        List<Class<?>> beanlist = ClassHelper.getClassList();
//        for (Class<?> bean : beanlist){
//            Object obj= ObjectUtil.newInstance(bean.getName());
//            Beans.put(bean,obj);
//        }

        try {
            // 获取应用包路径下所有的类
            List<Class<?>> classList = ClassLoader.getClassList();
            for (Class<?> cls : classList) {
                // 处理带有 Bean/Service/Action/Aspect 注解的类
                if (cls.isAnnotationPresent(Bean.class) ||
                        cls.isAnnotationPresent(Service.class) ||
                        cls.isAnnotationPresent(Controller.class) ||
                        cls.isAnnotationPresent(Aspect.class)) {
                    // 创建 Bean 实例
                    Object beanInstance = cls.newInstance();
                    // 将 Bean 实例放入 Bean Map 中（键为 Bean 类，值为 Bean 实例）
                    Beans.put(cls, beanInstance);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Map<Class<?>, Object> getBeans() {
        return Beans;
    }

    public static <T> T getBean(Class<T> cls) {
        if (!Beans.containsKey(cls)) {
            throw new RuntimeException("无法根据类名获取实例！" + cls);
        }
        return (T) Beans.get(cls);
    }

    public static void setBean(Class<?> cls, Object object) {
        Beans.put(cls, object);
    }
}
