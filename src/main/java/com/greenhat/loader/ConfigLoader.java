package com.greenhat.loader;


import com.greenhat.ConfigNames;
import com.greenhat.util.PropsUtil;

import java.util.Map;
import java.util.Properties;

/**
 * 获取属性文件中的属性值
 */
public final class ConfigLoader {

    /**
     * 属性文件对象
     */
    private static final Properties configProps = PropsUtil.loadProps(ConfigNames.CONFIG_PROPS);

    /**
     * 获取jdbc驱动
     *
     * @return
     */
    public static String getJdbcDriver() {
        return PropsUtil.getString(configProps, ConfigNames.JDBC_DRIVER);
    }

    /**
     * 获取jdbc URL
     *
     * @return
     */
    public static String getJdbcURL() {
        return PropsUtil.getString(configProps, ConfigNames.JDBC_URL);
    }
    public static String getJdbcType(){
        return PropsUtil.getString(configProps, ConfigNames.JDBC_TYPE,"mysql");
    }

    /**
     * 获取jdbc username
     *
     * @return
     */
    public static String getJdbcUsername() {
        return PropsUtil.getString(configProps, ConfigNames.JDBC_USERNAME);
    }

    /**
     * 获取jdbc password
     *
     * @return
     */
    public static String getJdbcPassword() {
        return PropsUtil.getString(configProps, ConfigNames.JDBC_PASSWORD);
    }

    /**
     * 获取基础包名
     *
     * @return
     */
    public static String getAppBasePackage() {
        return PropsUtil.getString(configProps, ConfigNames.APP_BASE_PACKAGE);
    }

    /**
     * 获取 JSP路径
     *
     * @return
     */
    public static String getAppJspPath() {
        return PropsUtil.getString(configProps, ConfigNames.APP_JSP_PATH, "/WEB-INF/view/");
    }

    /**
     * 获取 资源目录
     *
     * @return
     */
    public static String getAppAssetPath() {
        return PropsUtil.getString(configProps, ConfigNames.APP_ASSET_PATH, "/asset/");
    }

    public static String getAppWwwPath() {
        return PropsUtil.getString(configProps, ConfigNames.APP_WWW_PATH, "/www/");
    }
    /**
     * 获取 String 类型的属性值
     */
    public static String getString(String key) {
        return PropsUtil.getString(configProps, key);
    }

    /**
     * 获取 String 类型的属性值（可指定默认值）
     */
    public static String getString(String key, String defaultValue) {
        return PropsUtil.getString(configProps, key, defaultValue);
    }

    /**
     * 获取 int 类型的属性值
     */
    public static int getInt(String key) {
        return PropsUtil.getNumber(configProps, key);
    }

    /**
     * 获取 int 类型的属性值（可指定默认值）
     */
    public static int getInt(String key, int defaultValue) {
        return PropsUtil.getNumber(configProps, key, defaultValue);
    }

    /**
     * 获取 boolean 类型的属性值
     */
    public static boolean getBoolean(String key) {
        return PropsUtil.getBoolean(configProps, key);
    }

    /**
     * 获取 int 类型的属性值（可指定默认值）
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        return PropsUtil.getBoolean(configProps, key, defaultValue);
    }

    /**
     * 获取指定前缀的相关属性
     *
     * @since 2.2
     */
    public static Map<String, Object> getMap(String prefix) {
        return PropsUtil.getMap(configProps, prefix);
    }
}
