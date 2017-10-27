package com.greenhat.loader;

import com.greenhat.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by jiacheng on 2017/7/21.
 */
public final class DatabaseLoader {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseLoader.class);

    private static final String driver = Config.JDBC_DRIVER;
    private static final String url = Config.JDBC_URL;
    private static final String username = Config.JDBC_USERNAME;
    private static final String password = Config.JDBC_PASSWORD;

    private static ThreadLocal<Connection> connContainer = new ThreadLocal<Connection>();

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() {
        Connection conn=connContainer.get();;
        try {
            // 先从 ThreadLocal 中获取 Connection
            if (conn == null) {
                Class.forName(driver);
                conn= DriverManager.getConnection(url,username,password);
                if (conn != null) {
                    connContainer.set(conn);
                }
            }
        } catch (SQLException e) {
            logger.error("获取数据库连接出错！");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            logger.error("未找到数据库驱动，请检查config.propties中的驱动名称是否正确，或者jar是否引入！", e);
        }
        return conn;
    }

    /**
     * 开启事务
     */
    public static void beginTransaction() {
        Connection conn = getConnection();
        if (conn != null) {
            try {
                conn.setAutoCommit(false);
            } catch (SQLException e) {
                logger.error("开启事务出错！");
                throw new RuntimeException(e);
            } finally {
                connContainer.set(conn);
            }
        }
    }

    /**
     * 提交事务
     */
    public static void commitTransaction() {
        Connection conn = getConnection();
        if (conn != null) {
            try {
                conn.commit();
                conn.close();
            } catch (SQLException e) {
                logger.error("提交事务出错！");
                throw new RuntimeException(e);
            } finally {
                connContainer.remove();
            }
        }
    }

    /**
     * 回滚事务
     */
    public static void rollbackTransaction() {
        Connection conn = getConnection();
        if (conn != null) {
            try {
                conn.rollback();
                conn.close();
            } catch (SQLException e) {
                logger.error("回滚事务出错！");
                throw new RuntimeException(e);
            } finally {
                connContainer.remove();
            }
        }
    }
}
