package com.greenhat;

import com.greenhat.jdbc.DataAccessor;
import com.greenhat.jdbc.ds.DataSourceFactory;
import com.greenhat.jdbc.ds.impl.DefaultDataSourceFactory;
import com.greenhat.jdbc.impl.DefaultDataAccessor;
import com.greenhat.loader.ConfigLoader;
import com.greenhat.util.ObjectUtil;
import com.greenhat.util.StringUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InstanceFactory {

    /**
     * 用于缓存对应的实例
     */
    private static final Map<String, Object> cache = new ConcurrentHashMap<String, Object>();
    private static final String DS_FACTORY = "ds_factory";
    private static final String DATA_ACCESSOR = "data_accessor";
    public static DataSourceFactory getDataSourceFactory() {
        return getInstance(DS_FACTORY, DefaultDataSourceFactory.class);
    }
    public static DataAccessor getDataAccessor() {
        return getInstance(DATA_ACCESSOR, DefaultDataAccessor.class);
    }
    @SuppressWarnings("unchecked")
    public static <T> T getInstance(String cacheKey, Class<T> defaultImplClass) {
        // 若缓存中存在对应的实例，则返回该实例
        if (cache.containsKey(cacheKey)) {
            return (T) cache.get(cacheKey);
        }
        // 从配置文件中获取相应的接口实现类配置
        String implClassName = ConfigLoader.getString(cacheKey);
        // 若实现类配置不存在，则使用默认实现类
        if (StringUtil.isEmpty(implClassName)) {
            implClassName = defaultImplClass.getName();
        }
        // 通过反射创建该实现类对应的实例
        T instance = ObjectUtil.newInstance(implClassName);
        // 若该实例不为空，则将其放入缓存
        if (instance != null) {
            cache.put(cacheKey, instance);
        }
        // 返回该实例
        return instance;
    }
}
