package com.greenhat.orm;
import com.greenhat.loader.ClassLoader;
import com.greenhat.orm.annotation.Column;
import com.greenhat.orm.annotation.Entity;
import com.greenhat.orm.annotation.Id;
import com.greenhat.orm.annotation.Table;
import com.greenhat.util.ArrayUtil;
import com.greenhat.util.MapUtil;
import com.greenhat.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityHelper {
    private static final Logger logger = LoggerFactory.getLogger(EntityHelper.class);
    /**
     * 实体类 => 表名
     */
    private static final Map<Class<?>, String> entityClassTableNameMap = new HashMap<Class<?>, String>();

    /**
     * 实体类 => (字段名 => 列名)
     */
    private static final Map<Class<?>, Map<String, String>> entityClassFieldMapMap = new HashMap<Class<?>, Map<String, String>>();

    private static final Map<Class<?>,String> pkNameMap=new HashMap<>();

    static {
        logger.info("EntityHelper start init!");
        // 获取并遍历所有实体类
        List<Class<?>> entityClassList = ClassLoader.getClassListByAnnotation(Entity.class);
        if (entityClassList!=null){
            logger.info("Found {} Entity",entityClassList.size());
        }
        for (Class<?> entityClass : entityClassList) {
            initEntityNameMap(entityClass);
            initEntityFieldMapMap(entityClass);
        }
    }

    private static void initEntityNameMap(Class<?> entityClass) {
        // 判断该实体类上是否存在 Table 注解
        String tableName;
        if (entityClass.isAnnotationPresent(Table.class)) {
            // 若已存在，则使用该注解中定义的表名
            tableName = entityClass.getAnnotation(Table.class).value();
        } else {
            // 若不存在，则将实体类名转换为下划线风格的表名
            tableName = StringUtil.camelhumpToUnderline(entityClass.getSimpleName());
        }
        logger.info("Scan Table:{}",tableName);
        entityClassTableNameMap.put(entityClass, tableName);
    }

    private static void initEntityFieldMapMap(Class<?> entityClass) {
        // 获取并遍历该实体类中所有的字段（不包括父类中的方法）
        Field[] fields = entityClass.getDeclaredFields();
        if (ArrayUtil.isNotEmpty(fields)) {
            // 创建一个 fieldMap（用于存放列名与字段名的映射关系）
            Map<String, String> fieldMap = new HashMap<String, String>();
            for (Field field : fields) {
                String fieldName = field.getName();
                String columnName;
                // 判断该字段上是否存在 Column 注解
                if (field.isAnnotationPresent(Column.class)) {
                    // 若已存在，则使用该注解中定义的列名
                    columnName = field.getAnnotation(Column.class).value();
                    if (field.isAnnotationPresent(Id.class)){
                        pkNameMap.put(entityClass,columnName);
                    }
                } else {
                    // 若不存在，则将字段名转换为下划线风格的列名
                    columnName = StringUtil.camelhumpToUnderline(fieldName);
                }
                fieldMap.put(fieldName, columnName);
            }
            entityClassFieldMapMap.put(entityClass, fieldMap);
        }
    }

    public static String getTableName(Class<?> entityClass) {
        return entityClassTableNameMap.get(entityClass);
    }

    public static Map<String, String> getFieldMap(Class<?> entityClass) {
        return entityClassFieldMapMap.get(entityClass);
    }

    public static Map<String, String> getColumnMap(Class<?> entityClass) {
        return MapUtil.invert(getFieldMap(entityClass));
    }

    public static String getColumnName(Class<?> entityClass, String fieldName) {
        String columnName = getFieldMap(entityClass).get(fieldName);
        return StringUtil.isNotEmpty(columnName) ? columnName : fieldName;
    }
}
