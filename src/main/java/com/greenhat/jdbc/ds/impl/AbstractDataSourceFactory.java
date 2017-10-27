package com.greenhat.jdbc.ds.impl;


import com.greenhat.Config;
import com.greenhat.jdbc.ds.DataSourceFactory;
import com.greenhat.loader.ConfigLoader;

import javax.sql.DataSource;

public abstract class AbstractDataSourceFactory<T extends DataSource> implements DataSourceFactory {

    protected  final String driver = Config.JDBC_DRIVER;
    protected  final String url = Config.JDBC_URL;
    protected  final String username = Config.JDBC_USERNAME;
    protected  final String password = Config.JDBC_PASSWORD;

    @Override
    public final T getDataSource() {
        // 创建数据源对象
        T ds = createDataSource();
        // 设置基础属性
        setDriver(ds, driver);
        setUrl(ds, url);
        setUsername(ds, username);
        setPassword(ds, password);
        // 设置高级属性
        setAdvancedConfig(ds);
        return ds;
    }

    public abstract T createDataSource();

    public abstract void setDriver(T ds, String driver);

    public abstract void setUrl(T ds, String url);

    public abstract void setUsername(T ds, String username);

    public abstract void setPassword(T ds, String password);

    public abstract void setAdvancedConfig(T ds);
}
