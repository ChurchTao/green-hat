package com.greenhat;


import com.greenhat.loader.ConfigLoader;

public interface Config {
    String UTF_8 = "UTF-8";
    String VERSION = "1.1";
    String CONFIG_PROPS = "config.properties";
    //===============
    String JDBC_DRIVER = ConfigLoader.getString("jdbc.driver", "com.mysql.jdbc.Driver");
//    String JDBC_TEMP=ConfigLoader.getString("jdbc.url");
//    String JDBC_UTF_8="?useUnicode=true&characterEncoding=UTF-8";
//    String JDBC_URL = JDBC_TEMP.contains(JDBC_UTF_8)?JDBC_TEMP:(JDBC_TEMP+JDBC_UTF_8);
    String JDBC_URL =ConfigLoader.getString("jdbc.url");

    String JDBC_TYPE = ConfigLoader.getString("jdbc.type","mysql");
    String JDBC_USERNAME = ConfigLoader.getString("jdbc.username");
    String JDBC_PASSWORD = ConfigLoader.getString("jdbc.password");
    String JDBC_PRINTSQL = ConfigLoader.getString("printSQL","true");

    String APP_BASE_PACKAGE = ConfigLoader.getString("app.base_package");
    String APP_JSP_PATH = ConfigLoader.getString("app.jsp_path","/WEB-INF/view/");
    String APP_ASSET_PATH = ConfigLoader.getString("app.asset_path","/asset/");
    String APP_WWW_PATH = ConfigLoader.getString("app.www_path","/www/");
    String APP_PATH_404 = ConfigLoader.getString("app.404_path","404.html");
    String HOME_PAGE = ConfigLoader.getString("app.home_page", "/index.html");
    int UPLOAD_LIMIT = ConfigLoader.getInt("app.upload_limit", 10);
}
