package com.greenhat.loader;


import com.greenhat.annotation.Autowired;
import com.greenhat.util.ArrayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by jiacheng on 2017/7/19.
 */

/**
 * 注入对象
 */
public final class IocLoader {
    private static final Logger logger = LoggerFactory.getLogger(IocLoader.class);
    static {
        try {
            logger.info("IocLoader init start!");
            // 获取并遍历所有的 Bean 类
            Map<Class<?>, Object> beanMap = BeanLoader.getBeans();
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                // 获取 Bean 类与 Bean 实例
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                // 获取 Bean 类中所有的字段（不包括父类中的方法）
                Field[] beanFields = beanClass.getDeclaredFields();
                if (ArrayUtil.isNotEmpty(beanFields)) {
                    // 遍历所有的 Bean 字段
                    for (Field beanField : beanFields) {
                        // 判断当前 Bean 字段是否带有 Inject 注解
                        if (beanField.isAnnotationPresent(Autowired.class)) {
                            Class<?> beanFieldClass = beanField.getType();
                            Object FieldInstance = beanMap.get(beanFieldClass);
                            if (FieldInstance != null) {
                                beanField.setAccessible(true); // 将字段设置为 public
                                beanField.set(beanInstance, FieldInstance); // 设置字段初始值
                            }else {
                                FieldInstance = beanFieldClass.newInstance();
                                beanMap.put(beanFieldClass,FieldInstance);
                                beanField.setAccessible(true); // 将字段设置为 public
                                beanField.set(beanInstance, FieldInstance); // 设置字段初始值
                            }
                        }
                    }
               //     logger.info("IocLoader total [autowired] [{}] bean in [{}] ~",beanFields.length,beanClass.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
