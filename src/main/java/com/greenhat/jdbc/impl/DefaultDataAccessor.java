package com.greenhat.jdbc.impl;

import com.greenhat.jdbc.DataAccessor;
import com.greenhat.jdbc.DatabaseLoader;
import com.greenhat.loader.ConfigLoader;
import com.greenhat.orm.EntityHelper;
import com.greenhat.util.ArrayUtil;
import com.greenhat.util.MapUtil;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 基于 Apache Commons DbUtils 实现
 */
public class DefaultDataAccessor implements DataAccessor {

    private static final Logger logger = LoggerFactory.getLogger(DefaultDataAccessor.class);

    private final QueryRunner queryRunner;

    private static final boolean isPrintSQL = ConfigLoader.getPrintSQL().equals("true");

    public DefaultDataAccessor() {
        DataSource dataSource = DatabaseLoader.getDataSource();
        queryRunner = new QueryRunner(dataSource);
    }

    @Override
    public <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
        T result;
        try {
            Map<String, String> columnMap = EntityHelper.getColumnMap(entityClass);
            if (MapUtil.isNotEmpty(columnMap)) {
                result = queryRunner.query(sql, new BeanHandler<T>(entityClass, new BasicRowProcessor(new BeanProcessor(columnMap))), params);
            } else {
                result = queryRunner.query(sql, new BeanHandler<T>(entityClass), params);
            }
        } catch (SQLException e) {
            logger.error("查询出错！");
            throw new RuntimeException(e);
        }
        printSQL(sql);
        return result;
    }

    @Override
    public <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> result;
        try {
            Map<String, String> columnMap = EntityHelper.getColumnMap(entityClass);
            if (MapUtil.isNotEmpty(columnMap)) {
                result = queryRunner.query(sql, new BeanListHandler<T>(entityClass, new BasicRowProcessor(new BeanProcessor(columnMap))), params);
            } else {
                result = queryRunner.query(sql, new BeanListHandler<T>(entityClass), params);
            }
        } catch (SQLException e) {
            logger.error("查询出错！");
            throw new RuntimeException(e);
        }
        printSQL(sql);
        return result;
    }

    @Override
    public <K, V> Map<K, V> queryEntityMap(Class<V> entityClass, String sql, Object... params) {
        Map<K, V> entityMap;
        try {
            entityMap = queryRunner.query(sql, new BeanMapHandler<K, V>(entityClass), params);
        } catch (SQLException e) {
            logger.error("查询出错！");
            throw new RuntimeException(e);
        }
        printSQL(sql);
        return entityMap;
    }

    @Override
    public Object[] queryArray(String sql, Object... params) {
        Object[] array;
        try {
            array = queryRunner.query(sql, new ArrayHandler(), params);
        } catch (SQLException e) {
            logger.error("查询出错！");
            throw new RuntimeException(e);
        }
        printSQL(sql);
        return array;
    }

    @Override
    public List<Object[]> queryArrayList(String sql, Object... params) {
        List<Object[]> arrayList;
        try {
            arrayList = queryRunner.query(sql, new ArrayListHandler(), params);
        } catch (SQLException e) {
            logger.error("查询出错！");
            throw new RuntimeException(e);
        }
        printSQL(sql);
        return arrayList;
    }

    @Override
    public Map<String, Object> queryMap(String sql, Object... params) {
        Map<String, Object> map;
        try {
            map = queryRunner.query(sql, new MapHandler(), params);
        } catch (SQLException e) {
            logger.error("查询出错！");
            throw new RuntimeException(e);
        }
        printSQL(sql);
        return map;
    }

    @Override
    public List<Map<String, Object>> queryMapList(String sql, Object... params) {
        List<Map<String, Object>> fieldMapList;
        try {
            fieldMapList = queryRunner.query(sql, new MapListHandler(), params);
        } catch (SQLException e) {
            logger.error("查询出错！");
            throw new RuntimeException(e);
        }
        printSQL(sql);
        return fieldMapList;
    }

    @Override
    public <T> T queryColumn(String sql, Object... params) {
        T obj;
        try {
            obj = queryRunner.query(sql, new ScalarHandler<T>(), params);
        } catch (SQLException e) {
            logger.error("查询出错！");
            throw new RuntimeException(e);
        }
        printSQL(sql);
        return obj;
    }

    @Override
    public <T> List<T> queryColumnList(String sql, Object... params) {
        List<T> list;
        try {
            list = queryRunner.query(sql, new ColumnListHandler<T>(), params);
        } catch (SQLException e) {
            logger.error("查询出错！");
            throw new RuntimeException(e);
        }
        printSQL(sql);
        return list;
    }

    @Override
    public <T> Map<T, Map<String, Object>> queryColumnMap(String column, String sql, Object... params) {
        Map<T, Map<String, Object>> map;
        try {
            map = queryRunner.query(sql, new KeyedHandler<T>(column), params);
        } catch (SQLException e) {
            logger.error("查询出错！");
            throw new RuntimeException(e);
        }
        printSQL(sql);
        return map;
    }

    @Override
    public long queryCount(String sql, Object... params) {
        long result;
        try {
            result = queryRunner.query(sql, new ScalarHandler<Long>("count(*)"), params);
        } catch (SQLException e) {
            logger.error("查询出错！");
            throw new RuntimeException(e);
        }
        printSQL(sql);
        return result;
    }

    @Override
    public int update(String sql, Object... params) {
        int result;
        try {
            Connection conn = DatabaseLoader.getConnection();
            result = queryRunner.update(conn, sql, params);
        } catch (SQLException e) {
            logger.error("更新出错！");
            throw new RuntimeException(e);
        }
        printSQL(sql);
        return result;
    }

    @Override
    public Serializable insertReturnPK(String sql, Object... params) {
        Serializable key = null;
        try {
            Connection conn = DatabaseLoader.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            if (ArrayUtil.isNotEmpty(params)) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }
            int rows = pstmt.executeUpdate();
            if (rows == 1) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    key = (Serializable) rs.getObject(1);
                }
            }
        } catch (SQLException e) {
            logger.error("插入出错！");
            throw new RuntimeException(e);
        }
        printSQL(sql);
        return key;
    }

    private static void printSQL(String sql) {
        if (isPrintSQL) logger.info("[GreenHat-JDBC] SQL - {}", sql);
    }
}
