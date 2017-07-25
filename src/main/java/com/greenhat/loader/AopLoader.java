package com.greenhat.loader;

import com.greenhat.annotation.Aspect;
import com.greenhat.annotation.Service;
import com.greenhat.proxy.AspectProxy;
import com.greenhat.proxy.Proxy;
import com.greenhat.proxy.ProxyManager;
import com.greenhat.proxy.TransactionProxy;
import com.greenhat.util.CollectionUtil;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Created by jiacheng on 2017/7/21.
 */
public final class AopLoader {
    static {
        try {
            // 创建 Proxy Map（用于 存放代理类 与 目标类列表 的映射关系）
            Map<Class<?>, List<Class<?>>> proxyMap = createProxyMap();
            // 创建 Target Map（用于 存放目标类 与 代理类列表 的映射关系）
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
            // 遍历 Target Map
            for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
                // 分别获取 map 中的 key 与 value
                Class<?> targetClass = targetEntry.getKey();
                List<Proxy> proxyList = targetEntry.getValue();
                // 创建代理实例
                Object proxyInstance = ProxyManager.createProxy(targetClass, proxyList);
                // 用代理实例覆盖目标实例，并放入 Bean 容器中
                BeanLoader.setBean(targetClass, proxyInstance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static List<Class<?>> createTargetClassList(Aspect aspect) throws Exception {
        List<Class<?>> targetClassList = new ArrayList<Class<?>>();
        Class<? extends Annotation> annotation = aspect.value();
        if (annotation != null && !annotation.equals(Aspect.class)) {
            targetClassList.addAll(ClassLoader.getClassListByAnnotation(annotation));
        }
        return targetClassList;
    }

    private static Map<Class<?>, List<Class<?>>> createProxyMap() throws Exception {
        Map<Class<?>, List<Class<?>>> proxyMap = new LinkedHashMap<Class<?>, List<Class<?>>>();
        // 添加相关代理

        addAspectProxy(proxyMap);      // 切面代理
        addTransactionProxy(proxyMap); // 事务代理
        return proxyMap;
    }

    private static void addAspectProxy(Map<Class<?>, List<Class<?>>> proxyMap) throws Exception {
        // 获取插件包名下父类为 PluginProxy 的所有类（插件代理类）
        List<Class<?>> proxyClassList = ClassLoader.getClassListBySuper(AspectProxy.class);
        if (CollectionUtil.isNotEmpty(proxyClassList)) {
            // 遍历所有插件代理类
            for (Class<?> proxyClass : proxyClassList) {
                if (proxyClass.isAnnotationPresent(Aspect.class)) {
                    Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                    List<Class<?>> targetClassList = createTargetClassList(aspect);
                    proxyMap.put(proxyClass, targetClassList);
                }
            }
        }
    }

    private static void addTransactionProxy(Map<Class<?>, List<Class<?>>> proxyMap) {
        // 使用 TransactionProxy 代理所有 Service 类
        List<Class<?>> serviceClassList = ClassLoader.getClassListByAnnotation(Service.class);
        proxyMap.put(TransactionProxy.class, serviceClassList);
    }

    private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, List<Class<?>>> proxyMap) throws Exception {
        Map<Class<?>, List<Proxy>> targetMap = new HashMap<Class<?>, List<Proxy>>();
        // 遍历 Proxy Map
        for (Map.Entry<Class<?>, List<Class<?>>> proxyEntry : proxyMap.entrySet()) {
            // 分别获取 map 中的 key 与 value
            Class<?> proxyClass = proxyEntry.getKey();
            List<Class<?>> targetClassList = proxyEntry.getValue();
            // 遍历目标类列表
            for (Class<?> targetClass : targetClassList) {
                // 创建代理类（切面类）实例
                Proxy baseAspect = (Proxy) proxyClass.newInstance();
                // 初始化 Target Map
                if (targetMap.containsKey(targetClass)) {
                    targetMap.get(targetClass).add(baseAspect);
                } else {
                    List<Proxy> baseAspectList = new ArrayList<Proxy>();
                    baseAspectList.add(baseAspect);
                    targetMap.put(targetClass, baseAspectList);
                }
            }
        }
        return targetMap;
    }
}
