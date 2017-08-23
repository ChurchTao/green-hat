package com.greenhat;


import com.greenhat.loader.ConfigLoader;

public interface ConfigNames {
    String UTF_8 = "UTF-8";
    String CONFIG_PROPS = "config.properties";
    String JDBC_DRIVER = "jdbc.driver";
    String JDBC_URL = "jdbc.url";
    String JDBC_TYPE ="jdbc.type";
    String JDBC_USERNAME = "jdbc.username";
    String JDBC_PASSWORD = "jdbc.password";
    String APP_BASE_PACKAGE = "app.base_package";
    String APP_JSP_PATH = "app.jsp_path";
    String APP_ASSET_PATH = "app.asset_path";
    String APP_WWW_PATH = "app.www_path";
    String APP_PATH_404 = "app.404_path";
    String HOME_PAGE = ConfigLoader.getString("app.home_page", "/index.html");
    String VERSION ="1.0";
    int UPLOAD_LIMIT = ConfigLoader.getInt("app.upload_limit", 10);
}
